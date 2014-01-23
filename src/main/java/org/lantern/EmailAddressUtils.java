package org.lantern;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class EmailAddressUtils {

    public static class NormalizationException extends Exception {
        public NormalizationException(String s, Throwable t) {
            super(s, t);
        }
    }

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
    public static String normalizedEmail(String input) throws NormalizationException {
        try {
            input = input.toLowerCase(Locale.ENGLISH);
            int idx = input.indexOf("@");
            String beforeAt = input.substring(0, idx);
            String rest = input.substring(idx);
            beforeAt = beforeAt.replaceAll("\\.", "");
            beforeAt = StringUtils.substringBefore(beforeAt, "+");
            rest = rest.replaceAll("googlemail.com$", "gmail.com");
            return beforeAt + rest;
        } catch (Exception e) {
            throw new EmailAddressUtils.NormalizationException(
                    "Couldn't normalize email: ", e);
        }
    }
}
