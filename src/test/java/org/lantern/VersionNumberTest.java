package org.lantern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VersionNumberTest {

    @Test
    public void test() throws Exception {
        VersionNumber vn0 = new VersionNumber("1.2.0");
        VersionNumber vn1 = new VersionNumber("1.2");
        VersionNumber vn2 = new VersionNumber("1.2.3");
        VersionNumber vn3 = new VersionNumber("1.2.4");
        VersionNumber vn4 = new VersionNumber("2.2.4");

        assertEquals(1, vn1.getMajor());
        assertEquals(2, vn1.getMinor());
        assertEquals(0, vn1.getPatch());

        assertEquals(vn0, vn1);
        assertEquals(0, vn0.compareTo(vn1));
        assertTrue(vn1.compareTo(vn2) < 0);
        assertTrue(vn2.compareTo(vn1) > 0);
        assertTrue(vn2.compareTo(vn3) < 0);
        assertTrue(vn3.compareTo(vn4) < 0);

    }
}
