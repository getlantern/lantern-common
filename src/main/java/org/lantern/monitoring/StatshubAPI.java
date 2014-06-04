package org.lantern.monitoring;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.lantern.HttpURLClient;
import org.lantern.JsonUtils;
import org.lantern.LanternConstants;

/**
 * API for posting and querying stats to/from statshub.
 */
public class StatshubAPI extends HttpURLClient {
    public StatshubAPI() {
        this(null);
    }

    public StatshubAPI(InetSocketAddress proxyAddress) {
        super(proxyAddress);
    }

    /**
     * Submit stats for an instance to statshub.
     * 
     * @param instanceId
     * @param userGuid
     * @param countryCode
     * @param isFallback
     * @param stats
     * @throws Exception
     */
    public void postInstanceStats(
            String instanceId,
            String userGuid,
            String countryCode,
            boolean isFallback,
            Stats stats)
            throws Exception {
        postStats("instance_" + instanceId, userGuid, countryCode, isFallback,
                stats, null);
    }

    /**
     * Submit stats for a user to statshub.
     * 
     * @param userGuid
     * @param countryCode
     * @param stats
     * @throws Exception
     */
    public void postUserStats(
            String userGuid,
            String countryCode,
            Stats stats)
            throws Exception {
        postStats("user_" + userGuid, userGuid, countryCode, false, stats, null);
    }

    /**
     * Submits stats to statshub.
     * 
     * @param id
     *            the stat id (instanceId or userId)
     * @param userGuid
     *            the userId (if applicable)
     * @param countryCode
     *            the countryCode
     * @param addFallbackDim
     *            if true, the id will be added to the dims as "fallback"
     * @param stats
     *            the stats
     * @param dims
     *            additional dimensions to post with the stats
     */
    public void postStats(
            String id,
            String userGuid,
            String countryCode,
            boolean addFallbackDim,
            Stats stats,
            Map<String, String> additionalDims)
            throws Exception {
        Map<String, Object> request = new HashMap<String, Object>();
        Map<String, String> dims = new HashMap<String, String>();
        if (additionalDims != null) {
            dims.putAll(additionalDims);
        }
        dims.put("user", userGuid);
        dims.put("country", countryCode);
        if (addFallbackDim) {
            dims.put("fallback", id);
        }
        request.put("dims", dims);
        request.put("counters", stats.getCounters());
        request.put("increments", stats.getIncrements());
        request.put("gauges", stats.getGauges());
        request.put("members", stats.getMembers());

        HttpURLConnection conn = null;
        OutputStream out = null;
        try {
            String url = urlFor(id);
            conn = newConn(url);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            out = conn.getOutputStream();
            JsonUtils.OBJECT_MAPPER.writeValue(out, request);
            int code = conn.getResponseCode();
            if (code != 200) {
                // will be logged below
                throw new Exception("Got " + code + " response for " + url
                        + ":\n"
                        + conn.getResponseMessage());
            }
        } finally {
            IOUtils.closeQuietly(out);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public StatsResponse getStats(final String dimension) throws IOException {
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            String url = urlFor(dimension + "/");
            conn = newConn(url);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            in = conn.getInputStream();
            int code = conn.getResponseCode();
            if (code != 200) {
                // will be logged below
                throw new IOException("Got " + code + " response for " + url
                        + ":\n"
                        + conn.getResponseMessage());
            }
            return JsonUtils.decode(in, StatsResponse.class);
        } finally {
            IOUtils.closeQuietly(in);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String urlFor(String instanceId) {
        return LanternConstants.statshubBaseAddress
                + instanceId;
    }

}
