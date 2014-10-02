package org.lantern.papertrail;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;
import org.lantern.CertPinningSSLContextSource;

public abstract class Papertrail {
    /**
     * Constant to be used in trimming log messages to hard limit of 8 kilobytes
     * as specified in the Papertrail support documentation.
     */
    private static final int MAX_MESSAGE_SIZE = 8192;
    private static final String PAPERTRAIL_CERT = "-----BEGIN CERTIFICATE-----\nMIIFKTCCBBGgAwIBAgIRAKykYSWWhRYl9j5NgHG/ntQwDQYJKoZIhvcNAQEFBQAw\ncjELMAkGA1UEBhMCR0IxGzAZBgNVBAgTEkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4G\nA1UEBxMHU2FsZm9yZDEaMBgGA1UEChMRQ09NT0RPIENBIExpbWl0ZWQxGDAWBgNV\nBAMTD0Vzc2VudGlhbFNTTCBDQTAeFw0xNDA0MDgwMDAwMDBaFw0xNTA0MDgyMzU5\nNTlaMGExITAfBgNVBAsTGERvbWFpbiBDb250cm9sIFZhbGlkYXRlZDEeMBwGA1UE\nCxMVRXNzZW50aWFsU1NMIFdpbGRjYXJkMRwwGgYDVQQDFBMqLnBhcGVydHJhaWxh\ncHAuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArDyKVx7N9iDG\n43G5VYn2u4K6Zw65bOkeCoDeuftMHoIFdAx78pLhdHyAAaJz7BjslVlu5hMMaaUL\n0LS0KKXMB0l/Y9OZJDNfGKKpYmxgKf3Q9r7MBFMFoA0pcViSVfy7x5MlZ7rHx4ef\n2cyKBBmVKnf+8MYdpFKVcAoLO3b3R1ewAVCUxDldVwD3TQlCbY/euwV9b7UMyQxS\nF5Ceit4/Hxg78SumK2Vv8DdBbyYThru7Xpo3qfalyeB+krm7OQ5yVUIgy6OALiTH\nY1QL7eXH3J20Tx7M4gvts2TFrMGJy8YP36ovWqnsjBlwLo1zfd+cfOWk64WnQ858\nrtUWrjQHjwIDAQABo4IByTCCAcUwHwYDVR0jBBgwFoAU2svqrVsIXcz//CZUzknl\nVcY49PgwHQYDVR0OBBYEFFf8lVzD+G3naHElzeQkx4n5wOj1MA4GA1UdDwEB/wQE\nAwIFoDAMBgNVHRMBAf8EAjAAMDQGA1UdJQQtMCsGCCsGAQUFBwMBBggrBgEFBQcD\nAgYKKwYBBAGCNwoDAwYJYIZIAYb4QgQBME8GA1UdIARIMEYwOgYLKwYBBAGyMQEC\nAgcwKzApBggrBgEFBQcCARYdaHR0cHM6Ly9zZWN1cmUuY29tb2RvLmNvbS9DUFMw\nCAYGZ4EMAQIBMDsGA1UdHwQ0MDIwMKAuoCyGKmh0dHA6Ly9jcmwuY29tb2RvY2Eu\nY29tL0Vzc2VudGlhbFNTTENBLmNybDBuBggrBgEFBQcBAQRiMGAwOAYIKwYBBQUH\nMAKGLGh0dHA6Ly9jcnQuY29tb2RvY2EuY29tL0Vzc2VudGlhbFNTTENBXzIuY3J0\nMCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5jb21vZG9jYS5jb20wMQYDVR0RBCow\nKIITKi5wYXBlcnRyYWlsYXBwLmNvbYIRcGFwZXJ0cmFpbGFwcC5jb20wDQYJKoZI\nhvcNAQEFBQADggEBAFkoJT8+Dmd2+7ZNIWt6V9/WlNA9IyDntH1JZL1GKnVtJ0co\nm6mOAU9kOlvdl8Aoh9FemApBNUbpVCtOQYLmmQ0oNuQUtTXF/A3HoYizEMMZov/P\nzAS85rw9xyLussiP3E6vn4EsQB843I2gJ8Q8VLpp/nQcllx4GNXHqTe1F1uhZISK\nBDhDIgRqgppbl4K5/eIkAEd/2C/9YwVTTsWieYqO5zKF2d23UXcHGmuKE7VSUBLh\n/VeNMJv/k8RFO2PfM6sxvi2k5brrmIa0IJnPuogE+UCBoLWdJBnSFU/LcUQfePL3\nWD7ov9l43i1HqTzQgzxJk40vORMa3JISGrb1FLs=\n-----END CERTIFICATE-----";

    private final String host;
    private final int port;
    private final CertPinningSSLContextSource sslContextSource;
    private volatile SSLSocket socket;
    private volatile BufferedWriter writer;

    public Papertrail(String host, int port) {
        this.host = host;
        this.port = port;
        this.sslContextSource = new CertPinningSSLContextSource("papertrail",
                PAPERTRAIL_CERT);
    }

    protected abstract Socket newPlainTextSocket() throws Exception;

    public void log(String message) {
        try {
            tryToLog(message);
        } catch (Exception ee) {
            try {
                IOUtils.closeQuietly(writer);
                IOUtils.closeQuietly(socket);
                writer = null;
                socket = null;
                tryToLog(message);
            } catch (Exception e) {
                System.err.println("Unable to log message to Papertrail: "
                        + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    private void tryToLog(String message) throws Exception {
        BufferedWriter writer = getWriter();
        writer.write(truncate(message));
        writer.flush();
    }

    private synchronized BufferedWriter getWriter() throws Exception {
        if (this.socket == null || this.socket.isClosed()) {
            SSLSocketFactory socketFactory = sslContextSource.getContext(host)
                    .getSocketFactory();
            this.socket = (SSLSocket) socketFactory.createSocket(
                    newPlainTextSocket(), host, port, true);
            this.socket.startHandshake();
            this.writer = new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
        }
        return this.writer;
    }

    private static String truncate(String text) {
        return text.substring(0,
                (text.length() < MAX_MESSAGE_SIZE) ? text.length()
                        : MAX_MESSAGE_SIZE);
    }
}
