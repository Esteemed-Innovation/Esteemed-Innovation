package eiteam.esteemedinnovation.api.util;

public class StringUtility {
    /**
     * Capitalizes the first letter in the provided String.
     * @param str The string to capitalize.
     * @return The string with the first character capitalized. Examples:
     * capitalize("title") // => "Title"
     * capitalize("Title") // => "Title"
     * capitalize("él") // => "Él"
     */
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + (str.substring(1));
    }

    /**
     * Opposite of {@link StringUtility#capitalize(String)}.
     */
    public static String decapitalize(String str) {
        return str.substring(0, 1).toLowerCase() + (str.substring(1));
    }
}
