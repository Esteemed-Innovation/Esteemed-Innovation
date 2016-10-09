package misc;

import org.junit.Test;

import static eiteam.esteemedinnovation.misc.JavaHelper.capitalize;
import static org.junit.Assert.assertEquals;

public class JavaHelperTest {
    @Test
    public void testCapitalize() {
        assertEquals(capitalize("title"), "Title");
        assertEquals(capitalize("Title"), "Title");
        assertEquals(capitalize("él"), "Él");
        assertEquals(capitalize("Él"), "Él");
    }
}
