package org.lantern;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class FallbackProxyConfig {

    private String cookie;
    private Collection<FallbackProxy> proxies;

    public FallbackProxyConfig() {}

    public FallbackProxyConfig(String cookie,
                               Collection<FallbackProxy> proxies) {
        this.cookie = cookie;
        this.proxies = proxies;
    }

    public String getCookie() {
        return cookie;
    }

    public Collection<FallbackProxy> getProxies() {
        return proxies;
    }
}
