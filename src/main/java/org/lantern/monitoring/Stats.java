package org.lantern.monitoring;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic representation of statistics.
 */
public class Stats {
    public static enum CounterKey {
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

    public static enum GaugeKey {
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

    private volatile Map<String, Long> counter = new HashMap<String, Long>();
    private volatile Map<String, Long> increment = new HashMap<String, Long>();
    private volatile Map<String, Long> gauge = new HashMap<String, Long>();

    public Stats() {
    }

    public Map<String, Long> getCounter() {
        return counter;
    }

    public void setCounter(Map<String, Long> counter) {
        this.counter = counter;
    }

    public Map<String, Long> getIncrement() {
        return increment;
    }

    public void setIncrement(Map<String, Long> increment) {
        this.increment = increment;
    }

    public Map<String, Long> getGauge() {
        return gauge;
    }

    public void setGauge(Map<String, Long> gauge) {
        this.gauge = gauge;
    }

    public void setCounter(CounterKey name, long value) {
        counter.put(name.toString(), value);
    }

    public void setCounter(CounterKey name, String country, long value) {
        counter.put(name.toString() + "_" + country, value);
    }

    public void setIncrement(CounterKey name, long value) {
        increment.put(name.toString(), value);
    }

    public void setIncrement(CounterKey name, String country, long value) {
        increment.put(name.toString() + "_" + country, value);
    }

    public void setGauge(GaugeKey name, long value) {
        gauge.put(name.toString(), value);
    }

}
