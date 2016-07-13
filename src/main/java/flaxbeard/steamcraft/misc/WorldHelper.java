package flaxbeard.steamcraft.misc;

import net.minecraft.util.math.BlockPos;

public class WorldHelper {
    /**
     * @param first BlockPos
     * @param second BlockPos
     * @return Returns whether the two BlockPos have equal X, Y, and Z values.
     */
    public static boolean areBlockPosEqual(BlockPos first, BlockPos second) {
        return first.getX() == second.getX() && first.getY() == second.getY() && first.getZ() == second.getZ();
    }
}
