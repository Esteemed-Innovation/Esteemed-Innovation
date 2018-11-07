package eiteam.esteemedinnovation.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;

public class FluidHelper {
    private static Fluid water = FluidRegistry.WATER;

    /**
     * If this mod is used with other mods that use different types of water (fresh water, salt water etc),
     * this can change which fluid EI considers water thus increasing compatibility.
     */
    public static void changeWaterFluid(Fluid newWater) {
        water = newWater;
    }

    public static Fluid getWaterFluid() {
        return water;
    }

    /**
     * Check if the player is holding a water container.
     * @param player The player.
     * @return Whether the player is holding a container and it has water in it.
     */
    public static boolean playerIsHoldingWaterContainer(EntityPlayer player) {
        ItemStack heldItem = ItemStackUtility.getHeldItemStack(player);
        return itemStackIsWaterContainer(heldItem);
    }

    /**
     * Check if the ItemStack is a water container.
     * @param itemStack The ItemStack.
     * @return Whether the ItemStack is a container and has water in it.
     */
    public static boolean itemStackIsWaterContainer(ItemStack itemStack) {
        FluidStack fluid = getFluidFromItemStack(itemStack);

        return !itemStack.isEmpty() && fluid != null && fluid.getFluid() == water;
    }

    /**
     * Gets the FluidStack inside the ItemStack container.
     * @param itemStack The ItemStack.
     * @return The FluidStack in the ItemStack fluid container.
     */
    private static FluidStack getFluidFromItemStack(ItemStack itemStack) {
        if (itemStack.isEmpty() || !itemStack.hasCapability(FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            return null;
        }

        IFluidHandler handler = itemStack.getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null);
        for (IFluidTankProperties prop : handler.getTankProperties()) {
            FluidStack fluid = prop.getContents();
            if (fluid != null) {
                return fluid;
            }
        }

        return null;
    }

    /**
     * Returns the Fluid for the given blockstate. Special handling for vanilla fluids (wooo {@literal >.>})
     * @param state The blockstate
     * @return The fluid. If the blockstate's material is WATER or LAVA, returns the according fluid from FluidRegistry.
     *         Can be null.
     */
    public static Fluid getFluidFromBlockState(IBlockState state) {
        Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());
        if (fluid != null) {
            return fluid;
        } else if (state.getMaterial() == Material.WATER) {
            return FluidRegistry.WATER;
        } else if (state.getMaterial() == Material.LAVA) {
            return FluidRegistry.LAVA;
        }

