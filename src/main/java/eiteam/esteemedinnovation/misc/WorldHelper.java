package eiteam.esteemedinnovation.misc;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    /**
     * Handles the rotation of a block correctly. If it is already facing the provided direction, then it will flip it.
     * @param property The property to get the current facing direction from and to set the new one in.
     * @param world The world.
     * @param state The block's blockstate.
     * @param pos The position of the block in the world.
     * @param tryDir The direction to rotate to (or its opposite).
     */
    public static void rotateProperly(IProperty<EnumFacing> property, World world, IBlockState state, BlockPos pos, EnumFacing tryDir) {
        EnumFacing currentFacing = state.getValue(property);
        EnumFacing newFacing = currentFacing == tryDir ? tryDir.getOpposite() : tryDir;
        world.setBlockState(pos, state.withProperty(property, newFacing));
    }
}
