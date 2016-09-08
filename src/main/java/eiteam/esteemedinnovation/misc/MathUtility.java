package eiteam.esteemedinnovation.misc;

public class MathUtility {
    /**
     * Similar to Math.min, but never returns a. If a >= b, it returns the default.
     *
     * Example:
     *
     * <code>
     *     MathUtility.minWithDefault(1, 2, 3) // => 3
     *     MathUtility.minWithDefault(3, 2, 1) // => 2
     * </code>
     * @param def The default value. This is in no way restricted to values according to the a and b values.
     */
    public static int minWithDefault(int a, int b, int def) {
        return a < b ? def : b;
    }
}
