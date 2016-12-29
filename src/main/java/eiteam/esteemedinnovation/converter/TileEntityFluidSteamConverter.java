package eiteam.esteemedinnovation.converter;

import eiteam.esteemedinnovation.api.steamnet.SteamNetwork;
import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.api.wrench.Wrenchable;
import eiteam.esteemedinnovation.commons.util.FluidHelper;
import eiteam.esteemedinnovation.init.misc.integration.CrossMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityFluidSteamConverter extends SteamTransporterTileEntity implements Wrenchable {
    public int runTicks = 0;
    private boolean lastRunning = false;

    public boolean pushing = false; // Indicates that converter is pushing steam actively.
    private static final int PUSH_MAX = 250; // in mb a tick
    @Nullable
    private FluidTank tank;
    private final Fluid fluid;

    public TileEntityFluidSteamConverter() {
        Fluid steam = FluidRegistry.getFluid("steam");
        if (steam == null && CrossMod.IC2) {
            steam = FluidRegistry.getFluid("ic2steam");
        }
        if (steam != null) {
            tank = new FluidTank(steam, 0, getCapacity());
        }
        fluid = steam;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = getUpdateTag();
        access.setShort("runTicks", (short) runTicks);
        access.setBoolean("pushing", pushing);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        if (runTicks == 0 && access.getShort("runTicks") != 0) {
            runTicks = access.getShort("runTicks");
        }
        pushing = access.getBoolean("pushing");
        markForResync();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("pushing", pushing);
        if (tank != null) {
            tank.writeToNBT(nbt);
        }
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (tank != null) {
            tank.readFromNBT(nbt);
        }
        pushing = nbt.getBoolean("pushing");
    }

    @Override
    public void update() {
        if (runTicks > 0) {
            runTicks--;
        }
        if (runTicks > 0 != lastRunning) {
            markForResync();
        }
        lastRunning = runTicks > 0;
        EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockFluidSteamConverter.FACING);

        setDistributionDirections(new EnumFacing[] {
          dir.getOpposite()
        });

        if (fluid != null && tank != null && pushing) {
            TileEntity tileEntity = worldObj.getTileEntity(getOffsetPos(dir));
            if (tileEntity != null) {
                SteamNetwork steamNetwork = getNetwork();
                IFluidHandler handler = FluidHelper.getFluidHandler(tileEntity, dir);
                if (handler != null && steamNetwork != null) {
                    //To make for some interesting feedback systems, active push amount is based on current network pressure
                    //This could be considered for non pushing systems, but there's no guarantee of consistency of the pull rate
                    //from different mods, so something complicated would have to be done to audit that behaviour.
                    //todo: external config for converter behaviour?
                    //todo: apply limiting mechanism to non-pushing state?

                    int maxDrain = (int) (PUSH_MAX * steamNetwork.getPressure());
                    if (maxDrain > 0) {
                        tank.fill(new FluidStack(fluid, maxDrain), true);
                        FluidStack avail = tank.drain(maxDrain, false);
                        int taken = handler.fill(avail, true);
                        steamNetwork.decrSteam(taken);
                        if (taken > 0 && runTicks <= 0) {
                            runTicks = 100;
                            markForResync();
                        }
                    }
                }
            }
        }

        super.update();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, IBlockState state, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            pushing = !pushing;
        } else {
            int steam = getSteamShare();
            getNetwork().split(this, true);
            setDistributionDirections(new EnumFacing[] {
              state.getValue(BlockFluidSteamConverter.FACING).getOpposite()
            });

            SteamNetwork.newOrJoin(this);
            getNetwork().addSteam(steam);
        }
        markForResync();
        return false;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockFluidSteamConverter.FACING);
        return (tank != null && dir == facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockFluidSteamConverter.FACING);
        if (tank != null && dir == facing && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }
        return super.getCapability(capability, facing);
    }
}
