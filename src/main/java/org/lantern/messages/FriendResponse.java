package org.lantern.messages;

import com.google.appengine.repackaged.org.codehaus.jackson.JsonNode;

/**
 * A response from the FriendEndpoint API.
 * 
 * @param <P>
 *            type of payload carried in this response
 */
public class FriendResponse<P> {
    private boolean success;
    private int remainingFriendingQuota;
    private P payload;
    private JsonNode payloadJson;

    public FriendResponse() {
    }

    public FriendResponse(boolean success,
            int remainingFriendingQuota,
            P payload) {
        super();
        this.success = true;
        this.remainingFriendingQuota = remainingFriendingQuota;
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getRemainingFriendingQuota() {
        return remainingFriendingQuota;
    }

    public void setRemainingFriendingQuota(int remainingFriendingQuota) {
        this.remainingFriendingQuota = remainingFriendingQuota;
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payloadJson) {
        this.payloadJson = payloadJson;
    }

    public JsonNode getPayloadJson() {
        return payloadJson;
    }
}
