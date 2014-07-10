package flaxbeard.steamcraft.api.tile;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.Tuple3;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import flaxbeard.steamcraft.block.BlockSteamGauge;

public class SteamTransporterTileEntity extends TileEntity implements ISteamTransporter{

	public float pressureResistance = 0.5F;
	public float lastPressure = -1F;
	public float pressure;
	private String networkName;
	private SteamNetwork network;
	public int capacity;
	private ForgeDirection[] distributionDirections;
	private ArrayList<ForgeDirection> gaugeSideBlacklist = new ArrayList<ForgeDirection>();
	
	public SteamTransporterTileEntity(){
		this(ForgeDirection.VALID_DIRECTIONS);
	}
	
	
	public SteamTransporterTileEntity(ForgeDirection[] distributionDirections){
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
        if (networkName != null) {
        	access.setString("networkName", networkName);
        	access.setFloat("pressure", this.getPressure());
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	
	public NBTTagCompound getDescriptionTag()
	{
        NBTTagCompound access = new NBTTagCompound();
        if (networkName != null){
        	//System.out.println("Setting pressure!");
        	access.setString("networkName", networkName);
            access.setFloat("pressure", this.getPressure());
        }
        return access;
	}
	
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	if (access.hasKey("networkName")) {
    		this.networkName = access.getString("networkName");
    		this.pressure = access.getFloat("pressure");
    		System.out.println("Set pressure to "+this.pressure);
    	}
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	markDirty();
    }
	 
	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
        
        if (compound.hasKey("networkName")){
        	this.networkName = compound.getString("networkName");
        }
    }
	
	@Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (networkName != null)
        	compound.setString("networkName", networkName);
    }
	
	public int getCapacity(){
		return this.capacity;
	}
	
	public float getPressure(){
		return (this.network != null) ? (float)this.network.getPressure() : this.pressure;
	}
	
	@Override
	public void updateEntity(){
		if (this.network == null && !worldObj.isRemote){
			System.out.println("Null network");
			if (this.networkName != null){
				this.network = SteamNetworkRegistry.getInstance().getNetwork(this.networkName);
				if (this.network == null){
					SteamNetwork.newOrJoin(this);
				}
				
			} else {
				System.out.println("Requesting new network build");
				SteamNetwork.newOrJoin(this);
				
			}
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if (this.hasGauge()){
			if (Math.abs(this.getPressure() - this.lastPressure) > 0.01F){
				System.out.println("Updating PRESHAAA");
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.lastPressure = this.getPressure();
			}
			
		}
	}
	
	@Override
	public void insertSteam(int amount, ForgeDirection face) {
		if (this.network != null){
			this.network.addSteam(amount);
		}
	}
	
	@Override
	public void decrSteam(int i) {
		if (this.network != null){
			this.network.decrSteam(i);
		}
	}
	
	@Override
	public void explode() {
    	UtilSteamTransport.preExplosion(worldObj, xCoord, yCoord, zCoord, this.distributionDirections);
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


	@Override
	public String getNetworkName() {
		return this.networkName;
	}
	
	public void setNetworkName(String name){
		this.networkName = name;
	}
	
	public SteamNetwork getNetwork(){
		return this.network;
	}
	
	public void setNetwork(SteamNetwork network){
		this.network = network;
	}

	@Override
	public int getSteam() {
		if (this.network != null){
			int mySteam = (int)(Math.floor((double)this.getCapacity() * (double)this.network.getPressure()));
			return mySteam;
		}
		return 0;
	}
	
	public boolean hasGauge(){
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if (this.acceptsGauge(dir)){
				Block block = worldObj.getBlock(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
				if (block instanceof BlockSteamGauge)
					return true;
			}
		}
		return false;
	}
	
	 
}
