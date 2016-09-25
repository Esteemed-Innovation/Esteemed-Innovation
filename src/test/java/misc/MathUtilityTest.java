package misc;

import org.junit.Test;

import static eiteam.esteemedinnovation.misc.MathUtility.*;
import static org.junit.Assert.*;

public class MathUtilityTest {
    @Test
    public void testMinWithDefault() {
        assertEquals(11, minWithDefault(19, 11, 9));
        assertEquals(19, minWithDefault(19, 19, 9));
        assertEquals(9, minWithDefault(19, 20, 9));
        assertEquals(8, minWithDefault(19, 8, 9));
        assertEquals(3, minWithDefault(1, 2, 3));
        assertEquals(2, minWithDefault(3, 2, 1));
    }

    @Test
    public void testRanges() {
        assertFalse(isBetween(0, 0, 10));
        assertFalse(isBetween(0, 10, 10));
        assertTrue(isBetween(0, 5, 10));
        assertFalse(isBetween(0, 15, 10));

        assertTrue(isBetweenBothInclusive(0, 0, 10));
        assertTrue(isBetweenBothInclusive(0, 10, 10));
        assertTrue(isBetweenBothInclusive(0, 5, 10));
        assertFalse(isBetweenBothInclusive(0, 15, 10));

        assertFalse(isBetweenMaxInclusive(0, 0, 10));
        assertTrue(isBetweenMaxInclusive(0, 10, 10));
        assertTrue(isBetweenMaxInclusive(0, 5, 10));
        assertFalse(isBetweenMaxInclusive(0, 15, 10));

        assertTrue(isBetweenMinInclusive(0, 0, 10));
        assertFalse(isBetweenMinInclusive(0, 10, 10));
        assertTrue(isBetweenMinInclusive(0, 5, 10));
        assertFalse(isBetweenMinInclusive(0, 15, 10));
    }
}
