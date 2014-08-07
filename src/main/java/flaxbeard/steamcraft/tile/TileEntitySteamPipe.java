package flaxbeard.steamcraft.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.client.render.BlockSteamPipeRenderer;
import flaxbeard.steamcraft.codechicken.lib.raytracer.IndexedCuboid6;
import flaxbeard.steamcraft.codechicken.lib.raytracer.RayTracer;
import flaxbeard.steamcraft.codechicken.lib.vec.Cuboid6;
import flaxbeard.steamcraft.packet.SteamcraftClientPacketHandler;
import flaxbeard.steamcraft.packet.SteamcraftServerPacketHandler;

public class TileEntitySteamPipe extends SteamTransporterTileEntity implements ISteamTransporter,IWrenchable {
	//protected FluidTank dummyFluidTank = FluidRegistry.isFluidRegistered("steam") ? new FluidTank(new FluidStack(FluidRegistry.getFluid("steam"), 0),10000) : null;
	public ArrayList<Integer> blacklistedSides = new ArrayList<Integer>();
	
	
	
	protected boolean isLeaking = false;
	private boolean isSplitting = false;
	private int mySteam = 0;
	public Block disguiseBlock = null;
	public int disguiseMeta = 0;
	private boolean lastWrench = false;
	public boolean isOriginalPipe = false;
	public boolean isOtherPipe = false;
	
	public TileEntitySteamPipe(){
		super(ForgeDirection.values());
		this.name = "Pipe";
	}
	
	public TileEntitySteamPipe(int capacity){
		this();
		this.capacity = capacity;
	}

