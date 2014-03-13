package org.lantern.monitoring;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.lantern.HttpURLClient;
import org.lantern.JsonUtils;
import org.lantern.LanternConstants;

/**
 * Base of API for submitting stats to StatsHub.
 */
public class StatshubAPI extends HttpURLClient {
    public StatshubAPI() {
        this(null);
    }

    public StatshubAPI(InetSocketAddress proxyAddress) {
        super(proxyAddress);
    }

    /**
     * Submits stats to statshub.
     * 
     * @param id
     *            the id (instance or user)
     * @param countryCode
     *            (the country code, xx for unknown)
     * @param stats
     *            the stats
     */
    public void postStats(String id, String countryCode, Stats stats)
            throws Exception {
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("countryCode", countryCode);
        request.put("counter", stats.getCounter());
        request.put("gauge", stats.getGauge());

        HttpURLConnection conn = newConn(urlFor(id));
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        OutputStream out = conn.getOutputStream();
        try {
            JsonUtils.OBJECT_MAPPER.writeValue(out, request);
            int code = conn.getResponseCode();
            if (code != 200) {
                // will be logged below
                throw new Exception("Got " + code + " response:\n"
                        + conn.getResponseMessage());
            }
        } finally {
            try {
                out.close();
            } catch (IOException ioe) {
                // ignore
            }
        }
    }

    public StatsResponse getStats(String id) throws Exception {
        HttpURLConnection conn = newConn(urlFor(id));
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        InputStream in = conn.getInputStream();
        try {
            int code = conn.getResponseCode();
            if (code != 200) {
                // will be logged below
                throw new Exception("Got " + code + " response:\n"
                        + conn.getResponseMessage());
            }
            return JsonUtils.decode(in, StatsResponse.class);
        } finally {
            try {
                in.close();
            } catch (IOException ioe) {
                // ignore
            }
        }
    }

    private String urlFor(String instanceId) {
        return LanternConstants.statshubBaseAddress
                + instanceId;
    }

}
