package eiteam.esteemedinnovation.misc;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class WorldHelper {
    /**
     * @param first BlockPos
     * @param second BlockPos
     * @return Returns whether the two BlockPos have equal X, Y, and Z values.
     */
    public static boolean areBlockPosEqual(BlockPos first, BlockPos second) {
        return first.getX() == second.getX() && first.getY() == second.getY() && first.getZ() == second.getZ();
    }

    /**
     * @param random Random object
     * @return A random value of North, South, East, or West. Like {@link EnumFacing#random(Random)} but does not include
     *         up and down.
     */
    public static EnumFacing randomHorizontal(Random random) {
        return EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)];
    }
}