        return null;
    }

    /**
     * Fills the IFluidTank with an ItemStack fluid container.
     * @param container The ItemStack holding the fluid.
     * @param tank The tank to fill.
     * @param drainContainer Whether to actually drain the `container` ItemStack.
     * @return The modified ItemStack, which probably has no fluid in it anymore.
     */
    public static ItemStack fillTankFromItem(ItemStack container, IFluidTank tank, boolean drainContainer) {
        if (container.isEmpty() || !container.hasCapability(FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            return container;
        }

        IFluidHandlerItem handler = container.getCapability(FLUID_HANDLER_ITEM_CAPABILITY, null);
        int roomLeftInContainer = getRoomLeftInTank(tank);

        if (roomLeftInContainer > 0) {
            FluidStack drained = handler.drain(roomLeftInContainer, drainContainer);
            tank.fill(drained, true);
        }

        return handler.getContainer();
    }

    /**
     * Gets how much room there is in the tank for more fluids.
     * @param tank The tank to check.
     * @return How much space there is in the tank for more fluid.
     */
    private static int getRoomLeftInTank(IFluidTank tank) {
        return tank.getCapacity() - tank.getFluidAmount();
    }

    /**
     * Gets the still texture for the fluid
     * @param mc Minecraft instance
     * @param fluid Fluid to get the texture of
     * @return the texture
     */
    public static TextureAtlasSprite getStillTexture(Minecraft mc, Fluid fluid) {
        return mc.getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());
    }

    /**
     * Returns the "level" value for the block that is considered the still fluid block.
     * @param block The block
     * @return If the block is a BlockFluidBase (Forge fluid), the max render height meta, otherwise 0.
     */
    public static int getStillFluidLevel(Block block) {
        return block instanceof BlockFluidBase ? ((BlockFluidBase) block).getMaxRenderHeightMeta() : 0;
    }

    /**
     * Gets the current fluid level for the block. Handles Forge and Minecraft fluids differently {@literal >.>}
     * @param world The world
     * @param pos The pos
     * @return The current level
     */
    public static int getFluidLevel(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        // Thank you Minecraft and Minecraft Forge...
        if (block instanceof BlockFluidBase) {
            return state.getValue(BlockFluidBase.LEVEL);
        } else if (block instanceof BlockLiquid) {
            return state.getValue(BlockLiquid.LEVEL);
        }
        return 0;
    }

    /**
     * Checks whether the block is an infinite source block of water.
     * @param world The world
     * @param pos The block that is being tested against
     * @param fluid The fluid in the block
     * @return Whether the given pos can be treated as an infinite source of water. For example, if the water is "XYZ",
     *         only "Y" will return true.
     */
    public static boolean isInfiniteWaterSource(World world, BlockPos pos, Fluid fluid) {
        if (fluid != FluidRegistry.WATER) {
            return false;
        }

        List<BlockPos> adjacent = new ArrayList<>();
        adjacent.addAll(Arrays.asList(pos.north(), pos.south(), pos.east(), pos.west()));

        int sourceBlocks = 0;

        for (BlockPos blockToCheck : adjacent) {
            IBlockState state = world.getBlockState(blockToCheck);
            Block block = state.getBlock();
            if (getFluidFromBlockState(state) == FluidRegistry.WATER &&
              getFluidLevel(world, blockToCheck) == getStillFluidLevel(block)) {
                sourceBlocks++;
            }
        }

        return sourceBlocks >= 2;
    }

    /**
     * Recursively scans the blocks around the given starting block to check if they are fluids.
     * @param world The world
     * @param start The block to start scanning at. It will check all directions adjacent to it except down.
     * @param fluid The fluid in the block
     * @param alreadyChecked The list of blocks that have already been checked.
     * @return The BlockPos that is a source block. If none is found, returns null.
     */
    public static BlockPos findSourceBlockPos(World world, BlockPos start, Fluid fluid, Set<BlockPos> alreadyChecked) {
        if (fluid.getBlock() instanceof BlockFluidFinite || world.getBlockState(start).getValue(BlockLiquid.LEVEL) == getStillFluidLevel(fluid.getBlock())) {
            return start;
        }

        List<BlockPos> blocksToCheck = new ArrayList<>();
        blocksToCheck.addAll(Arrays.asList(start.up(), start.north(), start.south(), start.east(), start.west()));

        for (BlockPos blockToCheck : blocksToCheck) {
            if (!alreadyChecked.contains(blockToCheck) && getFluidFromBlockState(world.getBlockState(blockToCheck)) == fluid) {
                if (getFluidLevel(world, blockToCheck) == getStillFluidLevel(fluid.getBlock())) {
                    return blockToCheck;
                } else {
                    alreadyChecked.add(blockToCheck);
                    BlockPos source = findSourceBlockPos(world, blockToCheck, fluid, alreadyChecked);
                    if (source != null) {
                        return source;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param tile The tile (nonnull)
     * @param dir The direction (can be null)
     * @return An {@link IFluidHandler} for the Tile and direction. If it uses the deprecated API, returns a new wrapper.
     */
    public static IFluidHandler getFluidHandler(TileEntity tile, EnumFacing dir) {
        if (tile.hasCapability(FLUID_HANDLER_CAPABILITY, dir)) {
            return tile.getCapability(FLUID_HANDLER_CAPABILITY, dir);
        }
        if (tile instanceof IFluidHandler) {
            return (IFluidHandler) tile;
        }
        return null;
    }
}
