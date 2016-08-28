package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.block.BlockFunnel;
import flaxbeard.steamcraft.misc.FluidHelper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.HashSet;

import static net.minecraftforge.fluids.Fluid.BUCKET_VOLUME;
import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class TileEntityFunnel extends TileEntity implements ITickable {
    protected FluidTank tank = new FluidTank(BUCKET_VOLUME);

    @Override
    public void update() {
        if (worldObj.isRemote || worldObj.getBlockState(pos).getActualState(worldObj, pos).getValue(BlockFunnel.POWERED)) {
            return;
        }

        if (tank.getFluidAmount() == 0) {
            BlockPos up = pos.up();
            IBlockState aboveState = worldObj.getBlockState(up);
            Fluid fluid = FluidHelper.getFluidFromBlockState(aboveState);
            if (fluid != null) {
                BlockPos source = FluidHelper.findSourceBlockPos(worldObj, up, fluid, new HashSet<BlockPos>());
                if (source != null) {
                    IBlockState sourceState = worldObj.getBlockState(source);
                    Block sourceBlock = sourceState.getBlock();
                    FluidStack fluidStack;
                    if (sourceBlock instanceof IFluidBlock) {
                        fluidStack = ((IFluidBlock) sourceBlock).drain(worldObj, source, true);
                    } else {
                        fluidStack = new FluidStack(fluid, BUCKET_VOLUME);
                        if (!FluidHelper.isInfiniteWaterSource(worldObj, up, fluid)) {
                            worldObj.setBlockToAir(source);
                        }
                    }
                    tank.fill(fluidStack, true);
                }
            }
        }

        FluidStack fluid = tank.getFluid();
        if (tank.getFluidAmount() > 0 && fluid != null) {
            BlockPos down = pos.down();
            TileEntity belowTile = worldObj.getTileEntity(down);
            if (belowTile == null) {
                return;
            }

            IFluidHandler handler = null;
            if (belowTile.hasCapability(FLUID_HANDLER_CAPABILITY, EnumFacing.UP)) {
                handler = belowTile.getCapability(FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
            } else if (belowTile instanceof IFluidHandler) {
                handler = (IFluidHandler) belowTile;
            } else if (belowTile instanceof net.minecraftforge.fluids.IFluidHandler) {
                // Really... Why are you still using this.
                net.minecraftforge.fluids.IFluidHandler deprecatedHandler = (net.minecraftforge.fluids.IFluidHandler) belowTile;
                deprecatedHandler.fill(EnumFacing.UP, fluid, true);
                tank.drain(fluid, true);
            }

            if (handler != null) {
                handler.fill(fluid, true);
                tank.drain(fluid, true);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tank.writeToNBT(tag);
        return tag;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return facing.getAxis() == EnumFacing.Axis.Y && capability == FLUID_HANDLER_CAPABILITY ||
          super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == FLUID_HANDLER_CAPABILITY && facing.getAxis() == EnumFacing.Axis.Y) {
            return (T) tank;
        }
        return super.getCapability(capability, facing);
    }
}
