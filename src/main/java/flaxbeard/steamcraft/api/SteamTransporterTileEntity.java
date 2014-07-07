package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class SteamTransporterTileEntity extends TileEntity implements ISteamTransporter{

	public int steam;
	public int capacity;
	public int lastSteam = 0;
	private ForgeDirection[] distributionDirections;
	
	public SteamTransporterTileEntity(){
		this(ForgeDirection.VALID_DIRECTIONS);
	}
	
	
	public SteamTransporterTileEntity(ForgeDirection[] distributionDirections){
		this.steam = 0;
		this.capacity = 1000;
		this.distributionDirections = distributionDirections;
	}
	
	public SteamTransporterTileEntity(int capacity, ForgeDirection[] distributionDirections){
		this(distributionDirections);
		this.capacity = capacity;
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
    	super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("steam", steam);
        access.setInteger("lastSteam", lastSteam);
        
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.steam = access.getInteger("steam");
    	this.lastSteam = access.getInteger("lastSteam");
    }
	 
	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
        
        if (compound.hasKey("steam"))
        {
        	this.steam = compound.getShort("steam");
        }
        if (compound.hasKey("lastSteam")){
        	this.lastSteam = compound.getShort("lastSteam");
        }
    }
	
	@Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("steam",(short) this.steam);
        compound.setShort("lastSteam",(short) this.lastSteam);
    }
	
	public int getSteam(){
		return this.steam;
	}
	
	public int getCapacity(){
		return this.capacity;
	}
	 
	public int getLastSteam(){
		return this.lastSteam;
	}
	
	public float getPressure(){
		return (float)this.steam/(float)this.capacity;
	}
	
	@Override
	public void updateEntity(){
		if (this.steam != this.lastSteam){
			System.out.println("Pressure change!");
			this.lastSteam = this.steam;
			UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,this.distributionDirections);
	    	UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
	    }
		
	}
	
	@Override
	public void insertSteam(int amount, ForgeDirection face) {
		steam+=amount;
	}
	
	@Override
	public void decrSteam(int i) {
		this.steam-=i;
	}
	
	@Override
	public void explode() {
    	UtilSteamTransport.preExplosion(worldObj, xCoord, yCoord, zCoord,new ForgeDirection[] { ForgeDirection.UP });
		this.steam = 0;
	}


	@Override
	public boolean canInsert(ForgeDirection face) {
		return false;
	}


	@Override
	public boolean doesConnect(ForgeDirection face) {
		return false;
	}


	@Override
	public boolean acceptsGauge(ForgeDirection face) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
