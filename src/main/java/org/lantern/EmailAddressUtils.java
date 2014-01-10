package org.lantern;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class EmailAddressUtils {
    /**
     * From https://github.com/getlantern/lantern/issues/1300:
     *
     * "We should have a LanternControllerUtils.normalizeEmail that takes an email (or
     * the userid part of a jabberid) and returns a normalized version with folded
     * case, '.'s and '+'s stripped from the username, and '@googlemail.com' replaced
     * with '@gmail.com'."
     *
     * Domains other than gmail.com and googlemail.com are assumed to be Google Apps for
     * Domains domains and are not changed.
     *
     * If a string that is not of the expected form is passed in, no guarantee is
     * made about the result.
     *
     * @param input The email address to normalize.
     * @return The normalized email address.
     */
    public static String normalizedEmail(String input) throws Exception {
        input = input.toLowerCase(Locale.ENGLISH);
        int idx = input.indexOf("@");
        String beforeAt = input.substring(0, idx);
        String rest = input.substring(idx);
        beforeAt = beforeAt.replaceAll("\\.", "");
        beforeAt = StringUtils.substringBefore(beforeAt, "+");
        rest = rest.replaceAll("googlemail.com$", "gmail.com");
        return beforeAt + rest;
    }
}
