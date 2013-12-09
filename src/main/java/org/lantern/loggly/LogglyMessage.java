package org.lantern.loggly;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.jackson.annotate.JsonIgnore;

public class LogglyMessage {
    private String reporterId;
    private String message;
    private Date occurredAt;
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

    public String getThrowableOrigin() {
        return throwableOrigin;
    }

    public Object getExtra() {
        return extra;
    }

    public LogglyMessage setExtra(Object extra) {
        this.extra = extra;
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
}
