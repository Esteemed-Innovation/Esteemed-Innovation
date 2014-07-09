package flaxbeard.steamcraft.api;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class SteamTransporterTileEntity extends TileEntity implements ISteamTransporter{

	public float pressureResistance = 0.5F;
	public int steam;
	public int capacity;
	public int lastSteam = 0;
	private ForgeDirection[] distributionDirections;
	private ArrayList<ForgeDirection> gaugeSideBlacklist = new ArrayList<ForgeDirection>();
	
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
	
	public NBTTagCompound getDescriptionTag()
	{
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("steam", steam);
        access.setInteger("lastSteam", lastSteam);
        return access;
	}
	
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.steam = access.getInteger("steam");
    	this.lastSteam = access.getInteger("lastSteam");
    	
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
			if (!worldObj.isRemote){
				System.out.println("Pressure change!");
				this.lastSteam = this.steam;
				UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,this.distributionDirections);
		    	UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
		    	//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			
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
    	UtilSteamTransport.preExplosion(worldObj, xCoord, yCoord, zCoord, this.distributionDirections);
		this.steam = 0;
	}

	private boolean isValidSteamSide(ForgeDirection face){
		for (ForgeDirection d : distributionDirections){
			if (d == face){
				return true;
			}
		}
		return false;
	}
	

	@Override
	public boolean canInsert(ForgeDirection face) {
		return isValidSteamSide(face);
	}


	public void addSideToGaugeBlacklist(ForgeDirection face){
		gaugeSideBlacklist.add(face);
	}
	
	public void addSidesToGaugeBlacklist(ForgeDirection[] faces){
		for (ForgeDirection face : faces){
			addSideToGaugeBlacklist(face);
		}
	}
	
	@Override
	public boolean doesConnect(ForgeDirection face) {
		return isValidSteamSide(face);
	}


	@Override
	public boolean acceptsGauge(ForgeDirection face) {
		return !gaugeSideBlacklist.contains(face);
	}

	protected void setPressureResistance(float resistance){
		this.pressureResistance = resistance;
	}
	
	public float getPressureResistance(){
		return this.pressureResistance;
	}
	
	public void setDistributionDirections(ForgeDirection[] faces){
		this.distributionDirections = faces;
	}


	@Override
	public HashSet<ForgeDirection> getConnectionSides() {
		HashSet<ForgeDirection> out = new HashSet();
		for (ForgeDirection d : distributionDirections){
			out.add(d);
		}
		return out;
	}


	@Override
	public Tuple3<Integer, Integer, Integer> getCoords() {
		return new Tuple3(xCoord, yCoord,zCoord);
	}
	
	 
}
