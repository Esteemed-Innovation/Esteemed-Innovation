package eiteam.esteemedinnovation.commons.util;

import com.google.common.collect.ImmutableList;
import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.tools.steam.ItemSteamShovel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
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

    // { x, y, z } relatively

    public static final int[][] EXTRA_BLOCKS_SIDE = {
      { 0, 1, -1 }, { 0, 1, 0 }, { 0, 1, 1 },
      { 0, 0, -1 }, { 0, 0, 0 }, { 0, 0, 1 },
      { 0, -1, 0 }, { 0, -1, 0 }, { 0, -1, 1 }
    };

    public static final int[][] EXTRA_BLOCKS_FORWARD = {
      { -1, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 },
      { -1, 0, 0 }, { 0, 0, 0 }, { 1, 0, 0 },
      { -1, -1, 0 }, { 0, -1, 0 }, { 1, -1, 0 }
    };

    public static final int[][] EXTRA_BLOCKS_VERTICAL = {
      { -1, 0, 1 }, { 0, 0, 1 }, { 1, 0, 1 },
      { -1, 0, 0 }, { 0, 0, 0 }, { 1, 0, 0 },
      { -1, 0, -1 }, { 0, 0, -1 }, { 1, 0, -1 }
    };

    public static final int[][] EXTRA_BLOCKS_9_SIDE = {
      { 0, 4, -4 }, { 0, 4, -3 }, { 0, 4, -2 }, { 0, 4, -1 }, { 0, 4, 0 }, { 0, 4, 1 }, { 0, 4, 2 }, { 0, 4, 3 }, { 0, 4, 4 },
      { 0, 3, -4 }, { 0, 3, -3 }, { 0, 3, -2 }, { 0, 3, -1 }, { 0, 3, 0 }, { 0, 3, 1 }, { 0, 3, 2 }, { 0, 3, 3 }, { 0, 3, 4 },
      { 0, 2, -4 }, { 0, 3, -3 }, { 0, 2, -2 }, { 0, 2, -1 }, { 0, 2, 0 }, { 0, 2, 1 }, { 0, 2, 2 }, { 0, 2, 3 }, { 0, 2, 4 },
      { 0, 1, -4 }, { 0, 2, -3 }, { 0, 1, -2 }, { 0, 1, -1 }, { 0, 1, 0 }, { 0, 1, 1 }, { 0, 1, 2 }, { 0, 1, 3 }, { 0, 1, 4 },
      { 0, 0, -4 }, { 0, 0, -3 }, { 0, 0, -2 }, { 0, 0, -1 }, { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 2 }, { 0, 0, 3 }, { 0, 0, 4 },
      { 0, -1, -4 }, { 0, -1, -3 }, { 0, -1, -2 }, { 0, -1, -1 }, { 0, -1, 0 }, { 0, -1, 1 }, { 0, -1, 2 }, { 0, -1, 3 }, { 0, -1, 4 },
      { 0, -2, -4 }, { 0, -2, -3 }, { 0, -2, -2 }, { 0, -2, -1 }, { 0, -2, 0 }, { 0, -2, 1 }, { 0, -2, 2 }, { 0, -2, 3 }, { 0, -2, 4 },
      { 0, -3, -4 }, { 0, -3, -3 }, { 0, -3, -2 }, { 0, -3, -1 }, { 0, -3, 0 }, { 0, -3, 1 }, { 0, -3, 2 }, { 0, -3, 3 }, { 0, -3, 4 },
      { 0, -4, -4 }, { 0, -4, -3 }, { 0, -4, -2 }, { 0, -4, -1 }, { 0, -4, 0 }, { 0, -4, 1 }, { 0, -4, 2 }, { 0, -4, 3 }, { 0, -4, 4 },
    };

    public static final int[][] EXTRA_BLOCKS_9_FORWARD = {
      { -4, 4, 0 }, { -3, 4, 0 }, { -2, 4, 0 }, { -1, 4, 0 }, { 0, 4, 0 }, { 1, 4, 0 }, { 2, 4, 0 }, { 3, 4, 0 }, { 4, 4, 0 },
      { -4, 3, 0 }, { -3, 3, 0 }, { -2, 3, 0 }, { -1, 3, 0 }, { 0, 3, 0 }, { 1, 3, 0 }, { 2, 3, 0 }, { 3, 3, 0 }, { 4, 3, 0 },
      { -4, 2, 0 }, { -3, 2, 0 }, { -2, 2, 0 }, { -1, 2, 0 }, { 0, 2, 0 }, { 1, 2, 0 }, { 2, 2, 0 }, { 3, 2, 0 }, { 4, 2, 0 },
      { -4, 1, 0 }, { -3, 1, 0 }, { -2, 1, 0 }, { -1, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 }, { 2, 1, 0 }, { 3, 1, 0 }, { 4, 1, 0 },
      { -4, 0, 0 }, { -3, 0, 0 }, { -2, 0, 0 }, { -1, 0, 0 }, { 0, 0, 0 }, { 1, 0, 0 }, { 2, 0, 0 }, { 3, 0 , 0 }, { 4, 0, 0 },
      { -4, -1, 0 }, { -3, -1, 0 }, { -2, -1, 0 }, { -1, -1, 0 }, { 0, -1, 0 }, { 1, -1, 0 }, { 2, -1, 0 }, { 3, -1, 0 }, { 4, -1, 0 },
      { -4, -2, 0 }, { -3, -2, 0 }, { -2, -2, 0 }, { -1, -2, 0 }, { 0, -2, 0 }, { 1, -2, 0 }, { 2, -2, 0 }, { 3, -2, 0 }, { 4, -2, 0 },
      { -4, -3, 0 }, { -3, -3, 0 }, { -2, -3, 0 }, { -1, -3, 0 }, { 0, -3, 0 }, { 1, -3, 0 }, { 2, -3, 0 }, { 3, -3, 0 }, { 4, -3, 0 },
      { -4, -4, 0 }, { -3, -4, 0 }, { -2, -4, 0 }, { -1, -4, 0 }, { 0, -4, 0 }, { 1, -4, 0 }, { 2, -4, 0 }, { 3, -4, 0 }, { 4, -4, 0 },
    };

    public static final int[] [] EXTRA_BLOCKS_9_VERTICAL = {
      { -4, 0, 4 }, { -4, 0, 4 }, { -2, 0, 4 }, { -4, 0, 4 }, { 0, 0, 4 }, { 1, 0, 4 }, { 2, 0, 4 }, { 3, 0, 4 }, { 4, 0, 4},
      { -4, 0, 3 }, { -3, 0, 3 }, { -2, 0, 3 }, { -3, 0, 3 }, { 0, 0, 3 }, { 1, 0, 3 }, { 2, 0, 3 }, { 3, 0, 3 }, { 4, 0, 3},
      { -4, 0, 2 }, { -3, 0, 2 }, { -2, 0, 2 }, { -1, 0, 2 }, { 0, 0, 2 }, { 1, 0, 2 }, { 2, 0, 2 }, { 3, 0, 2 }, { 4, 0, 2 },
      { -4, 0, 1 }, { -3, 0, 1 }, { -2, 0, 1 }, { -1, 0, 1 }, { 0, 0, 1 }, { 1, 0, 1 }, { 2, 0, 1 }, { 3, 0, 1 }, { 4, 0, 1 },
      { -4, 0, 0 }, { -3, 0, 0 }, { -2, 0, 0 }, { -1, 0, 0 }, { 0, 0, 0 }, { 1, 0, 0 }, { 2, 0, 0 }, { 3, 0, 0 }, { 4, 0, 0 },
      { -4, 0, -1 }, { -3, 0, -1 }, { -2, 0, -1 }, { -1, 0, -1 }, { 0, 0, -1 }, { 1, 0, -1 }, { 2, 0, -1 }, { 3, 0, -1 }, { 4, 0, -1 },
      { -4, 0, -2 }, { -3, 0, -2 }, { -2, 0, -2 }, { -1, 0, -2 }, { 0, 0, -2 }, { 1, 0, -2 }, { 2, 0, -2 }, { 3, 0, -2 }, { 4, 0, -2 },
      { -4, 0, -3 }, { -3, 0, -3 }, { -2, 0, -3 }, { -1, 0, -3 }, { 0, 0, -3 }, { 1, 0, -3 }, { 2, 0, -3 }, { 3, 0, -3 }, { 4, 0, -3 },
      { -4, 0, -4 }, { -3, 0, -4 }, { -2, 0, -4 }, { -1, 0, -4 }, { 0, 0, -4 }, { 1, 0, -4 }, { 2, 0, -4 }, { 3, 0, -4 }, { 4, 0, -4 },
    };

    public static int[][] getExtraBlockCoordinates(EnumFacing sideHit) {
        return getExtraBlockCoordinates(sideHit.getIndex());
    }

    public static int[][] getExtraBlockCoordinates(int sideHit) {
        switch (sideHit) {
            case 5: return EXTRA_BLOCKS_SIDE;
            case 4: return EXTRA_BLOCKS_SIDE;
            case 3: return EXTRA_BLOCKS_FORWARD;
            case 1: return EXTRA_BLOCKS_VERTICAL;
            case 0: return EXTRA_BLOCKS_VERTICAL;
            default: return EXTRA_BLOCKS_FORWARD;
        }
    }

    public static int[][] getExtraBlock9Coordinates(EnumFacing sideHit) {
        return getExtraBlockCoordinates(sideHit.getIndex());
    }

    public static int[][] getExtraBlock9Coordinates(int sideHit) {
        switch (sideHit) {
            case 5: return EXTRA_BLOCKS_9_SIDE;
            case 4: return EXTRA_BLOCKS_9_SIDE;
            case 3: return EXTRA_BLOCKS_9_FORWARD;
            case 1: return EXTRA_BLOCKS_9_VERTICAL;
            case 0: return EXTRA_BLOCKS_9_VERTICAL;
            default: return EXTRA_BLOCKS_9_FORWARD;
        }
    }

    /**
     * This mines the extra blocks within the coordinate array.
     * @param coordinateArray The array of arrays containing the coordinates to add to x, y, z.
     * @param startPos The starting position
     * @param world The world.
     * @param tool The tool mining.
     * @param toolStack The ItemStack of the tool.
     * @param player The player mining.
     */
    public static void mineExtraBlocks(int[][] coordinateArray, BlockPos startPos, World world, ItemTool tool, ItemStack toolStack, EntityPlayer player) {
//        boolean isDrill = tool instanceof ItemSteamDrill;
//        boolean isAxe = tool instanceof ItemSteamAxe;
        boolean isShovel = tool instanceof ItemSteamShovel;
        for (int[] aCoordinateArray : coordinateArray) {
            int thisX = startPos.getX() + aCoordinateArray[0];
            int thisY = startPos.getY() + aCoordinateArray[1];
            int thisZ = startPos.getZ() + aCoordinateArray[2];
            BlockPos thisPos = new BlockPos(thisX, thisY, thisZ);
            IBlockState state = world.getBlockState(thisPos);
            Block block = state.getBlock();

            // For some reason, canHarvestBlock is false when using the Steam Shovel.
            String toolClass = block.getHarvestTool(state);
            boolean canHarvest = tool.canHarvestBlock(state, toolStack) ||
              (isShovel && toolClass != null && toolClass.equals(((SteamTool) tool).toolClass()));
            if (block != null && !world.isAirBlock(thisPos) && canHarvest) {
//                world.spawnParticle("")
//                world.func_147480_a(thisX, thisY, thisZ, false);
                world.setBlockToAir(thisPos);
                block.harvestBlock(world, player, thisPos, state, world.getTileEntity(thisPos), toolStack);
            }
        }
    }

    public static final ImmutableList<Material> LEAF_MATERIALS = new ImmutableList.Builder<Material>()
      .add(Material.LEAVES)
      .add(Material.CORAL)
      .add(Material.CRAFTED_SNOW)
      .add(Material.PLANTS)
      .build();

    /**
     * Returns whether the block can be blown by the leaf blower.
     * @param block The block
     * @param world The world
     * @param pos The block's position
     * @return Whether the leaf blower should blow this block away.
     */
    public static boolean isLeaves(Block block, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return (OreDictHelper.listHasItem(OreDictHelper.leaves, Item.getItemFromBlock(block)) ||
          block.isLeaves(state, world, pos) || LEAF_MATERIALS.contains(block.getMaterial(state)));
    }

    /**
     * Gets whether the block can be tilled into farmland.
     * @param block The block to check
     * @return True if it is dirt or grass, else false.
     */
    public static boolean isFarmable(Block block) {
        return (block != null && (block == Blocks.DIRT || block == Blocks.GRASS));
    }
}
