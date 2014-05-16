package org.lantern.aws;

import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.littleshoot.util.Base64;

/**
 * <p>
 * Signs a GET URL request to Amazon S3.
 * </p>
 * 
 * <p>
 * See http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html#
 * RESTAuthenticationQueryStringAuth
 * </p>
 * 
 * <p>
 * See
 * http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/
 * AuthJavaSampleHMACSignature.html
 * </p>
 */
public class SignedURL {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private final String accessKeyId;
    private final String secretAccessKey;
    private final String baseUrl;
    private final String resource;
    private String queryString;
    private final int expirationInSecondsSinceEpoch;

    public SignedURL(String accessKeyId, String secretAccessKey,
            String baseUrl, String resource, int expirationInSecondsSinceEpoch) {
        super();
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.baseUrl = baseUrl;
        this.resource = resource;
        this.expirationInSecondsSinceEpoch = expirationInSecondsSinceEpoch;
    }

    public SignedURL withQueryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    public String signed()
            throws Exception {
        boolean hasQueryString = queryString != null
                && queryString.length() > 0;

        StringBuilder stringToSignBuilder = new StringBuilder();
        stringToSignBuilder.append("GET");
        stringToSignBuilder.append("\n");
        stringToSignBuilder.append("\n");
        stringToSignBuilder.append("\n");
        stringToSignBuilder.append(expirationInSecondsSinceEpoch);
        stringToSignBuilder.append("\n");
        stringToSignBuilder.append(resource);
        if (hasQueryString) {
            stringToSignBuilder.append("?");
            stringToSignBuilder.append(queryString);
        }
        String stringToSign = stringToSignBuilder.toString();

        // get an hmac_sha1 key from the raw key bytes
        SecretKeySpec signingKey = new SecretKeySpec(
                secretAccessKey.getBytes(), HMAC_SHA1_ALGORITHM);

        // get an hmac_sha1 Mac instance and initialize with the signing key
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);

        // compute the hmac on input data bytes
        byte[] rawHmac = mac.doFinal(stringToSign.getBytes());

        // base64-encode the hmac
        String signature = Base64.encodeBytes(rawHmac);

        StringBuilder result = new StringBuilder();
        result.append(baseUrl);
        result.append(resource);
        result.append("?");
        if (hasQueryString) {
            result.append(queryString);
            result.append("&");
        }
        result.append("AWSAccessKeyId=");
        result.append(URLEncoder.encode(accessKeyId, "UTF-8"));
        result.append("&Signature=");
        result.append(URLEncoder.encode(signature, "UTF-8"));
        result.append("&Expires=");
        result.append(expirationInSecondsSinceEpoch);
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        String accessKeyId = args[0];
        String secretAccessKey = args[1];
        String baseUrl = " https://s3.amazonaws.com";
        String resource = "/oxtoacart/Lantern.dmg";
        long expiresAt = System.currentTimeMillis() + 10 * 365 * 24 * 60 * 60
                * 1000;
        int expirationInSecondsSinceEpoch = Math.round(expiresAt / 1000);
        String queryString = "response-content-disposition=attachment; filename=lantern2632534543.exe";

        SignedURL url = new SignedURL(accessKeyId, secretAccessKey, baseUrl,
                resource, expirationInSecondsSinceEpoch);
        System.out.println("Without Querystring: " + url.signed());
        System.out.println("With Querystring: "
                + url.withQueryString(queryString).signed());
    }
}
