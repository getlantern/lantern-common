package org.lantern;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.lantern.proxy.FallbackProxy;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseS3Config {

    public static final String DEFAULT_CONTROLLER_ID = "lanternctrl1-2";
    
    public static final String DEFAULT_FLASHLIGHT_CLOUDCONFIG = "https://s3.amazonaws.com/lantern_config/cloud.yaml.1.5.8";
    public static final String DEFAULT_FLASHLIGHT_CLOUDCONFIG_CA = "-----BEGIN CERTIFICATE-----\nMIIExjCCBC+gAwIBAgIQNZcxh/OHOgcyfs5YDJt+2jANBgkqhkiG9w0BAQUFADBf\nMQswCQYDVQQGEwJVUzEXMBUGA1UEChMOVmVyaVNpZ24sIEluYy4xNzA1BgNVBAsT\nLkNsYXNzIDMgUHVibGljIFByaW1hcnkgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkw\nHhcNMDYxMTA4MDAwMDAwWhcNMjExMTA3MjM1OTU5WjCByjELMAkGA1UEBhMCVVMx\nFzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMR8wHQYDVQQLExZWZXJpU2lnbiBUcnVz\ndCBOZXR3b3JrMTowOAYDVQQLEzEoYykgMjAwNiBWZXJpU2lnbiwgSW5jLiAtIEZv\nciBhdXRob3JpemVkIHVzZSBvbmx5MUUwQwYDVQQDEzxWZXJpU2lnbiBDbGFzcyAz\nIFB1YmxpYyBQcmltYXJ5IENlcnRpZmljYXRpb24gQXV0aG9yaXR5IC0gRzUwggEi\nMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCvJAgIKXo1nmAMqudLO07cfLw8\nRRy7K+D+KQL5VwijZIUVJ/XxrcgxiV0i6CqqpkKzj/i5Vbext0uz/o9+B1fs70Pb\nZmIVYc9gDaTY3vjgw2IIPVQT60nKWVSFJuUrjxuf6/WhkcIzSdhDY2pSS9KP6HBR\nTdGJaXvHcPaz3BJ023tdS1bTlr8Vd6Gw9KIl8q8ckmcY5fQGBO+QueQA5N06tRn/\nArr0PO7gi+s3i+z016zy9vA9r911kTMZHRxAy3QkGSGT2RT+rCpSx4/VBEnkjWNH\niDxpg8v+R70rfk/Fla4OndTRQ8Bnc+MUCH7lP59zuDMKz10/NIeWiu5T6CUVAgMB\nAAGjggGRMIIBjTAPBgNVHRMBAf8EBTADAQH/MDEGA1UdHwQqMCgwJqAkoCKGIGh0\ndHA6Ly9jcmwudmVyaXNpZ24uY29tL3BjYTMuY3JsMA4GA1UdDwEB/wQEAwIBBjA9\nBgNVHSAENjA0MDIGBFUdIAAwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cudmVy\naXNpZ24uY29tL2NwczAdBgNVHQ4EFgQUf9Nlp8Ld7LvwMAnzQzn6Aq8zMTMwNAYD\nVR0lBC0wKwYJYIZIAYb4QgQBBgpghkgBhvhFAQgBBggrBgEFBQcDAQYIKwYBBQUH\nAwIwbQYIKwYBBQUHAQwEYTBfoV2gWzBZMFcwVRYJaW1hZ2UvZ2lmMCEwHzAHBgUr\nDgMCGgQUj+XTGoasjY5rw8+AatRIGCx7GS4wJRYjaHR0cDovL2xvZ28udmVyaXNp\nZ24uY29tL3ZzbG9nby5naWYwNAYIKwYBBQUHAQEEKDAmMCQGCCsGAQUFBzABhhho\ndHRwOi8vb2NzcC52ZXJpc2lnbi5jb20wDQYJKoZIhvcNAQEFBQADgYEADyWuSO0b\nM4VMDLXC1/5N1oMoTEFlYAALd0hxgv5/21oOIMzS6ke8ZEJhRDR0MIGBJopK90Rd\nfjSAqLiD4gnXbSPdie0oCL1jWhFXCMSe2uJoKK/dUDzsgiHYAMJVRFBwQa2DF3m6\nCPMr3u00HUSe0gST9MsFFy0JLS1j7/YmC3s=\n-----END CERTIFICATE-----";
    public static final String DEFAULT_WADDELL_ADDR = "128.199.130.61:443";
    public static final String DEFAULT_FLASHLIGHT_CONFIG_ADDR = "localhost:23000";
    
    public static final String[] DEFAULT_MASQUERADE_HOSTS = new String[] {
            "elance.com",
            "ojooo.com",
            "news.ycombinator.com",
    };
    
    public static final Map<String, String> DEFAULT_HOSTS_TO_CERTS = 
            new HashMap<String, String>();

    private String controller = DEFAULT_CONTROLLER_ID;
    private int minpoll = 5;
    private int maxpoll = 15;
    private Collection<FallbackProxy> fallbacks = Collections.emptyList();

    /**
     * Milliseconds to wait before retrying disconnected signaling connections.
     */
    private long signalingRetryTime = 6000;

    /**
     * Get stats every minute.
     */
    private int statsGetInterval = 60;

    /**
     * Wait a bit before first posting stats, to give the system a chance to
     * initialize metadata.
     */
    private int statsPostInterval = 5 * 60;
    
    private String[] masqueradeHosts = DEFAULT_MASQUERADE_HOSTS;

    private String dnsRegUrl = "cloudflare-peerdnsreg.herokuapp.com";

    /**
     * Note that DEFAULT_HOSTS_TO_CERTS is populated in the constructor of 
     * subclasses.
     */
    private Map<String, String> masqueradeHostsToCerts = DEFAULT_HOSTS_TO_CERTS;
    
    private String flashlightCloudConfig = DEFAULT_FLASHLIGHT_CLOUDCONFIG;
    private String flashlightCloudConfigCA = DEFAULT_FLASHLIGHT_CLOUDCONFIG_CA;
    private String waddellAddr = DEFAULT_WADDELL_ADDR;
    private String flashlightConfigAddr = DEFAULT_FLASHLIGHT_CONFIG_ADDR;

    public BaseS3Config() {
    }

    public String getController() {
        return controller;
    }

    public int getMinpoll() {
        return minpoll;
    }

    public int getMaxpoll() {
        return maxpoll;
    }

    public Collection<FallbackProxy> getFallbacks() {
        return fallbacks;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public void setMinpoll(int minpoll) {
        this.minpoll = minpoll;
    }

    public void setMaxpoll(int maxpoll) {
        this.maxpoll = maxpoll;
    }

    public void setFallbacks(Collection<FallbackProxy> fallbacks) {
        this.fallbacks = fallbacks;
    }

    public int getStatsGetInterval() {
        return statsGetInterval;
    }

    public void setStatsGetInterval(int statsGetInterval) {
        this.statsGetInterval = statsGetInterval;
    }

    public int getStatsPostInterval() {
        return statsPostInterval;
    }

    public void setStatsPostInterval(int statsPostInterval) {
        this.statsPostInterval = statsPostInterval;
    }

    public long getSignalingRetryTime() {
        return signalingRetryTime;
    }

    public void setSignalingRetryTime(long signalingRetryTime) {
        this.signalingRetryTime = signalingRetryTime;
    }

    public Map<String, String> getMasqueradeHostsToCerts() {
        return this.masqueradeHostsToCerts;
    }

    public void setMasqueradeHostsToCerts(Map<String, String> masqueradeHostsToCerts) {
        this.masqueradeHostsToCerts = masqueradeHostsToCerts;
    }

    public String getDnsRegUrl() {
        return dnsRegUrl;
    }

    public void setDnsRegUrl(String dnsRegUrl) {
        this.dnsRegUrl = dnsRegUrl;
    }

    public String[] getMasqueradeHosts() {
        return masqueradeHosts;
    }

    public void setMasqueradeHosts(String[] masqueradeHosts) {
        this.masqueradeHosts = masqueradeHosts;
    }
    
    public String getFlashlightCloudConfig() {
        return flashlightCloudConfig;
    }
    
    public void setFlashlightCloudConfig(String flashlightCloudConfig) {
        this.flashlightCloudConfig = flashlightCloudConfig;
    }
    
    public String getFlashlightCloudConfigCA() {
        return flashlightCloudConfigCA;
    }
    
    public void setFlashlightCloudConfigCA(String flashlightCloudConfigCA) {
        this.flashlightCloudConfigCA = flashlightCloudConfigCA;
    }
    
    public String getWaddellAddr() {
        return waddellAddr;
    }
    
    public void setWaddellAddr(String waddellAddr) {
        this.waddellAddr = waddellAddr;
    }
    
    public String getFlashlightConfigAddr() {
        return flashlightConfigAddr;
    }
    
    public void setFlashlightConfigAddr(String flashlightConfigAddr) {
        this.flashlightConfigAddr = flashlightConfigAddr;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((controller == null) ? 0 : controller.hashCode());
        result = prime * result
                + ((dnsRegUrl == null) ? 0 : dnsRegUrl.hashCode());
        result = prime * result
                + ((fallbacks == null) ? 0 : fallbacks.hashCode());
        result = prime
                * result
                + ((masqueradeHostsToCerts == null) ? 0
                        : masqueradeHostsToCerts.hashCode());
        result = prime * result + maxpoll;
        result = prime * result + minpoll;
        result = prime * result
                + (int) (signalingRetryTime ^ (signalingRetryTime >>> 32));
        result = prime * result + statsGetInterval;
        result = prime * result + statsPostInterval;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseS3Config other = (BaseS3Config) obj;
        if (controller == null) {
            if (other.controller != null)
                return false;
        } else if (!controller.equals(other.controller))
            return false;
        if (dnsRegUrl == null) {
            if (other.dnsRegUrl != null)
                return false;
        } else if (!dnsRegUrl.equals(other.dnsRegUrl))
            return false;
        if (fallbacks == null) {
            if (other.fallbacks != null)
                return false;
        } else if (!fallbacks.equals(other.fallbacks))
            return false;
        if (masqueradeHostsToCerts == null) {
            if (other.masqueradeHostsToCerts != null)
                return false;
        } else if (!masqueradeHostsToCerts.equals(other.masqueradeHostsToCerts))
            return false;
        if (maxpoll != other.maxpoll)
            return false;
        if (minpoll != other.minpoll)
            return false;
        if (signalingRetryTime != other.signalingRetryTime)
            return false;
        if (statsGetInterval != other.statsGetInterval)
            return false;
        if (statsPostInterval != other.statsPostInterval)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BaseS3Config [controller=" + controller + ", minpoll="
                + minpoll + ", maxpoll=" + maxpoll + ", fallbacks=" + fallbacks
                + ", signalingRetryTime=" + signalingRetryTime
                + ", statsGetInterval=" + statsGetInterval
                + ", statsPostInterval=" + statsPostInterval + ", dnsRegUrl="
                + dnsRegUrl + ", masqueradeHostsToCerts="
                + masqueradeHostsToCerts + "]";
    }
}
