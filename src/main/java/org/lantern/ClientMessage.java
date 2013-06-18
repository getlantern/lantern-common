package org.lantern;

import java.util.List;

import org.lantern.state.Friend;

public class ClientMessage {
    enum Type {
        friendSync
    }

    public Type type;

    // for friendSync messages
    public List<Friend> friends;
}
