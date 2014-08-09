package flaxbeard.steamcraft.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntityFluidSteamConverter extends SteamTransporterTileEntity implements ISteamTransporter,IFluidHandler,IWrenchable {
	private FluidTank dummyTank;
	private boolean isInitialized = false;
	private boolean lastRunning = false;
	public int runTicks = 0;
	
	@Override
	public Packet getDescriptionPacket()
	{
        NBTTagCompound access = super.getDescriptionTag();
        access.setShort("runTicks",(short)runTicks);
        

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	if (runTicks == 0 && access.getShort("runTicks") != 0) {
    		runTicks = access.getShort("runTicks");
    	}

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
			this.setDistributionDirections(new ForgeDirection[] { ForgeDirection.getOrientation( this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)).getOpposite()});
		}
		super.updateEntity();
	}
	
	@Override
	public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float xO, float yO, float zO) {
		int steam = this.getSteamShare();
		this.getNetwork().split(this, true);
		this.setDistributionDirections(new ForgeDirection[] { ForgeDirection.getOrientation( this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)).getOpposite()});
		
		SteamNetwork.newOrJoin(this);
		this.getNetwork().addSteam(steam);
		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (from.ordinal() != meta) {
			return 0;
		}
        if (resource.fluidID == FluidRegistry.getFluid("steam").getID()) {
        	if (doFill) {
        		this.insertSteam(resource.amount, from);
            	runTicks = runTicks > 0 ? runTicks : 100;
        	}
        	return resource.amount;
        }
        return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		////Steamcraft.log.debug("t");

		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (from.ordinal() != meta) {
			return null;
		}
		Fluid fluid = FluidRegistry.getFluid("steam");
        int drained = resource.amount;
        if (this.getSteamShare() < drained)
        {
            drained = this.getSteamShare();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain)
        {
            this.decrSteam(drained);
        	runTicks = stack.amount > 0 ? (runTicks > 0 ? runTicks : 100) : runTicks;
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
        int drained = maxDrain;
        if (this.getSteamShare() < drained)
        {
            drained = this.getSteamShare();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (doDrain)
        {
            this.decrSteam(drained);
        	runTicks = stack.amount > 0 ? (runTicks > 0 ? runTicks : 100) : runTicks;

        }
        return stack;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
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
		dummyTank = new FluidTank(new FluidStack(FluidRegistry.getFluid("steam"),this.getSteamShare()), this.getCapacity());
		return new FluidTankInfo[] {dummyTank.getInfo()};
	}

}
