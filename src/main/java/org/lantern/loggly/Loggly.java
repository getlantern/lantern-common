package org.lantern.loggly;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.lantern.CertPinningSSLContextSource;
import org.lantern.HttpURLClient;
import org.lantern.JsonUtils;

public class Loggly extends HttpURLClient {
    public static final String LOGGLY_HOST = "logs-01.loggly.com";
    private static final String URL = String
            .format("https://%1$s/inputs/469973d5-6eaf-445a-be71-cf27141316a1/tag/http-client/",
                    LOGGLY_HOST);
    private static final String LOGGLY_CERT = "-----BEGIN CERTIFICATE-----\nMIIFgTCCBGmgAwIBAgIHSxvnXYSbKzANBgkqhkiG9w0BAQUFADCB3DELMAkGA1UE\nBhMCVVMxEDAOBgNVBAgTB0FyaXpvbmExEzARBgNVBAcTClNjb3R0c2RhbGUxJTAj\nBgNVBAoTHFN0YXJmaWVsZCBUZWNobm9sb2dpZXMsIEluYy4xOTA3BgNVBAsTMGh0\ndHA6Ly9jZXJ0aWZpY2F0ZXMuc3RhcmZpZWxkdGVjaC5jb20vcmVwb3NpdG9yeTEx\nMC8GA1UEAxMoU3RhcmZpZWxkIFNlY3VyZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0\neTERMA8GA1UEBRMIMTA2ODg0MzUwHhcNMTQwNDEwMDAxMDQ3WhcNMTUwNDEwMDAx\nMDQ3WjBAMSEwHwYDVQQLExhEb21haW4gQ29udHJvbCBWYWxpZGF0ZWQxGzAZBgNV\nBAMTEmxvZ3MtMDEubG9nZ2x5LmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC\nAQoCggEBAKjolBIkIrzLrtZ+DdtUIGd1WaPMXnayGb9Cf50PGTqB7TrNWB9nlw8P\nflgNCp1sd3K7Umrn0CTim79Nrm5a86FS6qwNS5jJ+70GHo0bILO9SI0XA5r10f1b\nsmkXP8JQmqmwoCbBg0N5e4HRUWstHFgJBPOpYEO5ecO+1+Iya8G8WJQoV+biVSFZ\nXfuLFSJe3HvG76ok6oF5HCjRfkSfmmTWhJPMdlpO0QTFt17NXe85CLEe1yGStE0q\nVuiF2StSllmf1nbUr4WE5Oan+7TcOGCJBdq/pvGRbU+jxkE+XIlGUdd7btdupmaj\nXIkmfYXfczuA24qWyk7dx1LVSoOpZ6UCAwEAAaOCAeEwggHdMA8GA1UdEwEB/wQF\nMAMBAQAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMA4GA1UdDwEB/wQE\nAwIFoDA5BgNVHR8EMjAwMC6gLKAqhihodHRwOi8vY3JsLnN0YXJmaWVsZHRlY2gu\nY29tL3NmczEtMjcuY3JsMFkGA1UdIARSMFAwTgYLYIZIAYb9bgEHFwEwPzA9Bggr\nBgEFBQcCARYxaHR0cDovL2NlcnRpZmljYXRlcy5zdGFyZmllbGR0ZWNoLmNvbS9y\nZXBvc2l0b3J5LzCBjQYIKwYBBQUHAQEEgYAwfjAqBggrBgEFBQcwAYYeaHR0cDov\nL29jc3Auc3RhcmZpZWxkdGVjaC5jb20vMFAGCCsGAQUFBzAChkRodHRwOi8vY2Vy\ndGlmaWNhdGVzLnN0YXJmaWVsZHRlY2guY29tL3JlcG9zaXRvcnkvc2ZfaW50ZXJt\nZWRpYXRlLmNydDAfBgNVHSMEGDAWgBRJS1In0Ru88qEhamJ7UUJ6itfVVjA1BgNV\nHREELjAsghJsb2dzLTAxLmxvZ2dseS5jb22CFnd3dy5sb2dzLTAxLmxvZ2dseS5j\nb20wHQYDVR0OBBYEFPQwzyhHMVgp4HSS5CS8TEZEFfBaMA0GCSqGSIb3DQEBBQUA\nA4IBAQAGUSPQo8+/Be0IrIBE/q0AXPnWKvGIDfzVSi/zkfp3MD1kiuKn2FukAIDs\nE0eKZF3+KhE73h5YNeRbtCP5WVL5Y1d0vv5B/jZhMhOKUbPC2zAFR3W2SaqjwWm2\nyjR4uXTdnoVwUpJBdZC1fqcDT6pgRb361V0MDlnPl2IIDlI+JLv917JyuwKhwJ3p\n4BdKqU+jM6kBzNyCvD2WEAmL6iDK7RzKn0EvZEi6FGLssSMsmHCSMQGqVdCycaUd\nZ2Jo5+i5iySJI7cTzZEdrV9LfEmQ+FgFQhHmumbKNv7V5W0P0Zxdu4vkWzZMeU7p\nty8tlLv/y1Qd/LsmzWs8C8ZxgY0z\n-----END CERTIFICATE-----";

    private final boolean inTestMode;
    private final ConcurrentHashMap<String, LogglyMessage> messageCounts = new ConcurrentHashMap<String, LogglyMessage>();

    public Loggly(boolean inTestMode) {
        this(inTestMode, null);
    }

    public Loggly(boolean inTestMode, InetSocketAddress proxyAddress) {
        super(proxyAddress);
        this.inTestMode = inTestMode;
        setSslContextSource(new CertPinningSSLContextSource("loggly", LOGGLY_CERT));
    }

    public void log(LogglyMessage msg) {
        String msgKey = msg.getKey();
        if (msgKey == null) {
            // Can't dedupe, report immediately
            reportToLoggly(msg);
        } else {
            LogglyMessage previouslySet = messageCounts.putIfAbsent(
                    msgKey, msg);
            if (previouslySet == null) {
                reportToLoggly(msg);
            } else {
                previouslySet.incrementNsimilarSuppressed();
                boolean report = previouslySet.getnSimilarSuppressed() >= 200;
                if (report) {
                    // Report the most recent message with the full count of
                    // similar suppressed
                    msg.setnSimilarSuppressed(previouslySet
                            .getnSimilarSuppressed());
                    reportToLoggly(msg);
                    previouslySet.setnSimilarSuppressed(0);
                }
            }
        }
    }

    private void reportToLoggly(LogglyMessage msg) {
        try {
            OutputStream out = null;
            HttpURLConnection conn = null;
            try {
                conn = newConn(URL);
                if (inTestMode) {
                    conn.setRequestProperty("X-LOGGLY-TAG", "test");
                }
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                out = conn.getOutputStream();
                JsonUtils.OBJECT_MAPPER.writeValue(out, msg);
                int code = conn.getResponseCode();
                if (code >= 300) {
                    // will be logged below
                    throw new Exception("Got " + code + " response:\n"
                            + conn.getResponseMessage());
                }
            } finally {
                IOUtils.closeQuietly(out);
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to submit to Loggly");
            e.printStackTrace(System.err);
        }
    }
}
