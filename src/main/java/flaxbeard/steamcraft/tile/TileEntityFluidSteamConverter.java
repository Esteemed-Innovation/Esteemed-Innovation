package flaxbeard.steamcraft.tile;

import cpw.mods.fml.common.Loader;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import static org.apache.commons.lang3.ArrayUtils.add;

public class TileEntityFluidSteamConverter extends SteamTransporterTileEntity implements ISteamTransporter, IFluidHandler, IWrenchable {
    public int runTicks = 0;
    private FluidTank dummyTank;
    private FluidTank ic2DummyTank;
    private boolean isInitialized = false;
    private boolean lastRunning = false;

    public boolean pushing = false; // Indicates that converter is pushing steam actively.
    private static final int PUSH_MAX = 250; // in mb a tick

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound access = super.getDescriptionTag();
        access.setShort("runTicks", (short) runTicks);
        access.setBoolean("pushing", pushing);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.func_148857_g();
        if (runTicks == 0 && access.getShort("runTicks") != 0) {
            runTicks = access.getShort("runTicks");
        }
        pushing = access.getBoolean("pushing");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("pushing", pushing);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        pushing = par1NBTTagCompound.getBoolean("pushing");
    }

    @Override
    public void updateEntity() {
        if (runTicks > 0) {
            runTicks--;
        }
        if (runTicks > 0 != lastRunning) {
            this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        lastRunning = runTicks > 0;

        if (!this.isInitialized) {
            this.setDistributionDirections(new ForgeDirection[]{ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)).getOpposite()});
        }

        if (pushing) {
            int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            ForgeDirection dir = ForgeDirection.getOrientation(meta);

            TileEntity tileEntity = this.worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            SteamNetwork steamNetwork = this.getNetwork();
            if(tileEntity != null && tileEntity instanceof IFluidHandler && steamNetwork != null) {
                //To make for some interesting feedback systems, active push amount is based on current network pressure
                //This could be considered for non pushing systems, but there's no guarantee of consistency of the pull rate
                //from different mods, so something complicated would have to be done to audit that behaviour.
                //todo: external config for converter behaviour?
                //todo: apply limiting mechanism to non-pushing state?

                IFluidHandler tank = (IFluidHandler) tileEntity;
                int maxDrain = (int)(PUSH_MAX * steamNetwork.getPressure());
                if (maxDrain > 0) {
                    FluidStack avail = drain(dir, maxDrain, false);
                    int taken = tank.fill(dir.getOpposite(), avail, true);
                    steamNetwork.decrSteam(taken);
                }
            }
        }

        super.updateEntity();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xO, float yO, float zO) {
        if (player.isSneaking()) {
            pushing = !pushing;
        } else {
            int steam = this.getSteamShare();
            this.getNetwork().split(this, true);
            this.setDistributionDirections(new ForgeDirection[]{ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)).getOpposite()});

            SteamNetwork.newOrJoin(this);
            this.getNetwork().addSteam(steam);
        }
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

        if (from.ordinal() != meta) {
            return 0;
        }

        if (pushing) {
            return 0;
        }

        if (resource.getFluid().getID() == FluidRegistry.getFluid("steam").getID()) {
            if (doFill) {
                this.insertSteam(resource.amount, from);
                runTicks = runTicks > 0 ? runTicks : 100;
            }
            return resource.amount;
        }

        if (Loader.isModLoaded("IC2") && Config.enableIC2Integration && resource.getFluid().getID() == FluidRegistry.getFluid("ic2steam").getID()){
            if (doFill) {
                this.insertSteam(resource.amount, from);
                runTicks = runTicks > 0 ? runTicks : 100;
            }
            return resource.amount;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        ////Steamcraft.log.debug("t");

        int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        if (from.ordinal() != meta) {
            return null;
        }
        Fluid fluid = FluidRegistry.getFluid("steam");
        int drained = resource.amount;
        if (this.getSteamShare() < drained) {
            drained = this.getSteamShare();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain) {
            this.decrSteam(drained);
            runTicks = stack.amount > 0 ? (runTicks > 0 ? runTicks : 100) : runTicks;
        }

        if (Loader.isModLoaded("IC2") && Config.enableIC2Integration) {
            Fluid ic2Fluid = FluidRegistry.getFluid("ic2steam");
            int ic2Drained = resource.amount;
            if (this.getSteamShare() > ic2Drained) {
                ic2Drained = this.getSteamShare();
            }

            FluidStack ic2Stack = new FluidStack(ic2Fluid, ic2Drained);
            if (doDrain) {
                this.decrSteam(ic2Drained);
                runTicks = ic2Stack.amount > 0 ? (runTicks > 0 ? runTicks : 100) : runTicks;
            }
        }
        return stack;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        if (from.ordinal() != meta) {
            return null;
        }
        Fluid fluid = FluidRegistry.getFluid("steam");
        if (this.getSteamShare() < maxDrain) {
            maxDrain = this.getSteamShare();
        }
        FluidStack stack = new FluidStack(fluid, maxDrain);
        if (doDrain) {
            this.decrSteam(maxDrain);
            runTicks = stack.amount > 0 ? (runTicks > 0 ? runTicks : 100) : runTicks;
        }

        if (Loader.isModLoaded("IC2") && Config.enableIC2Integration) {
            Fluid ic2Fluid = FluidRegistry.getFluid("ic2steam");
            int ic2Drained = maxDrain;
            if (this.getSteamShare() > ic2Drained) {
                ic2Drained = this.getSteamShare();
            }
            FluidStack ic2Stack = new FluidStack(ic2Fluid, ic2Drained);
            if (doDrain) {
                this.decrSteam(ic2Drained);
                runTicks = ic2Stack.amount > 0 ? (runTicks > 0 ? runTicks : 100) : runTicks;
            }
        }
        return stack;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        if (pushing) {
            return false; //steam is blasting out! You can't put it back in!
        }

        int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return from.ordinal() != meta;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return from.ordinal() != meta;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        FluidTankInfo[] fti = {};
        if (from.ordinal() != meta) {
            return fti;
        }

        Fluid steam;
        steam = FluidRegistry.getFluid("steam");
        if (steam != null) {
            fti = add(fti, new FluidTank(new FluidStack(steam, this.getSteamShare()), this.getCapacity()).getInfo());
        }

        steam = FluidRegistry.getFluid("ic2steam");
        if (steam != null) {
            fti = add(fti, new FluidTank(new FluidStack(steam, this.getSteamShare()), this.getCapacity()).getInfo());
        }

        return fti;
    }

}
