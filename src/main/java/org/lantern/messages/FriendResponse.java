package org.lantern.messages;

import org.lantern.data.FriendingQuota;

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

    public FriendResponse() {
    }

    private FriendResponse(boolean success,
            FriendingQuota quota,
            P payload) {
        super();
        this.success = true;
        this.remainingFriendingQuota = quota.getRemainingQuota();
        this.payload = payload;
    }
    
    public FriendResponse(FriendingQuota quota) {
        
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

    public void setPayload(P payload) {
        this.payload = payload;
    }
}
