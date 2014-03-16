package org.lantern.monitoring;

import java.util.Map;

public class StatsResponse {
    private boolean succeeded;
    private String error;
    private Map<String, Map<String, Stats>> dims;

    public StatsResponse() {
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, Map<String, Stats>> getDims() {
        return dims;
    }

    public void setDims(Map<String, Map<String, Stats>> dims) {
        this.dims = dims;
    }

}
