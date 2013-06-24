package org.lantern.state;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;


public class Friend implements Serializable {
    private static final long serialVersionUID = 6669786580088595294L;

    private String email;

    private String name = "";

    public enum Status {
        friend,
        rejected,
        pending //everything else
    }

    private Status status = Status.pending;

    /**
     * The last time the status was updated by a user action or request, in
     * milliseconds since epoch
     */
    private long lastUpdated;

    /**
     * The next time, in milliseconds since epoch, that we will ask the user
     * about this friend, assuming status=requested.
     */
    private long nextQuery;

    /**
     * Whether or not an XMPP subscription request from this user is pending.
     */
    private boolean pendingSubscriptionRequest;

    public Friend() {
        lastUpdated = System.currentTimeMillis();
    }

    public Friend(String email) {
        lastUpdated = System.currentTimeMillis();
        this.setEmail(email);
    }

    public Friend(String email, Status status, String name, long nextQuery, long lastUpdated) {
        this.email = email;
        this.status = status;
        this.name = name;
        this.nextQuery = nextQuery;
        this.lastUpdated = lastUpdated;
    }

    public void update(Friend other) {
        this.status = other.status;
        this.name = other.name;
        this.nextQuery = other.nextQuery;
        this.lastUpdated = other.lastUpdated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        if (!StringUtils.equals(name, oldName)) {
            updated();
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        Status oldStatus = this.status;
        this.status = status;
        if (status != oldStatus) {
            updated();
        }
    }

    private void updated() {
        this.setLastUpdated(System.currentTimeMillis());
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(long nextQuery) {
        this.nextQuery = nextQuery;
    }

    public void setPendingSubscriptionRequest(boolean pending) {
        pendingSubscriptionRequest = pending;
    }

    public boolean isPendingSubscriptionRequest() {
        return pendingSubscriptionRequest;
    }

    public boolean shouldNotifyAgain() {
        if (status == Status.pending) {
            long now = System.currentTimeMillis();
            return nextQuery < now;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Friend(" + email + ")";
    }
}
