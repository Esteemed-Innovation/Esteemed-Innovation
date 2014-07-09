package flaxbeard.steamcraft.tile;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;

public class TileEntityValvePipe extends TileEntitySteamPipe {
	
	public boolean open = true;
	private boolean turning;
	public int turnTicks=0;
	private boolean redstoneState;
	private boolean isInitialized = false;
	
	public void updateRedstoneState(boolean flag) {
		
//		if (flag != redstoneState) {
//			if (!this.turning) {
//				this.setTurining();
//			}
//		}
		redstoneState = flag;
		
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
        NBTTagCompound access = super.getDescriptionTag();

        access.setBoolean("turning", turning);
        access.setBoolean("open", open);
        access.setInteger("turnTicks", turnTicks);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.turning = access.getBoolean("turning");
    	this.open = access.getBoolean("open");
    	this.turnTicks = access.getInteger("turnTicks");

    }
	
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.open = par1NBTTagCompound.getBoolean("open");
        this.redstoneState = par1NBTTagCompound.getBoolean("redstoneState");

    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("open",this.open);
        par1NBTTagCompound.setBoolean("redstoneState",this.redstoneState);

    }
	

	public ForgeDirection dir() {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		return ForgeDirection.getOrientation(meta);
	}
	
	@Override
	public boolean doesConnect(ForgeDirection face) {
		return face != dir();
	}
	
	@Override
	public void updateEntity() {
		if (this.getNetwork() == null && !worldObj.isRemote && this.open){
			System.out.println("Null network");
			if (this.getNetworkName() != null){
				this.setNetwork(SteamNetworkRegistry.getInstance().getNetwork(this.getNetworkName()));
				if (this.getNetwork() == null){
					SteamNetwork.newOrJoin(this);
				}
				
			} else {
				System.out.println("Requesting new network build");
				SteamNetwork.newOrJoin(this);
				
			}
		}
		if (!this.worldObj.isRemote) {
			if (turning && turnTicks < 10) {
				turnTicks++;
			}
			if (turnTicks >= 10) {
				turning = false;
				this.setOpen(!this.open);
				turnTicks = 0;
			}
			if (!turning) {
				this.turnTicks = 0;
			}
		}
		ForgeDirection myDir = dir();
		ForgeDirection[] directions = new ForgeDirection[6];
		int i = 0;
		for (ForgeDirection direction : ForgeDirection.values()) {
			if (direction != myDir) {
				directions[i] = direction;
				i++;
			}
		}
		if (Steamcraft.steamRegistered) {
			this.dummyFluidTank.setFluid(new FluidStack(FluidRegistry.getFluid("steam"), this.getSteam()*10));
		}
		if (!this.worldObj.isRemote && open) {
			UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,directions);
			UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
		}
		

		ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
		for (ForgeDirection direction : directions) {
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
		i = 0;
		if (myDirections.size() > 0) {
			ForgeDirection direction = myDirections.get(0).getOpposite();
			while (!this.doesConnect(direction)) {
				direction = ForgeDirection.getOrientation((direction.flag+1)%5);
			}
			if (myDirections.size() == 2 && open && this.getSteam() > 0 && i < 10 && (worldObj.isAirBlock(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) || !worldObj.isSideSolid(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ, direction.getOpposite()))) {
				this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:leaking", 2.0F, 0.9F);
			}
			while (myDirections.size() == 2 && open && this.getSteam() > 0 && i < 10 && (worldObj.isAirBlock(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) || !worldObj.isSideSolid(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ, direction.getOpposite()))) {
				this.decrSteam(1);
				this.worldObj.spawnParticle("smoke", xCoord+0.5F, yCoord+0.5F, zCoord+0.5F, direction.offsetX*0.1F, direction.offsetY*0.1F, direction.offsetZ*0.1F);
				i++;
			}
		}
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	private void setOpen(boolean open) {
		if (open){
			SteamNetwork.newOrJoin(this);
		} else {
			this.getNetwork().split(this);
		}
	}

	@Override
	public boolean canInsert(ForgeDirection face) {
		return face != dir() && open;
	}
	
	@Override
	public boolean acceptsGauge(ForgeDirection face) {
		return face != dir().getOpposite();
	}

	public boolean isTurning() {
		return turning;
	}

	public void setTurining() {
		this.turning = true;
		this.turnTicks = 0;
	}
}
