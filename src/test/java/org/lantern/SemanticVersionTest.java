package org.lantern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lantern.SemanticVersion;

public class SemanticVersionTest {

    @Test
    public void test() throws Exception {
        SemanticVersion beta7 = new SemanticVersion(1, 0, 0, "beta7");
        SemanticVersion rc1 = new SemanticVersion(1, 0, 0, "rc1");
        SemanticVersion final_ = new SemanticVersion(1, 0, 0, null);
        assertTrue(beta7.compareTo(rc1) < 0);
        assertTrue(rc1.compareTo(final_) < 0);
        assertTrue(beta7.compareTo(final_) < 0);
    }
}
