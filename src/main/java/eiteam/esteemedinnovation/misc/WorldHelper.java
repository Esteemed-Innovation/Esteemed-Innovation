package eiteam.esteemedinnovation.misc;

import net.minecraft.util.EnumFacing;

import java.util.Random;

public class WorldHelper {
    /**
     * @param random Random object
     * @return A random value of North, South, East, or West. Like {@link EnumFacing#random(Random)} but does not include
     *         up and down.
     */
    public static EnumFacing randomHorizontal(Random random) {
        return EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)];
    }
}
