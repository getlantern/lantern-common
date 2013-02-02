package org.lantern;

public class LanternXmppUtils {

    public static String jidToUserId(final String fullId) {
        return fullId.split("/")[0];
    }

    public static String jidToInstanceId(final String fullId) {
        return fullId.split("/")[1];
    }

    public static boolean isLanternHub(final String jabberid) {
        final String userid = jidToUserId(jabberid);
        return LanternConstants.LANTERN_JID.equals(userid);
    }

    public static boolean isLanternJid(final String from) {
        // Here's the format we're looking for: "-lan-"
        if (from.contains("/"+LanternConstants.UNCENSORED_ID)) {
            return true;
        }
        return false;
    }

}
