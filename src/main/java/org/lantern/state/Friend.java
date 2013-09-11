package org.lantern.state;

/**
 * Common interface for friends.
 */
public interface Friend {

    public enum Status {
        friend,
        rejected,
        pending //everything else
    }

    Long getId();

    void setId(Long id);
    
    String getUserEmail();

    void setUserEmail(String userEmail);

    String getEmail();

    void setEmail(String email);

    String getName();

    void setName(String name);
    
    Status getStatus();

    void setStatus(Status status);
    
    void setPendingSubscriptionRequest(boolean pending);
    
    boolean shouldNotifyAgain();

    boolean isPendingSubscriptionRequest();
    
    long getNextQuery();

    void setNextQuery(long nextQuery);

    void setLastUpdated(long lastUpdated);
    
    long getLastUpdated();

}
