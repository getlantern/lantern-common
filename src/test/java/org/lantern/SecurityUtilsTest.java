package org.lantern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SecurityUtilsTest {

    @Test
    public void testConstantTimeEquals() {
        // we can't test timing, because JIT (or merely other programs running
        // at the same time) can cause variance.  But we can test for correct
        // results of the equality comparison.

        assertTrue(SecurityUtils.constantTimeEquals("example", "example"));
        assertFalse(SecurityUtils.constantTimeEquals("example", "ex"));
        assertFalse(SecurityUtils.constantTimeEquals("ex", "example"));

        //these \uffff things are to test out an implementation detail of the method
        assertTrue(SecurityUtils.constantTimeEquals("\uffff", "\uffff"));
        assertFalse(SecurityUtils.constantTimeEquals("a\uffff", "a"));
        assertFalse(SecurityUtils.constantTimeEquals("a\uffff", "ab"));
        assertFalse(SecurityUtils.constantTimeEquals("ab", "a\uffff"));
        assertFalse(SecurityUtils.constantTimeEquals("example one", "example two"));

    }

}