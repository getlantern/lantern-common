package org.lantern.loggly;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.lantern.JsonUtils;

public class LogglyMessage {
    private static final Sanitizer[] SANITIZERS = new Sanitizer[] {
            new IPv4Sanitizer()
    };

    private String reporterId;
    private String message;
    private Date occurredAt;
    private String locationInfo;
    private Throwable throwable;
    private String throwableOrigin;
    private String stackTrace;
    private Object extra;
    private AtomicInteger nSimilarSuppressed = new AtomicInteger(0);

    public LogglyMessage(String reporterId, String message, Date occurredAt) {
        this.reporterId = reporterId;
        this.message = message;
        this.occurredAt = occurredAt;
    }

    public String getReporterId() {
        return reporterId;
    }

    public String getMessage() {
        return message;
    }

    public Date getOccurredAt() {
        return occurredAt;
    }

    /**
     * Sanitizes the {@link #message} and {@link #stackTrace} to remove
     * sensitive data.
     * 
     * @return this
     */
    public LogglyMessage sanitized() {
        if (message != null) {
            message = sanitize(message);
        }
        if (stackTrace != null) {
            stackTrace = sanitize(stackTrace);
        }
        return this;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    @JsonIgnore
    public Throwable getThrowable() {
        return throwable;
    }

    public LogglyMessage setThrowable(Throwable throwable) {
        this.throwable = throwable;
        if (throwable == null) {
            stackTrace = null;
            throwableOrigin = null;
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            pw.close();
            stackTrace = sw.getBuffer().toString();
            throwableOrigin = throwable.getStackTrace()[0].toString();
        }
        return this;
    }

    /**
     * Returns a key that uniquely identifies this message.
     * 
     * @return
     */
    public String getKey() {
        if (locationInfo != null) {
            return locationInfo;
        } else {
            return throwableOrigin;
        }
    }

    public Object getExtra() {
        return extra;
    }

    public LogglyMessage setExtra(Object extra) {
        this.extra = extra;
        return this;
    }
    
    public LogglyMessage setExtraFromJson(String json) {
        this.extra = json != null ? JsonUtils.decode(json, Map.class) : null;
        return this;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public int getnSimilarSuppressed() {
        return nSimilarSuppressed.get();
    }

    public LogglyMessage incrementNsimilarSuppressed() {
        this.nSimilarSuppressed.incrementAndGet();
        return this;
    }

    public LogglyMessage setnSimilarSuppressed(int nSimilarSuppressed) {
        this.nSimilarSuppressed.set(nSimilarSuppressed);
        return this;
    }

    /**
     * Applies all {@link #SANITIZERS}s to the original string.
     * 
     * @param original
     * @return
     */
    private static String sanitize(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        String result = original;
        for (Sanitizer filter : SANITIZERS) {
            result = filter.sanitize(result);
        }
        return result;
    }

    private static interface Sanitizer {
        /**
         * Sanitize the given original string.
         * 
         * @param original
         * @return the sanitized string
         */
        String sanitize(String original);
    }

    /**
     * Sanitizer that sanitizes content by replacing occurrences of a regex with
     * a static string.
     */
    private static class RegexSanitizer implements Sanitizer {
        private final Pattern pattern;
        private final String replacement;

        /**
         * 
         * @param regex
         *            the regex
         * @param replacement
         *            the string with which to replace occurrences of the regex
         */
        public RegexSanitizer(String regex, String replacement) {
            super();
            this.pattern = Pattern.compile(regex);
            this.replacement = replacement;
        }

        @Override
        public String sanitize(String original) {
            Matcher matcher = pattern.matcher(original);
            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(result, replacement);
            }
            matcher.appendTail(result);
            return result.toString();
        }
    }

    /**
     * A {@link Sanitizer} that replaces everything that looks like an IPv4
     * address with ???.???.???.???.
     */
    private static class IPv4Sanitizer extends RegexSanitizer {
        private static final String IP_REGEX = "(?:[0-9]{1,3}\\.){3}[0-9]{1,3}";
        private static final String IP_REPLACEMENT = "???.???.???.???";

        public IPv4Sanitizer() {
            super(IP_REGEX, IP_REPLACEMENT);
        }
    }
    
    public static void main(String[] args) throws Exception {
        // Testing
        LogglyMessage msg = new LogglyMessage("reporter", "message", new Date())
            .setExtraFromJson("{\"key\": \"value\", \"otherKey\": 5}");
        System.out.println(msg.getExtra());
    }
}
