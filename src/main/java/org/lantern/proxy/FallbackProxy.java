package org.lantern.proxy;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.lantern.BaseS3Config;
import org.lantern.state.PeerType;
import org.littleshoot.util.FiveTuple.Protocol;

/**
 * Provided for backwards-compatibility with deserializing the json format from
 * {@link BaseS3Config}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FallbackProxy extends ProxyInfo {
    
    private static final URI JID;

    // We hard code the type here because the JSON in the S3 config file doesn't
    // include anything about the type. Fallbacks are always "cloud".
    private static final PeerType TYPE = PeerType.cloud;
    
    /**
    * The default relative weight for fallbacks.
    */
    private static final int WEIGHT = 1000;
    
    static {
        try {
            JID = new URI("fallback@getlantern.org");
        } catch (URISyntaxException use) {
            throw new RuntimeException(use);
        }
    }

    public FallbackProxy() {
        this(JID, 0, null, null, 0, WEIGHT);
    }

    public FallbackProxy(URI jid, int wanPort, Protocol protocol, Properties pt, 
            int priority, int weight) {
        this(jid, TYPE, null, wanPort, null, 0, null, false, protocol, null, null, pt, priority, weight);
    }
    
    public FallbackProxy(URI jid, PeerType type, String wanHost, int wanPort,
            String lanHost, int lanPort, InetSocketAddress boundFrom,
            boolean useLanAddress, Protocol protocol, String authToken,
            String cert, Properties pt, int priority, int weight) {
        super(jid, type, wanHost, wanPort, lanHost, lanPort, boundFrom,
                useLanAddress,
                protocol, authToken, cert, pt, priority, weight);
    }

    public void setIp(String ip) {
        this.wanHost = ip;
    }

    public void setPort(int port) {
        this.wanPort = port;
    }

    public void setAuth_token(String auth_token) {
        this.authToken = auth_token;
    }

    public void setProtocol(String protocol) {
        this.protocol = Protocol.valueOf(protocol.toUpperCase());
    }
}
