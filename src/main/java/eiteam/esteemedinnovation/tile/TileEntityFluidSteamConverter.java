package eiteam.esteemedinnovation.tile;

import eiteam.esteemedinnovation.api.ISteamTransporter;
import eiteam.esteemedinnovation.api.wrench.IWrenchable;
import eiteam.esteemedinnovation.api.steamnet.SteamNetwork;
import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
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
import net.minecraftforge.fluids.*;

import static org.apache.commons.lang3.ArrayUtils.add;

public class TileEntityFluidSteamConverter extends SteamTransporterTileEntity implements ISteamTransporter, IFluidHandler, IWrenchable {
    public int runTicks = 0;
    private boolean lastRunning = false;

    public boolean pushing = false; // Indicates that converter is pushing steam actively.
    private static final int PUSH_MAX = 250; // in mb a tick

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();
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
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        pushing = par1NBTTagCompound.getBoolean("pushing");
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

        setDistributionDirections(new EnumFacing[] {
          EnumFacing.getFront(getBlockMetadata()).getOpposite()
        });

        if (pushing) {
            int meta = getBlockMetadata();
            EnumFacing dir = EnumFacing.getFront(meta);
            TileEntity tileEntity = worldObj.getTileEntity(getOffsetPos(dir));
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
              EnumFacing.getFront(getBlockMetadata()).getOpposite()
            });

            SteamNetwork.newOrJoin(this);
            getNetwork().addSteam(steam);
        }
        return false;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        int meta = getBlockMetadata();

        if (from.ordinal() != meta) {
            return 0;
        }

        if (pushing) {
            return 0;
        }

        if (FluidRegistry.getFluid("steam") == resource.getFluid()) {
            if (doFill) {
                insertSteam(resource.amount, from);
                runTicks = runTicks > 0 ? runTicks : 100;
            }
            return resource.amount;
        }

        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        int meta = getBlockMetadata();
        if (from.ordinal() != meta) {
            return null;
        }
        Fluid fluid = FluidRegistry.getFluid("steam");
        int drained = resource.amount;
        if (getSteamShare() < drained) {
            drained = this.getSteamShare();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain) {
            this.decrSteam(drained);
            runTicks = stack.amount > 0 ? (runTicks > 0 ? runTicks : 100) : runTicks;
        }

        return stack;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        int meta = getBlockMetadata();
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

        return stack;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        if (pushing) {
            return false; //steam is blasting out! You can't put it back in!
        }

        int meta = getBlockMetadata();
        return from.ordinal() != meta;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        int meta = getBlockMetadata();
        return from.ordinal() != meta;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        int meta = getBlockMetadata();
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
