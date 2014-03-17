package org.lantern.monitoring;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic representation of statistics.
 */
public class Stats {
    public static enum Counters {
        requestsGiven,
        bytesGiven,
        requestsGivenByFallback,
        bytesGivenByFallback,
        requestsGivenByPeer,
        bytesGivenByPeer,
        requestsGotten,
        bytesGotten,
        directBytes,
    };

    public static enum Gauges {
        usingUPnP,
        usingNATPMP,
        bpsGiven,
        bpsGivenByFallback,
        bpsGivenByPeer,
        bpsGotten,
        distinctPeers,
        online,
        processCPUUsage,
        systemCPUUsage,
        systemLoadAverage,
        memoryUsage,
        openFileDescriptors
    };

    public static enum Members {
        everOnline
    }

    private volatile Map<String, Long> counters = new HashMap<String, Long>();
    private volatile Map<String, Long> increments = new HashMap<String, Long>();
    private volatile Map<String, Long> gauges = new HashMap<String, Long>();
    private volatile Map<String, Long> gaugesCurrent = new HashMap<String, Long>();
    private volatile Map<String, String> members = new HashMap<String, String>();

    public Stats() {
    }

    public Map<String, Long> getCounters() {
        return counters;
    }

    public void setCounters(Map<String, Long> counters) {
        this.counters = counters;
    }

    public Map<String, Long> getIncrements() {
        return increments;
    }

    public void setIncrements(Map<String, Long> increments) {
        this.increments = increments;
    }

    public Map<String, Long> getGauges() {
        return gauges;
    }

    public void setGauges(Map<String, Long> gauges) {
        this.gauges = gauges;
    }

    public Map<String, Long> getGaugesCurrent() {
        return gaugesCurrent;
    }

    public void setGaugesCurrent(Map<String, Long> gaugesCurrent) {
        this.gaugesCurrent = gaugesCurrent;
    }

    public Map<String, String> getMembers() {
        return members;
    }

    public void setMembers(Map<String, String> members) {
        this.members = members;
    }

    public void setCounter(Counters name, long value) {
        counters.put(name.toString(), value);
    }

    public void setCounter(Counters name, String country, long value) {
        counters.put(name.toString() + "_" + country, value);
    }

    public long getCounter(Counters name) {
        Long value = counters.get(name.toString());
        return value != null ? value : 0;
    }

    public void setIncrement(Counters name, long value) {
        increments.put(name.toString(), value);
    }

    public void setIncrement(Counters name, String country, long value) {
        increments.put(name.toString() + "_" + country, value);
    }

    public void setGauge(Gauges name, long value) {
        gauges.put(name.toString(), value);
    }

    public long getGauge(Gauges name) {
        Long value = gauges.get(name.toString());
        return value != null ? value : 0;
    }

    public void setMember(Members name, String value) {
        members.put(name.toString(), value);
    }

}
