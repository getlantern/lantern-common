package org.lantern.messages;

import static org.lantern.JsonUtils.*;

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
    private String payloadJson;

    public FriendResponse() {
    }

    public FriendResponse(boolean success,
            int remainingFriendingQuota,
            P payload) {
        super();
        this.success = success;
        this.remainingFriendingQuota = remainingFriendingQuota;
        this.payload = payload;
        try {
            this.payloadJson = OBJECT_MAPPER.writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <P> FriendResponse<P> fromJson(String json,
            Class<P> payloadType) {
        try {
            FriendResponse<P> resp = OBJECT_MAPPER.readValue(json,
                    FriendResponse.class);
            if (resp.payloadJson != null) {
                resp.payload = OBJECT_MAPPER.readValue(resp.payloadJson,
                        payloadType);
            }
            return resp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    public P payload() {
        return payload;
    }

}
