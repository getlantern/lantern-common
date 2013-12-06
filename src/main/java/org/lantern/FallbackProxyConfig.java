package org.lantern;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class FallbackProxyConfig {

    private String group;
    private int serial;
    private Collection<FallbackProxy> proxies;

    public FallbackProxyConfig() {}

    public FallbackProxyConfig(String group,
                               int serial,
                               Collection<FallbackProxy> proxies) {
        this.group = group;
        this.serial = serial;
        this.proxies = proxies;
    }

    public String getGroup() {
        return group;
    }

    public int getSerial() {
        return serial;
    }

    public Collection<FallbackProxy> getProxies() {
        return proxies;
    }
}
