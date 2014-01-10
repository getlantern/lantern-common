package org.lantern;

import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class EmailAddressUtils {

    private static final String AT_DOMAIN = "@gmail.com";

    @SuppressWarnings("unused")
    private static String lowerCased(String input) {
        return StringUtils.lowerCase(input, Locale.ENGLISH);
    }

    @SuppressWarnings("unused")
    private static String strippedDots(String input) {
        return StringUtils.replace(input, ".", "");
    }

    @SuppressWarnings("unused")
    private static String strippedPlusExtension(String input) {
        return StringUtils.substringBefore(input, "+");
    }

    private static Method[] normalizedEmailTransformations;
    static {
        try {
            normalizedEmailTransformations = new Method[] {
                    EmailAddressUtils.class.getDeclaredMethod("lowerCased", String.class),
                    EmailAddressUtils.class.getDeclaredMethod("strippedDots", String.class),
                    EmailAddressUtils.class.getDeclaredMethod("strippedPlusExtension", String.class),
            };
        }
        catch (NoSuchMethodException e) {
            System.err.println("NoSuchMethodException");
        }
    };

    /**
     * From https://github.com/getlantern/lantern/issues/1300:
     *
     * "We should have a LanternControllerUtils.normalizeEmail that takes an email (or
     * the userid part of a jabberid) and returns a normalized version with folded
     * case, '.'s and '+'s stripped from the username, and '@googlemail.com' replaced
     * with '@gmail.com'."
     *
     * If a string that is not of the expected form is passed in, no guarantee is
     * made about the result.
     *
     * @param input The email address to normalize.
     * @return The normalized email address.
     */
    public static String normalizedEmail(String input) throws Exception {
        String beforeAt = input.substring(0, input.indexOf("@"));
        String transformed = beforeAt;
        for (Method m : normalizedEmailTransformations) {
            transformed = (String) m.invoke(EmailAddressUtils.class, transformed);
        }
        return transformed + AT_DOMAIN;
    }
}
