package flaxbeard.steamcraft.tile;

import java.util.ArrayList;

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
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntitySteamPipe extends SteamTransporterTileEntity implements IFluidHandler,ISteamTransporter {
	protected FluidTank dummyFluidTank = FluidRegistry.isFluidRegistered("steam") ? new FluidTank(new FluidStack(FluidRegistry.getFluid("steam"), 0),10000) : null;

	
	protected boolean isLeaking = false;
	
	public TileEntitySteamPipe(){
		super(ForgeDirection.values());
	}
	
	public TileEntitySteamPipe(int capacity){
		this();
		this.capacity = capacity;
	}

//	@Override
//	public int getCapacity() {
//		return 1000;
//	}

//	@Override
//	public int getSteam() {
//		return super.getSteam();
//	}

//	@Override
//	public void insertSteam(int amount, ForgeDirection face) {
//		this.steam += amount;
//	}
	
//	@Override
//    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
//    {
//        super.readFromNBT(par1NBTTagCompound);
//        this.steam = par1NBTTagCompound.getShort("steam");
//    }

//    @Override
//    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
//    {
//        super.writeToNBT(par1NBTTagCompound);
//        par1NBTTagCompound.setShort("steam",(short) this.steam);
//    }
	
	@Override
	public Packet getDescriptionPacket()
	{
    	NBTTagCompound access = super.getDescriptionTag();
    	access.setBoolean("isLeaking", this.isLeaking);
        
        
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.isLeaking = access.getBoolean("isLeaking");
    	
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    public void superUpdate(){
    	super.updateEntity();
    }
	
	@Override
	public void updateEntity() {
		if (Steamcraft.steamRegistered) {
			this.dummyFluidTank.setFluid(new FluidStack(FluidRegistry.getFluid("steam"), this.getSteam()*10));
		}
		super.updateEntity();

		ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
		for (ForgeDirection direction : ForgeDirection.values()) {
			if (worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) != null) {
				TileEntity tile = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
				if (tile instanceof ISteamTransporter) {
					ISteamTransporter target = (ISteamTransporter) tile;
					if (target.doesConnect(direction.getOpposite())) {
						myDirections.add(direction);
					}
				}
				else if (tile instanceof IFluidHandler && Steamcraft.steamRegistered) {
					IFluidHandler target = (IFluidHandler) tile;
					if (target.canDrain(direction.getOpposite(), FluidRegistry.getFluid("steam")) || target.canFill(direction.getOpposite(), FluidRegistry.getFluid("steam"))) {
						myDirections.add(direction);
					}
				}
			}
		}
		int i = 0;
		if (myDirections.size() > 0) {
			ForgeDirection direction = myDirections.get(0).getOpposite();
			if (!worldObj.isRemote){
				if (myDirections.size() == 2 && this.getSteam() > 0 && i < 10 && (worldObj.isAirBlock(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) || !worldObj.isSideSolid(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ, direction.getOpposite()))) {
					this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:leaking", 2.0F, 0.9F);
					if (!isLeaking){
						System.out.println("Block is leaking!");
						isLeaking = true;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						markDirty();
					}
					
				} else {
					if (isLeaking){
						System.out.println("Block is no longer leaking!");
						isLeaking = false;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						markDirty();
					}
				}
				while (myDirections.size() == 2 && this.getSteam() > 0 && i < 10 && (worldObj.isAirBlock(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) || !worldObj.isSideSolid(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ, direction.getOpposite()))) {
					if (worldObj.isRemote){
						System.out.println("I AM THE CLIENT!");
					}
					this.decrSteam(1);
					
					i++;
				}
			}
			if (worldObj.isRemote && this.isLeaking){
				this.worldObj.spawnParticle("smoke", xCoord+0.5F, yCoord+0.5F, zCoord+0.5F, direction.offsetX*0.1F, direction.offsetY*0.1F, direction.offsetZ*0.1F);
			}
			
		}
		
		
	}
	
//	@Override
//	public boolean acceptsGauge(ForgeDirection face) {
//		return true;
//	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (resource.amount >= 10) {
			if (doFill) {
				this.insertSteam((resource.amount-resource.amount%10)/10, from);
			}
			FluidStack resource2 = resource.copy();
			resource2.amount = resource.amount-resource.amount%10;
			if (resource2 != null) {
				return dummyFluidTank.fill(resource2, doFill)+resource.amount%10;
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return (Steamcraft.steamRegistered ? fluid == FluidRegistry.getFluid("steam") : false);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {

		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if (Steamcraft.steamRegistered) {
			return new FluidTankInfo[] {dummyFluidTank.getInfo()};
		}
		return null;
	}

}
