package misc;

import org.junit.Test;

import static eiteam.esteemedinnovation.commons.util.JavaHelper.capitalize;
import static eiteam.esteemedinnovation.commons.util.JavaHelper.decapitalize;
import static org.junit.Assert.assertEquals;

public class JavaHelperTest {
    @Test
    public void testCapitalize() {
        assertEquals(capitalize("title"), "Title");
        assertEquals(capitalize("Title"), "Title");
        assertEquals(capitalize("él"), "Él");
        assertEquals(capitalize("Él"), "Él");
    }

    @Test
    public void testDecapitalize() {
        assertEquals(decapitalize("Title"), "title");
        assertEquals(decapitalize("title"), "title");
        assertEquals(decapitalize("TItle"), "tItle");
        assertEquals(decapitalize("Él"), "él");
        assertEquals(decapitalize("él"), "él");
    }
}
