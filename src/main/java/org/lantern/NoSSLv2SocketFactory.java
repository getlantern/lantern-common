package org.lantern;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * An {@link SSLSocketFactory} that doesn't allow SSLv2 protocols (in
 * particular, doesn't allow SSLv2Hello, which is the default in Java 6).
 */
public class NoSSLv2SocketFactory extends SSLSocketFactory {
    private final SSLSocketFactory wrapped;

    public NoSSLv2SocketFactory(SSLSocketFactory wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Socket createSocket(InetAddress address, int port,
            InetAddress localAddress, int localPort)
            throws IOException {
        return withLimitedProtocols(wrapped.createSocket(address,
                port, localAddress, localPort));
    }

    @Override
    public Socket createSocket(String host, int port,
            InetAddress localHost,
            int localPort) throws IOException, UnknownHostException {
        return withLimitedProtocols(wrapped.createSocket(host,
                port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port)
            throws IOException {
        return withLimitedProtocols(wrapped
                .createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port)
            throws IOException,
            UnknownHostException {
        return withLimitedProtocols(wrapped
                .createSocket(host, port));
    }

    @Override
    public Socket createSocket(Socket s, String host, int port,
            boolean autoClose) throws IOException {
        return withLimitedProtocols(wrapped.createSocket(s, host,
                port, autoClose));
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return wrapped.getSupportedCipherSuites();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return wrapped.getDefaultCipherSuites();
    }

    private Socket withLimitedProtocols(Socket socket) {
        SSLSocket sslSocket = (SSLSocket) socket;
        // Remove SSLv2
        List<String> protocols = new ArrayList<String>();
        for (String protocol : sslSocket.getSupportedProtocols()) {
            if (!protocol.startsWith("SSLv2")) {
                protocols.add(protocol);
            }
        }
        sslSocket.setEnabledProtocols(protocols
                .toArray(new String[0]));
        return socket;
    }
}
