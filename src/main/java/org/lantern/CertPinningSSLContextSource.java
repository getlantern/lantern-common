package org.lantern;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.lantern.HttpURLClient.SSLContextSource;

import com.google.common.base.Charsets;

public class CertPinningSSLContextSource implements SSLContextSource {
    private final SSLContext context;

    public CertPinningSSLContextSource(String alias, String pemCert) {
        try {
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(null, null);
            final CertificateFactory cf = CertificateFactory
                    .getInstance("X.509");
            addCert(ts, cf, alias, pemCert);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SSLContext getContext(String url) {
        return context;
    }

    private static void addCert(final KeyStore ks, final CertificateFactory cf,
            final String alias, final String pemCert)
            throws CertificateException, KeyStoreException {
        final InputStream bis =
                new ByteArrayInputStream(pemCert.getBytes(Charsets.UTF_8));
        final Certificate cert = cf.generateCertificate(bis);
        ks.setCertificateEntry(alias, cert);
    }
}
