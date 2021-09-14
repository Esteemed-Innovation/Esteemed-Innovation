package eiteam.esteemedinnovation.api.util;

import java.util.Locale;

public class StringUtility {
    /**
     * Capitalizes the first letter in the provided String.
     * @param str The string to capitalize.
     * @return The string with the first character capitalized. Examples:
     * <pre>
     *     capitalize("title"); // {@literal =>} "Title"
     *     capitalize("Title"); // {@literal =>} "Title"
     *     capitalize("él"); // {@literal =>} "Él"
     * </pre>
     */
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase(Locale.US) + (str.substring(1));
    }

    /**
     * Opposite of {@link StringUtility#capitalize(String)}.
     */
    public static String decapitalize(String str) {
        return str.substring(0, 1).toLowerCase(Locale.US) + (str.substring(1));
    }
}
