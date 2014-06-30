package org.lantern.papertrail;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;

public abstract class Papertrail {
    /**
     * Constant to be used in trimming log messages to hard limit of 8 kilobytes
     * as specified in the Papertrail support documentation.
     */
    private static final int MAX_MESSAGE_SIZE = 8192;

    private final String host;
    private final int port;
    private volatile SSLSocket socket;
    private volatile BufferedWriter writer;

    public Papertrail(String host, int port) {
        this.host = host;
        this.port = port;
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
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory
                    .getDefault();
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
