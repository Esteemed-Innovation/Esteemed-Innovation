package eiteam.esteemedinnovation.tile.pipe;

import eiteam.esteemedinnovation.api.tile.TileEntityBase;
import eiteam.esteemedinnovation.data.capabilities.FluidPipeBlockCapabilities;
import eiteam.esteemedinnovation.data.capabilities.TemperatureFluidTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

// TODO: Impellers
public class TileEntityTemperatureFluidPipe extends TileEntityBase implements ITickable {
    private int transferAmount;
    private int tickCounter = 0;
    private EnumFacing prevInteractSide = null;
    private List<EnumFacing> dirs = new ArrayList<EnumFacing>() {{
        add(EnumFacing.DOWN);
        for (EnumFacing dir : EnumFacing.HORIZONTALS) {
            add(dir);
        }
        add(EnumFacing.UP);
    }};
    public TemperatureFluidTank tank;

    public TileEntityTemperatureFluidPipe(int transferAmount, int minTemp, int maxTemp) {
        this.transferAmount = transferAmount;
        tank = new TemperatureFluidTank(minTemp, maxTemp);
    }

    @Override
    public void update() {
        if (worldObj.isRemote) {
            return;
        }

        tickCounter++;

        if (tickCounter > 10) {
            tickCounter = 0;
        }

        if (tickCounter != 10) {
            return;
        }

        FluidPipeBlockCapabilities.Mode transferMode = worldObj.getBlockState(pos).getValue(FluidPipeBlockCapabilities.MODE);

        // TODO: Impeller
        for (EnumFacing dir : dirs) {
            BlockPos offset = pos.offset(dir);
            TileEntity offsetTile = worldObj.getTileEntity(offset);
            if (offsetTile == null) {
                continue;
            }


            if (transferMode == FluidPipeBlockCapabilities.Mode.TRANSFER) {
                if (offsetTile instanceof TileEntityTemperatureFluidPipe && offsetTile.hasCapability(FLUID_HANDLER_CAPABILITY, dir)) {
                    FluidPipeBlockCapabilities.Mode offsetMode = worldObj.getBlockState(offset).getValue(FluidPipeBlockCapabilities.MODE);
                    IFluidHandler handler = offsetTile.getCapability(FLUID_HANDLER_CAPABILITY, dir);
                    if (offsetMode != FluidPipeBlockCapabilities.Mode.INPUT) {
                        tryFillOurTank(handler);
                        prevInteractSide = dir;
                    }
                    if (offsetMode != FluidPipeBlockCapabilities.Mode.OUTPUT) {
                        tryDrainOurTank(handler);
                        prevInteractSide = dir;
                    }
                }
            }
            if (transferMode == FluidPipeBlockCapabilities.Mode.INPUT && !(offsetTile instanceof TileEntityTemperatureFluidPipe)) {
                IFluidHandler cap = null;
                if (offsetTile.hasCapability(FLUID_HANDLER_CAPABILITY, dir)) {
                    cap = offsetTile.getCapability(FLUID_HANDLER_CAPABILITY, dir);
                } else if (offsetTile instanceof IFluidHandler) {
                    cap = (IFluidHandler) offsetTile;
                } else if (offsetTile instanceof net.minecraftforge.fluids.IFluidHandler) {
                    net.minecraftforge.fluids.IFluidHandler deprecatedHandler = (net.minecraftforge.fluids.IFluidHandler) offsetTile;
                    tryDrainOurTankDeprecated(deprecatedHandler, dir);
                    prevInteractSide = dir;
                }
                if (cap != null) {
                    tryDrainOurTank(cap);
                    prevInteractSide = dir;
                }
            } else if (transferMode == FluidPipeBlockCapabilities.Mode.OUTPUT && !(offsetTile instanceof TileEntityTemperatureFluidPipe)) {
                IFluidHandler cap = null;
                if (offsetTile.hasCapability(FLUID_HANDLER_CAPABILITY, dir)) {
                    cap = offsetTile.getCapability(FLUID_HANDLER_CAPABILITY, dir);
                } else if (offsetTile instanceof IFluidHandler) {
                    cap = (IFluidHandler) offsetTile;
                } else if (offsetTile instanceof net.minecraftforge.fluids.IFluidHandler) {
                    net.minecraftforge.fluids.IFluidHandler deprecatedHandler = (net.minecraftforge.fluids.IFluidHandler) offsetTile;
                    tryFillOurTankDeprecated(deprecatedHandler, dir);
                    prevInteractSide = dir;
                }
                if (cap != null) {
                    tryFillOurTank(cap);
                    prevInteractSide = dir;
                }
            }
        }

        if (prevInteractSide != null) {
            dirs.remove(prevInteractSide);
            dirs.add(0, prevInteractSide);
        }
    }

    private boolean tankHasEnoughRooom() {
        return tank.getFluidAmount() + transferAmount <= tank.getCapacity();
    }

    private void safelyFillAndDrain(IFluidHandler toFill, IFluidHandler toDrain) {
        FluidStack firstDrain = toDrain.drain(transferAmount, false);
        if (firstDrain != null) {
            int testFill = toFill.fill(firstDrain, false);
            if (testFill > 0) {
                FluidStack secondDrain = toDrain.drain(testFill, false);
                if (secondDrain != null) {
                    toDrain.drain(toFill.fill(firstDrain, true), true);
                }
            }
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private void tryFillOurTank(IFluidHandler capability) {
        safelyFillAndDrain(tank, capability);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private void tryDrainOurTank(IFluidHandler capability) {
        safelyFillAndDrain(capability, tank);
    }

    // Suppress duplicates because the two IFluidHandlers are not interchangeable.

    @SuppressWarnings({"SimplifiableIfStatement", "Duplicates"})
    private void tryFillOurTankDeprecated(net.minecraftforge.fluids.IFluidHandler handler, EnumFacing dir) {
        if (tankHasEnoughRooom()) {
            FluidStack firstDrain = handler.drain(dir, transferAmount, false);
            if (firstDrain != null) {
                int testFill = tank.fill(firstDrain, false);
                if (testFill > 0) {
                    FluidStack testDrain = handler.drain(dir, testFill, false);
                    if (testDrain != null) {
                        handler.drain(dir, tank.fill(firstDrain, true), true);
                    }
                }
            }
        }
    }

    @SuppressWarnings({"SimplifiableIfStatement", "Duplicates"})
    private void tryDrainOurTankDeprecated(net.minecraftforge.fluids.IFluidHandler handler, EnumFacing dir) {
        if (tank.getFluid() != null) {
            FluidStack firstDrain = tank.drain(transferAmount, false);
            if (firstDrain != null) {
                int testFill = handler.fill(dir, firstDrain, false);
                if (testFill > 0) {
                    FluidStack testDrain = tank.drain(testFill, false);
                    if (testDrain != null) {
                        tank.drain(handler.fill(dir, firstDrain, true), true);
                    }
                }
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
        return capability == FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }
        return super.getCapability(capability, facing);
    }
}
