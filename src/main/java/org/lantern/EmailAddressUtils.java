package org.lantern;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class EmailAddressUtils {

    // Extending RuntimeException so we don't have to catch this explicitly.
    // It's not clear what could trigger this, if anything, nor what to do
    // about it most of the time.  We were rethrowing this as
    // a RuntimeException every time anyway, a real PITA.  If you know of some
    // other sensible way to handle these in a particular situation, you can
    // still catch this.
    public static class NormalizationException extends RuntimeException {
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