	@Override
	public Packet getDescriptionPacket()
	{
    	NBTTagCompound access = super.getDescriptionTag();
    	access.setBoolean("isLeaking", this.isLeaking);
    	NBTTagCompound list = new NBTTagCompound();
    	int g = 0;
    	for (int i : blacklistedSides) {
    		list.setInteger(Integer.toString(g), i);
    		g++;
    	}
    	list.setInteger("size", g);
    	access.setTag("blacklistedSides", list);
    	access.setInteger("disguiseBlock", disguiseBlock.getIdFromBlock(disguiseBlock));
        access.setInteger("disguiseMeta", disguiseMeta);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.isLeaking = access.getBoolean("isLeaking");
    	NBTTagCompound sidesList = access.getCompoundTag("blacklistedSides");
    	int length = sidesList.getInteger("size");
    	Integer[] sidesInt = new Integer[length];
    	for (int i = 0; i < length; i++) {
    		sidesInt[i] = sidesList.getInteger(Integer.toString(i));
    	}
    	this.blacklistedSides = new ArrayList<Integer>(Arrays.asList(sidesInt));
    	this.disguiseBlock = Block.getBlockById(access.getInteger("disguiseBlock"));
    	this.disguiseMeta = access.getInteger("disguiseMeta");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
	
	@Override
    public void readFromNBT(NBTTagCompound access)
    {
        super.readFromNBT(access);
    	NBTTagCompound sidesList = access.getCompoundTag("blacklistedSides");
    	int length = sidesList.getInteger("size");
    	Integer[] sidesInt = new Integer[length];
    	for (int i = 0; i < length; i++) {
    		sidesInt[i] = sidesList.getInteger(Integer.toString(i));
    	}
    	this.blacklistedSides = new ArrayList<Integer>(Arrays.asList(sidesInt));
    	this.disguiseBlock = Block.getBlockById(access.getInteger("disguiseBlock"));
    	this.disguiseMeta = access.getInteger("disguiseMeta");
    }

    @Override
    public void writeToNBT(NBTTagCompound access)
    {
        super.writeToNBT(access);
        NBTTagCompound list = new NBTTagCompound();
    	int g = 0;
    	for (int i : blacklistedSides) {
    		list.setInteger(Integer.toString(g), i);
    		g++;
    	}
    	list.setInteger("size", g);
    	access.setTag("blacklistedSides", list);
    	access.setInteger("disguiseBlock", disguiseBlock.getIdFromBlock(disguiseBlock));
        access.setInteger("disguiseMeta", disguiseMeta);
    }
	    
    public void superUpdate(){
    	super.updateEntity();
    }
	
	@Override
	public void updateEntity() {
		super.updateEntity();

		if (this.worldObj.isRemote) {
			boolean hasWrench = BlockSteamPipeRenderer.updateWrenchStatus();
			if (hasWrench != lastWrench && !(this.disguiseBlock == null || this.disguiseBlock == Blocks.air)) {
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			lastWrench = hasWrench;
		}
		
		
		ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
		for (ForgeDirection direction : ForgeDirection.values()) {
			if (this.doesConnect(direction) && worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) != null) {
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
			while (!doesConnect(direction) || direction == myDirections.get(0)) {
				direction = ForgeDirection.getOrientation((direction.ordinal()+1)%5);
			}
			if (!worldObj.isRemote){
				if (myDirections.size() == 2 && this.getSteamShare() > 0 && i < 10 && (worldObj.isAirBlock(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) || !worldObj.isSideSolid(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ, direction.getOpposite()))) {
					this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:leaking", 2.0F, 0.9F);
					if (!isLeaking){
						////System.out.println("Block is leaking!");
						isLeaking = true;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						markDirty();
					}
					
				} else {
					if (isLeaking){
						////System.out.println("Block is no longer leaking!");
						isLeaking = false;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						markDirty();
					}
				}
				while (myDirections.size() == 2 && this.getPressure() > 0 && i < 10 && (worldObj.isAirBlock(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) || !worldObj.isSideSolid(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ, direction.getOpposite()))) {
					if (worldObj.isRemote){
					}
					this.decrSteam(10);
					
					i++;
				}
			}
			if (worldObj.isRemote && this.isLeaking){
				this.worldObj.spawnParticle("smoke", xCoord+0.5F, yCoord+0.5F, zCoord+0.5F, direction.offsetX*0.1F, direction.offsetY*0.1F, direction.offsetZ*0.1F);
			}
			
		}
		
		
	}
	
	@Override
	public HashSet<ForgeDirection> getConnectionSides() {
		HashSet<ForgeDirection> out = new HashSet();
		HashSet<ForgeDirection> blacklist = new HashSet();
		for (int i : blacklistedSides){
			blacklist.add(ForgeDirection.getOrientation(i));
		}
		for (ForgeDirection d : distributionDirections){
			if (!blacklist.contains(d)){
				out.add(d);
			}
			
		}
		return out;
	}
	
	@Override
	public boolean doesConnect(ForgeDirection face) {
		for (int i : blacklistedSides) {
			if (ForgeDirection.getOrientation(i) == face) {
				return false;
			}
		}
		return true;
	}
	
//	@Override
//	public boolean acceptsGauge(ForgeDirection face) {
//		return true;
//	}

	
	@Override
	public int getSteamShare(){
		if (this.getNetwork() == null){
			this.network = null;
			this.networkName = null;
			return 0;
		} else {
			return (int)Math.floor((double)this.getNetwork().getPressure() * (double)this.capacity);
		}
		
	}
	
	
	
	public MovingObjectPosition rayTrace(World world, Vec3 vec3d, Vec3 vec3d1, MovingObjectPosition fullblock)
	{
		return fullblock;
	}
	  
	private int canConnectSide(int side)
	{
		ForgeDirection direction = ForgeDirection.getOrientation(side);
		if (worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) != null) {
			TileEntity tile = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
			if (tile instanceof ISteamTransporter) {
				ISteamTransporter target = (ISteamTransporter) tile;
				if (target.doesConnect(direction.getOpposite())) {
					return target instanceof TileEntitySteamPipe ? 2 : 1;
				}
				if (target instanceof TileEntitySteamPipe && ((TileEntitySteamPipe) target).blacklistedSides.contains(direction.getOpposite().ordinal())) {
					return 2;
				}
			}
		}
		return 0;
	}
	
	public void addTraceableCuboids(List<IndexedCuboid6> cuboids)
	{
		float min = 4F/16F;
		float max = 12F/16F;
		Block block = worldObj.getBlock(xCoord,yCoord,zCoord);
		if (canConnectSide(0) > 0) {
			float bottom = canConnectSide(0) == 2 ? -5F/16F : 0.0F;
			cuboids.add(new IndexedCuboid6(Integer.valueOf(0), new Cuboid6(this.xCoord + min, this.yCoord + bottom, this.zCoord + min, this.xCoord + max, this.yCoord + 5F/16F, this.zCoord + max)));
	    }
		if (canConnectSide(1) > 0) {
	    	float top = canConnectSide(1) == 2 ? 21F/16F : 1.0F;
	    	cuboids.add(new IndexedCuboid6(Integer.valueOf(1), new Cuboid6(this.xCoord + min, this.yCoord + 11F/16F, this.zCoord + min, this.xCoord + max, this.yCoord + top, this.zCoord + max)));
	    }
		if (canConnectSide(2) > 0) {
			float bottom = canConnectSide(2) == 2 ? -5F/16F : 0.0F;
	    	cuboids.add(new IndexedCuboid6(Integer.valueOf(2), new Cuboid6(this.xCoord + min, this.yCoord + min, this.zCoord + bottom, this.xCoord + max, this.yCoord + max, this.zCoord + 5F/16F)));
	    }
		if (canConnectSide(3) > 0) {
	    	float top = canConnectSide(3) == 2 ? 21F/16F : 1.0F;
	    	cuboids.add(new IndexedCuboid6(Integer.valueOf(3), new Cuboid6(this.xCoord + min, this.yCoord + min, this.zCoord + 11F/16F, this.xCoord + max, this.yCoord + max, this.zCoord + top)));
	    }
		if (canConnectSide(4) > 0) {
			float bottom = canConnectSide(4) == 2 ? -5F/16F : 0.0F;
	    	cuboids.add(new IndexedCuboid6(Integer.valueOf(4), new Cuboid6(this.xCoord + bottom, this.yCoord + min, this.zCoord + min, this.xCoord + 5F/16F, this.yCoord + max, this.zCoord + max)));
	    }
		if (canConnectSide(5) > 0) {
	    	float top = canConnectSide(5) == 2 ? 21F/16F : 1.0F;
	    	cuboids.add(new IndexedCuboid6(Integer.valueOf(5), new Cuboid6(this.xCoord + 11F/16F, this.yCoord + min, this.zCoord + min, this.xCoord + top, this.yCoord + max, this.zCoord + max)));
	    }
	    cuboids.add(new IndexedCuboid6(Integer.valueOf(6), new Cuboid6(this.xCoord + 5F/16F, this.yCoord +  5F/16F, this.zCoord +  5F/16F, this.xCoord + 11F/16F, this.yCoord +  11F/16F, this.zCoord +  11F/16F)));

	}

	@Override
	public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float xO, float yO, float zO) {
		if (player.isSneaking()) {
			if (this.disguiseBlock != null) {
				if (!player.capabilities.isCreativeMode) {
					EntityItem entityItem = new EntityItem(world,player.posX, player.posY, player.posZ, new ItemStack(disguiseBlock,1,disguiseMeta));
					world.spawnEntityInWorld(entityItem);
				}
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), disguiseBlock.stepSound.getBreakSound(), (disguiseBlock.stepSound.getVolume() + 1.0F) / 2.0F, disguiseBlock.stepSound.getPitch() * 0.8F);
				disguiseBlock = null;
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return true;
			}
		}
		else
		{
			if (this.worldObj.isRemote) {
				MovingObjectPosition hit = RayTracer.retraceBlock(world, player, x, y, z);
				//Use ratracer to get the subpart that was hit. The # corresponds with a forge direction.
			    if (hit == null) {
			    	return false;
			    }

			    SteamcraftClientPacketHandler.sendConnectPacket(player, x, y, z, hit);
		    }
		}
	    return false;
	    
	}
	
	public void connectDisconnect(World world,
			int x, int y, int z, int subHit) {
		//Use ratracer to get the subpart that was hit. The # corresponds with a forge direction.
	    //If hit a part from 0 to 5 (direction) and hit me
	    if ((subHit >= 0) && (subHit < 6) && world.getBlock(x,y,z) instanceof BlockPipe)
	    {
	    	//Make sure that you can't make an 'end cap' by allowing less than 2 directions to connect
	    	int sidesConnect = 0;
	    	for (int i = 0; i<6; i++) {
	    		if (this.doesConnect(ForgeDirection.getOrientation(i))) {
	    			sidesConnect++;
	    		}
	    	}
	    	boolean netChange = false;
	    	//If does connect on this side, and has adequate sides left
	    	if (this.doesConnect(ForgeDirection.getOrientation(subHit))) {
	    		ForgeDirection direction = ForgeDirection.getOrientation(subHit);
	    		TileEntity tile = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
	    		if (tile instanceof TileEntitySteamPipe && ((TileEntitySteamPipe) tile).blacklistedSides.contains(direction.getOpposite().ordinal())) {
	    			TileEntitySteamPipe pipe = (TileEntitySteamPipe) tile;
	    			pipe.blacklistedSides.remove((Integer)direction.getOpposite().ordinal());
			    	
			    	//network stuff
	    			
					int steam = pipe.getNetwork().split(pipe, false);
					pipe.shouldJoin();
					pipe.isOtherPipe = true;
					//pipe.getNetwork().addSteam(steam);
					this.worldObj.markBlockForUpdate(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
	    		}
		    	else if (sidesConnect > 2) {
			    	//add to blacklist
		    		this.blacklistedSides.add(subHit);
		    		{
		    			ForgeDirection d = ForgeDirection.getOrientation(subHit);
		    			TileEntity te = worldObj.getTileEntity(x+d.offsetX, y+d.offsetY, z+d.offsetZ);
		    			if (te != null && te instanceof ISteamTransporter){
		    				ISteamTransporter p = (ISteamTransporter)te;
		    				p.getNetwork().shouldRefresh();
		    			}
		    		}
		    		this.isOriginalPipe = true;
		    		//bad network stuff
					int steam = this.getNetwork().split(this, false);
					this.shouldJoin();
					//this.getNetwork().addSteam(steam);
					////System.out.println("B");
					////System.out.println(this.getNetworkName());
					////System.out.println("steam: "+steam+"; nw steam: "+this.getNetwork().getSteam());
					
					refreshNeighbors();
					this.network.shouldRefresh();
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					
		    	}
	    	}
	    	//else if doesn't connect
	    	else if (!this.doesConnect(ForgeDirection.getOrientation(subHit))) {
	    		if (this.blacklistedSides.contains(subHit)) {
	    			//remove from whitelist
		    		this.blacklistedSides.remove((Integer)subHit);			    	
			    	//network stuff
					int steam = this.getNetwork().split(this, false);
					this.shouldJoin();
					//this.getNetwork().addSteam(steam);
					////System.out.println("C");
					////System.out.println(this.getNetworkName());
					////System.out.println("steam: "+steam+"; nw steam: "+this.getNetwork().getSteam());
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	    		}
	    	}
	    	if (this.getSteamShare() > 0){
	    		//world.playSoundEffect(x+0.5F, y+0.5F, z+0.5F, "steamcraft:leaking", 2.0F, 0.9F);
	    		ForgeDirection d = ForgeDirection.getOrientation(subHit);
	    		SteamcraftServerPacketHandler.sendPipeConnectDisconnectPacket(getDimension(), xCoord+0.5F+(d.offsetX/2F), yCoord+0.5F+(d.offsetY/2F), zCoord+0.5F+(d.offsetZ/2F));
		    }
	    	world.playSoundEffect(x+0.5F, y+0.5F, z+0.5F, "steamcraft:wrench", 2.0F, 0.9F);
	    	
	    }
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.isOriginalPipe = false;
		this.isOtherPipe = false;
	}
	
	private void refreshNeighbors(){
		//log.debug("Refreshing neighbors");
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			TileEntity te = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if (te != null && te instanceof ISteamTransporter){
				//log.debug("    Valid");
				ISteamTransporter trans = (ISteamTransporter)te;
				if (trans.getNetwork() != this.getNetwork()){
					//log.debug("     Different network!");
					trans.getNetwork().shouldRefresh();
				} else {
					//log.debug("SameNet");
				}
			}
		}
	}
}
