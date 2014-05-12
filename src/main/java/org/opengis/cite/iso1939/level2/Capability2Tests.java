package org.opengis.cite.iso1939.level2;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 2.
 */
public class Capability2Tests {

    /**
     * Run conformance level 2 tests only if the generated value exceeds the
     * threshold value.
     */
    @BeforeTest
    public void checkThresholdExceeded() {
        double rnd = Math.random();
        Assert.assertTrue(rnd > 0.9,
                "Trigger value does not exceed threshold: " + rnd);
    }

    /**
     * Checks the result of the length function.
     */
    @Test(description = "Implements ATC 2-1")
    public void checkLength() {
        String str = "perihelion";
        Assert.assertEquals(str.length(), 10);
    }

    /**
     * Checks the Unicode code point value of the first character.
     */
    @Test(description = "Implements ATC 2-2")
    public void codePoint() {
        String str = "perihelion";
        Assert.assertEquals(str.codePointAt(0), 100);
    }
}
