package org.lantern;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * Base class for users of HttpURLConnetion that may use a proxy.
 */
public abstract class HttpURLClient {
    private final Proxy proxy;

    protected HttpURLClient() {
        proxy = null;
    }

    protected HttpURLClient(InetSocketAddress proxyAddress) {
        if (proxyAddress != null) {
            this.proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
        } else {
            this.proxy = null;
        }
    }

    protected HttpURLConnection newConn(String url) throws IOException {
        return (HttpURLConnection) new URL(url)
                .openConnection(proxy == null ? Proxy.NO_PROXY : proxy);
    }
}
