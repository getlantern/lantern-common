package org.lantern;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class EmailAddressUtils {
    /**
     * Given an email (or the userid part of a jabberid), returns a normalized version with
     * the following transformations applied:
     *
     *     - lowercase
     *     - '.' characters removed from the username part
     *     - plus-extensions removed from the username
     *     - domains of 'googlemail.com' replaced with 'gmail.com'. other
     *       domains left as-is.
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
