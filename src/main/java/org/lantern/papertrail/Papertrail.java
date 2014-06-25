package org.lantern.papertrail;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Papertrail {
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

    public void log(String message) {
        try {
            BufferedWriter writer = getWriter();
            writer.write(truncate(message));
            writer.flush();
        } catch (Exception e) {
            System.err.println("Unable to write message to Papertrail: "
                    + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private synchronized BufferedWriter getWriter() throws Exception {
        if (this.socket == null || this.socket.isClosed()) {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory
                    .getDefault();
            this.socket = (SSLSocket) socketFactory.createSocket(host, port);
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
