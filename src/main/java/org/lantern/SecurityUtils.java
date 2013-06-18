package org.lantern;

public class SecurityUtils {
    /**
     * Compare two strings for equality securely. The first string is the
     * expected result, and the second string is the user input. The comparison
     * takes time proportional to the user input, so that (hopefully) the length
     * of the expected string is not leaked.
     *
     * @param expected
     * @param got
     * @return
     */
    public static boolean constantTimeEquals(String expected, String got) {
        boolean equals = true;
        if (got == null) {
            return false;
        }
        for (int i = 0; i < got.length(); ++i) {
            if (i < expected.length()) {
                equals &= expected.charAt(i) == got.charAt(i);
            } else {
                // this is never true, but hopefully, the compiler
                // won't optimize it away; we want to make the same
                // number of calls to charAt regardless of the length
                // of expected
                equals &= expected.charAt(0) == (-1 | got.charAt(i));
            }
        }
        return equals & expected.length() == got.length();
    }
}
