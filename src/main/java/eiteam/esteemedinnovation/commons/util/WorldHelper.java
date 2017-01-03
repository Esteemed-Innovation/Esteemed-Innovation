package eiteam.esteemedinnovation.commons.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

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

    /**
     * Overload for getDirectionalBoundingBox using a base AABB instead of a bunch of floats.
     * @param dir The direction the block is facing
     * @param base The base AxisAlignedBB
     * @param allowsUpDown Whether it allows vertical rotation (facing UP or facing DOWN)
     * @return The rotated AABB.
     */
    public static AxisAlignedBB getDirectionalBoundingBox(EnumFacing dir, AxisAlignedBB base, boolean allowsUpDown) {
        return getDirectionalBoundingBox(dir, (float) base.minX, (float) base.minY, (float) base.minZ,
          (float) base.maxX, (float) base.maxY, (float) base.maxZ, allowsUpDown);
    }

    /**
     * Gets an AxisAlignedBB according to the provided direction (rotation).
     * @param dir The direction
     * @param minX Minimum X for the AABB
     * @param minY Minimum Y
     * @param minZ Minimum Z
     * @param maxX Maximum X
     * @param maxY Maximum Y
     * @param maxZ Maximum Z
     * @param allowsUpDown Whether it allows vertical rotation (facing UP and facing DOWN)
     * @return The rotated AABB.
     */
    public static AxisAlignedBB getDirectionalBoundingBox(EnumFacing dir, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, boolean allowsUpDown) {
        switch (dir) {
            case NORTH: {
                return new AxisAlignedBB(1 - maxX, minY, 1 - maxZ, 1 - minX, maxY, 1 - minZ);
            }
            case SOUTH: {
                return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case EAST: {
                return new AxisAlignedBB(minZ, minY, minX, maxZ, maxY, maxX);
            }
            case WEST: {
                return new AxisAlignedBB(1 - maxZ, minY, 1 - maxX, 1 - minZ, maxY, 1 - minX);
            }
            case UP: {
                if (!allowsUpDown) {
                    break;
                }
                return new AxisAlignedBB(minX, minZ, minY, maxX, maxZ, maxY);
            }
            case DOWN: {
                if (!allowsUpDown) {
                    break;
                }
                return new AxisAlignedBB(minX, 1 - minZ, minY, maxX, 1 - maxZ, maxY);
            }
        }
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Thread-safe world mutation-safe version of getTileEntity. This should be used in methods which might be called
     * on alternative threads, namely getActualState and getExtendedState.
     * @param world The world
     * @param pos The position
     * @return The tile entity in the position
     */
    public static TileEntity getTileEntitySafely(IBlockAccess world, BlockPos pos) {
        return world instanceof ChunkCache ? ((ChunkCache) world).func_190300_a(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
    }
}
