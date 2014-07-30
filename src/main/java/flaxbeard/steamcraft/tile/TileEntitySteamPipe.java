package flaxbeard.steamcraft.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
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
import scala.actors.threadpool.Arrays;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.codechicken.lib.raytracer.IndexedCuboid6;
import flaxbeard.steamcraft.codechicken.lib.raytracer.RayTracer;
import flaxbeard.steamcraft.codechicken.lib.vec.Cuboid6;

public class TileEntitySteamPipe extends SteamTransporterTileEntity implements ISteamTransporter,IWrenchable {
	//protected FluidTank dummyFluidTank = FluidRegistry.isFluidRegistered("steam") ? new FluidTank(new FluidStack(FluidRegistry.getFluid("steam"), 0),10000) : null;
	public ArrayList<Integer> blacklistedSides = new ArrayList<Integer>();
	
	protected boolean isLeaking = false;
	
	public TileEntitySteamPipe(){
		super(ForgeDirection.values());
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
    	for (int i : blacklistedSides) {
    		System.out.println(i);
    	}
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
    	//this.blacklistedSides.clear();
    	this.blacklistedSides = new ArrayList<Integer>(Arrays.asList(sidesInt));
    }

    @Override
    public void writeToNBT(NBTTagCompound access)
    {
        super.writeToNBT(access);
    	NBTTagList list = new NBTTagList();
    	for (int i : blacklistedSides) {
    		list.appendTag(new NBTTagInt(i));
    	}
    	access.setTag("blacklistedSides", list);
    }
	    
    public void superUpdate(){
    	super.updateEntity();
    }
	
	@Override
	public void updateEntity() {
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
						//System.out.println("I AM THE CLIENT!");
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
	public int getSteam(){
		return this.getNetwork().getSteam();
	}
	
	
	
	public MovingObjectPosition rayTrace(World world, Vec3 vec3d, Vec3 vec3d1, MovingObjectPosition fullblock)
	{
		return fullblock;
	}
	  
	private boolean canConnectSide(int side)
	{
		ForgeDirection direction = ForgeDirection.getOrientation(side);
		if (worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) != null) {
			TileEntity tile = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
			if (tile instanceof ISteamTransporter) {
				ISteamTransporter target = (ISteamTransporter) tile;
				if (target.doesConnect(direction.getOpposite())) {
					return true;
				}
			}
		}
		return false;
	}
	  
	public void addTraceableCuboids(List<IndexedCuboid6> cuboids)
	{
		float min = 4F/16F;
		float max = 12F/16F;
		
		if (canConnectSide(0)) {
	      cuboids.add(new IndexedCuboid6(Integer.valueOf(0), new Cuboid6(this.xCoord + min, this.yCoord, this.zCoord + min, this.xCoord + max, this.yCoord + 5F/16F, this.zCoord + max)));
	    }
	    if (canConnectSide(1)) {
	      cuboids.add(new IndexedCuboid6(Integer.valueOf(1), new Cuboid6(this.xCoord + min, this.yCoord + 11F/16F, this.zCoord + min, this.xCoord + max, this.yCoord + 1, this.zCoord + max)));
	    }
	    if (canConnectSide(2)) {
	      cuboids.add(new IndexedCuboid6(Integer.valueOf(2), new Cuboid6(this.xCoord + min, this.yCoord + min, this.zCoord, this.xCoord + max, this.yCoord + max, this.zCoord + 5F/16F)));
	    }
	    if (canConnectSide(3)) {
	      cuboids.add(new IndexedCuboid6(Integer.valueOf(3), new Cuboid6(this.xCoord + min, this.yCoord + min, this.zCoord + 11F/16F, this.xCoord + max, this.yCoord + max, this.zCoord + 1)));
	    }
	    if (canConnectSide(4)) {
	      cuboids.add(new IndexedCuboid6(Integer.valueOf(4), new Cuboid6(this.xCoord, this.yCoord + min, this.zCoord + min, this.xCoord + 5F/16F, this.yCoord + max, this.zCoord + max)));
	    }
	    if (canConnectSide(5)) {
	      cuboids.add(new IndexedCuboid6(Integer.valueOf(5), new Cuboid6(this.xCoord + 11F/16F, this.yCoord + min, this.zCoord + min, this.xCoord + 1, this.yCoord + max, this.zCoord + max)));
	    }
	    cuboids.add(new IndexedCuboid6(Integer.valueOf(6), new Cuboid6(this.xCoord + 5F/16F, this.yCoord +  5F/16F, this.zCoord +  5F/16F, this.xCoord + 11F/16F, this.yCoord +  11F/16F, this.zCoord +  11F/16F)));
	}

	@Override
	public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float xO, float yO, float zO) {
		MovingObjectPosition hit = RayTracer.retraceBlock(world, player, x, y, z);
	    if (hit == null) {
	    	return false;
	    }
	    if ((hit.subHit >= 0) && (hit.subHit < 6) && world.getBlock(hit.blockX, hit.blockY, hit.blockZ) instanceof BlockPipe)
	    {
	    	int sidesConnect = 0;
	    	for (int i = 0; i<6; i++) {
	    		if (this.doesConnect(ForgeDirection.getOrientation(i))) {
	    			sidesConnect++;
	    		}
	    	}
	    	if (sidesConnect > 2 && this.doesConnect(ForgeDirection.getOrientation(hit.subHit))) {
		    	player.swingItem();
	    		this.blacklistedSides.add(hit.subHit);
				int steam = this.getSteam();
				this.getNetwork().split(this);
				SteamNetwork.newOrJoin(this);
				this.getNetwork().addSteam(steam);
				System.out.println(hit.subHit);
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

	    	}
	    	else if (!this.doesConnect(ForgeDirection.getOrientation(hit.subHit))) {
	    		if (this.blacklistedSides.contains(hit.subHit)) {
		    		this.blacklistedSides.remove((Integer)hit.subHit);
			    	player.swingItem();
					int steam = this.getSteam();
					this.getNetwork().split(this);
					SteamNetwork.newOrJoin(this);
					this.getNetwork().addSteam(steam);
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	    		}
	    	}

	      	return true;
	    }
	    return false;
	}
}
