package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityPump extends TileEntity implements IFluidHandler,ISteamTransporter {
	public FluidTank myTank = new FluidTank(1000);
	public int progress = 0;
	public int steam = 0;
	
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.progress = par1NBTTagCompound.getShort("progress");
        this.steam = par1NBTTagCompound.getShort("steam");

        if (par1NBTTagCompound.hasKey("fluid")) {
            this.myTank.setFluid(new FluidStack(FluidRegistry.getFluid(par1NBTTagCompound.getShort("fluid")),par1NBTTagCompound.getShort("water")));

        }
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
	    super.writeToNBT(par1NBTTagCompound);
	    par1NBTTagCompound.setShort("progress",(short) progress);
        par1NBTTagCompound.setShort("steam",(short) this.steam);

	    par1NBTTagCompound.setShort("water",(short) myTank.getFluidAmount());
	    if (myTank.getFluid() != null) {
	    	par1NBTTagCompound.setShort("fluid",(short)myTank.getFluid().fluidID);
	    }
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
    	super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("steam", steam);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.steam = access.getInteger("steam");

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	    
	private ForgeDirection getOutputDirection() {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		if (meta == 0) {
			dir = ForgeDirection.SOUTH;
		}
		if (meta == 1) {
			dir = ForgeDirection.WEST;
		}
		if (meta == 2) {
			dir = ForgeDirection.NORTH;
		}
		if (meta == 3) {
			dir = ForgeDirection.EAST;
		}
		return dir.getOpposite();
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if (from != this.getOutputDirection()) {
			return null;
		}
        if (resource == null || !resource.isFluidEqual(myTank.getFluid()))
        {
            return null;
        }
        return myTank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (from == this.getOutputDirection()) {
			return myTank.drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return (from==this.getOutputDirection());
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { myTank.getInfo() };
	}
	
	@Override
	public void updateEntity() {
		UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,ForgeDirection.values());
		UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
		ForgeDirection inputDir = this.getOutputDirection().getOpposite();
		int x = this.xCoord + inputDir.offsetX;
		int y = this.yCoord + inputDir.offsetY;
		int z = this.zCoord + inputDir.offsetZ;
		if (this.steam >= 10 && myTank.getFluidAmount() == 0 && this.worldObj.getBlockMetadata(x, y, z) == 0 && FluidRegistry.lookupFluidForBlock(this.worldObj.getBlock(x, y, z)) != null) {
			Fluid fluid = FluidRegistry.lookupFluidForBlock(this.worldObj.getBlock(x,y,z));
			if (myTank.getFluidAmount() < 1000) {
				this.myTank.fill(new FluidStack(fluid,1000), true);
				this.worldObj.setBlockToAir(x,y,z);
				progress = 0;
				steam-=10;
			}
		}
		if (myTank.getFluidAmount() > 0 && myTank.getFluid() != null && progress < 100) {
			progress++;
		}
		ForgeDirection outputDir = this.getOutputDirection();
		int x2 = this.xCoord + outputDir.offsetX;
		int y2 = this.yCoord + outputDir.offsetY;
		int z2 = this.zCoord + outputDir.offsetZ;
		if (myTank.getFluidAmount() > 0 && progress == 100 && this.worldObj.getTileEntity(x2, y2, z2) != null && this.worldObj.getTileEntity(x2, y2, z2) instanceof IFluidHandler) {
			IFluidHandler fluidHandler = (IFluidHandler) this.worldObj.getTileEntity(x2,y2,z2);
			if (fluidHandler.canFill(inputDir, myTank.getFluid().getFluid())) {
				int amnt = fluidHandler.fill(inputDir, this.myTank.getFluid(), true);
				if (amnt > 0) {
					this.myTank.drain(1000, true);
					progress = 0;
				}
			}
		}
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

	}

	@Override
	public float getPressure() {
		return this.steam/1000.0F;
	}

	@Override
	public boolean canInsert(ForgeDirection face) {
		return true;
	}

	@Override
	public int getCapacity() {
		return 1000;
	}

	@Override
	public int getSteam() {
		return steam;
	}

	@Override
	public void insertSteam(int amount, ForgeDirection face) {
		this.steam+=amount;	
	}

	@Override
	public void decrSteam(int i) {
		this.steam -= i;
	}

	@Override
	public boolean doesConnect(ForgeDirection face) {
		return true;
	}

	@Override
	public boolean acceptsGauge(ForgeDirection face) {
		return false;
	}
}
