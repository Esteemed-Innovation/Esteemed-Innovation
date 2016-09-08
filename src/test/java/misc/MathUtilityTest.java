package misc;

import org.junit.Test;

import static eiteam.esteemedinnovation.misc.MathUtility.minWithDefault;
import static org.junit.Assert.assertEquals;

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
}
