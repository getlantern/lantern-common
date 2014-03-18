package org.lantern.loggly;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import org.lantern.HttpURLClient;
import org.lantern.JsonUtils;

public class Loggly extends HttpURLClient {
    private static final String URL = "https://logs-01.loggly.com/inputs/469973d5-6eaf-445a-be71-cf27141316a1/tag/http-client/";

    private final boolean inTestMode;
    private final ConcurrentHashMap<String, LogglyMessage> messageCounts = new ConcurrentHashMap<String, LogglyMessage>();

    public Loggly(boolean inTestMode) {
        this(inTestMode, null);
    }

    public Loggly(boolean inTestMode, InetSocketAddress proxyAddress) {
        super(proxyAddress);
        this.inTestMode = inTestMode;
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
            HttpURLConnection conn = newConn(URL);
            if (inTestMode) {
                conn.setRequestProperty("X-LOGGLY-TAG", "test");
            }
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            try {
                JsonUtils.OBJECT_MAPPER.writeValue(out, msg);
                int code = conn.getResponseCode();
                if (code >= 300) {
                    // will be logged below
                    throw new Exception("Got " + code + " response:\n"
                            + conn.getResponseMessage());
                }
            } finally {
                try {
                    out.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to submit to Loggly");
            e.printStackTrace(System.err);
        }
    }
}
