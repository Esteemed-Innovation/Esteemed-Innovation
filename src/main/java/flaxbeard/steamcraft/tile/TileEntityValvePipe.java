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
	private boolean wasTurning = false;
	public int turnTicks=0;
	private boolean redstoneState;
	private boolean isInitialized = false;
	private boolean waitingOpen = false;
	
	public TileEntityValvePipe(){
		super(0);
	}
	
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
        access.setBoolean("leaking", isLeaking);
        access.setInteger("turnTicks", turnTicks);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.turning = access.getBoolean("turning");
    	this.isLeaking = access.getBoolean("leaking");
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
		super.superUpdate();
		if (worldObj.isRemote){
			if (turning && turnTicks <10){
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
			
			if (isLeaking){
				ForgeDirection myDir = dir();
				ForgeDirection[] directions = new ForgeDirection[6];
				int i = 0;
				for (ForgeDirection direction : ForgeDirection.values()) {
					if (direction != myDir) {
						directions[i] = direction;
						i++;
					}
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
				ForgeDirection direction = myDirections.get(0).getOpposite();
				while (myDirections.size() == 2 && open && i < 10 && (worldObj.isAirBlock(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) || !worldObj.isSideSolid(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ, direction.getOpposite()))) {
					//this.decrSteam(1);
					this.worldObj.spawnParticle("smoke", xCoord+0.5F, yCoord+0.5F, zCoord+0.5F, direction.offsetX*0.1F, direction.offsetY*0.1F, direction.offsetZ*0.1F);
					i++;
				}
			}
		} else {
			if (this.waitingOpen){
				System.out.println("Waiting for open");
				this.setOpen(!this.open);
			}
			if (turning != wasTurning){
				wasTurning = turning;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			if (turning && turnTicks < 10) {
				turnTicks++;
			}
			if (turnTicks >= 10) {
				turning = false;
				this.setOpen(!this.open);
				turnTicks = 0;
			}
			if (!turning) {
				if (wasTurning){
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
				this.turnTicks = 0;
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
				
				
				if (myDirections.size() == 2 && open && this.getNetwork().getSteam() > 0 && i < 10 && (worldObj.isAirBlock(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) || !worldObj.isSideSolid(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ, direction.getOpposite()))) {
					//System.out.println("open and should be leaking");
					if (!isLeaking){
						isLeaking = true;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
					this.decrSteam(10);
					this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:leaking", 2.0F, 0.9F);
				} else {
					//System.out.println("Probably shouldn't be leaking");
					if (isLeaking){
						isLeaking = false;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
				}
				
			} else {
				if (isLeaking){
					isLeaking = false;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
		}

		
		//this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	private void setOpen(boolean open) {
		this.open = open;
		boolean changed = true;
		if (!worldObj.isRemote){
			if (open){
				if (SteamNetworkRegistry.getInstance().isInitialized(this.getDimension())){
					SteamNetwork.newOrJoin(this);
				} else {
					changed = false;
					this.waitingOpen=true;
				}
			} else {
				if (this.getNetwork() != null){
					this.getNetwork().split(this);
				} else {
					changed = false;
					this.waitingOpen = true;
				}
				
			}
		}
		if (!changed){
			this.open=!open;
		} else {
			this.waitingOpen = false;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
	
	public boolean isOpen(){
		return this.open;
	}
}
