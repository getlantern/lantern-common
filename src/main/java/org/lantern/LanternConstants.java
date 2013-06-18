package org.lantern;

import java.nio.charset.Charset;

/**
 * Constants for Lantern.
 */
public class LanternConstants {

    public static final int DASHCACHE_MAXAGE = 60 * 5;

    public static final String API_VERSION = "0.0.1";

    public static final String BUILD_TIME = "build_time_tok";

    public static final String UNCENSORED_ID = "-lan-";

    /**
     * We make range requests of the form "bytes=x-y" where
     * y <= x + CHUNK_SIZE
     * in order to chunk and parallelize downloads of large entities. This
     * is especially important for requests to laeproxy since it is subject
     * to GAE's response size limits.
     * Because "bytes=x-y" requests bytes x through y _inclusive_,
     * this actually requests y - x + 1 bytes,
     * i.e. CHUNK_SIZE + 1 bytes
     * when x = 0 and y = CHUNK_SIZE.
     * This currently corresponds to laeproxy's RANGE_REQ_SIZE of 2000000.
     */
    public static final long CHUNK_SIZE = 2000000 - 1;

    public static final String STATS_URL = "http://lanternctrl.appspot.com/stats";

    public static final String VERSION_KEY = "v";

    public static final int LANTERN_LOCALHOST_HTTP_PORT = 8787;

    public static final String USER_NAME = "un";
    public static final String PASSWORD = "pwd";

    public static final String DIRECT_BYTES = "db";
    public static final String BYTES_PROXIED = "bp";

    public static final String REQUESTS_PROXIED = "rp";
    public static final String DIRECT_REQUESTS = "dr";

    public static final String MACHINE_ID = "m";
    public static final String COUNTRY_CODE = "cc";
    public static final String WHITELIST_ADDITIONS = "wa";
    public static final String WHITELIST_REMOVALS = "wr";
    public static final String SERVERS = "s";
    public static final String UPDATE_TIME = "ut";
    public static final String ROSTER = "roster";


    /**
     * The following are keys in the properties files.
     */
    public static final String FORCE_CENSORED = "forceCensored";

    /**
     * The key for the update JSON object.
     */
    public static final String UPDATE_KEY = "uk";

    public static final String UPDATE_VERSION_KEY = "number";

    public static final String UPDATE_URL_KEY = "url";

    public static final String UPDATE_MESSAGE_KEY = "message";

    public static final String UPDATE_RELEASED_KEY = "released";

    public static final String INVITES_KEY = "invites";

    public static final String INVITED_EMAIL = "invem";

    public static final String INVITEE_NAME = "inv_name";

    public static final String INVITER_NAME = "invr_name";

    public static final String INVITER_REFRESH_TOKEN = "invr_refrtok";

    public static final String INVITED = "invd";

    public static final String INVITED_KEY = "invited";

    public static final String FAILED_INVITES_KEY = "failed_invites";

    public static final String INVITE_FAILED_REASON = "reason";

    /**
     * The length of keys in translation property files.
     */
    public static final int I18N_KEY_LENGTH = 40;

    public static final String CONNECT_ON_LAUNCH = "connectOnLaunch";

    public static final String START_AT_LOGIN = "startAtLogin";

    public static final String FRIENDS = "friends";

    public static final String FRIEND = "friend";


    /**
     * Note that we don't include the "X-" for experimental headers here. See:
     * the draft that appears likely to become an RFC at:
     *
     * http://tools.ietf.org/html/draft-ietf-appsawg-xdash
     */
    public static final String LANTERN_VERSION_HTTP_HEADER_NAME =
        "Lantern-Version";

    public static final boolean ON_APP_ENGINE;

    public static final int KSCOPE_ADVERTISEMENT = 0x2111;
    public static final String KSCOPE_ADVERTISEMENT_KEY = "ksak";

    public static final Charset UTF8 = Charset.forName("UTF8");

    static {
        boolean tempAppEngine;
        try {
            Class.forName("org.lantern.LanternControllerUtils");
            tempAppEngine = true;
        } catch (final ClassNotFoundException e) {
            tempAppEngine = false;
        }

        ON_APP_ENGINE = tempAppEngine;
    }
}
