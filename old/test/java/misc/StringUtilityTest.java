package misc;

import org.junit.Test;

import static eiteam.esteemedinnovation.api.util.StringUtility.capitalize;
import static eiteam.esteemedinnovation.api.util.StringUtility.decapitalize;
import static org.junit.Assert.assertEquals;

public class StringUtilityTest {
    @Test
    public void testCapitalize() {
        assertEquals(capitalize("title"), "Title");
        assertEquals(capitalize("Title"), "Title");
//        assertEquals(capitalize("él"), "Él");
//        assertEquals(capitalize("Él"), "Él");
    }

    @Test
    public void testDecapitalize() {
        assertEquals(decapitalize("Title"), "title");
        assertEquals(decapitalize("title"), "title");
        assertEquals(decapitalize("TItle"), "tItle");
//        assertEquals(decapitalize("Él"), "él");
//        assertEquals(decapitalize("él"), "él");
    }
}
