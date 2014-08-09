package flaxbeard.steamcraft.tile;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchDisplay;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntityVacuum extends SteamTransporterTileEntity implements ISteamTransporter,IWrenchable,IWrenchDisplay {
	
	public boolean active;
	public boolean powered = false;
	public boolean lastSteam = false;
	public int rotateTicks = 0;
	private boolean isInitialized = false;
	private static int steamUsage = 3;
	public int range = 9;
	
	
	static public boolean isLyingInCone(float[] x, float[] t, float[] b, 
            float aperture){

	// This is for our convenience
	float halfAperture = aperture/2.f;
	
	// Vector pointing to X point from apex
	float[] apexToXVect = dif(t,x);
	
	// Vector pointing from apex to circle-center point.
	float[] axisVect = dif(t,b);
	
	// X is lying in cone only if it's lying in 
	// infinite version of its cone -- that is, 
	// not limited by "round basement".
	// We'll use dotProd() to 
	// determine angle between apexToXVect and axis.
	boolean isInInfiniteCone = dotProd(apexToXVect,axisVect)
	       /magn(apexToXVect)/magn(axisVect)
	         >
	       // We can safely compare cos() of angles 
	       // between vectors instead of bare angles.
	       Math.cos(halfAperture);
	
	
	if(!isInInfiniteCone) return false;
	
	// X is contained in cone only if projection of apexToXVect to axis
	// is shorter than axis. 
	// We'll use dotProd() to figure projection length.
	boolean isUnderRoundCap = dotProd(apexToXVect,axisVect)
	      /magn(axisVect)
	        <
	      magn(axisVect);
	return isUnderRoundCap;
	}
	
	static public float dotProd(float[] a, float[] b){
	    return a[0]*b[0]+a[1]*b[1]+a[2]*b[2];
	}

	static public float[] dif(float[] a, float[] b){
	    return (new float[]{
	            a[0]-b[0],
	            a[1]-b[1],
	            a[2]-b[2]
	    });
	}

	static public float magn(float[] a){
	    return (float) (Math.sqrt(a[0]*a[0]+a[1]*a[1]+a[2]*a[2]));
	}
	
	@Override
	public void updateEntity() {
		if (lastSteam != this.getSteamShare() > steamUsage) {
	        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		lastSteam = this.getSteamShare() > steamUsage;
		if (!isInitialized) {
			this.powered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
			ForgeDirection myDir = ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
			ForgeDirection[] directions = new ForgeDirection[5];
			int i = 0;
			for (ForgeDirection direction : ForgeDirection.values()) {
				if (direction != myDir && direction != myDir.getOpposite()) {
					directions[i] = direction;
					i++;
				}
			}
			this.setDistributionDirections(directions);
			isInitialized = true;
		}
		super.updateEntity();
		if (active && this.worldObj.isRemote) {
			rotateTicks++;
		}
		if (active && this.worldObj.isRemote || (this.getSteamShare() > steamUsage && !this.powered)) {
			if (!this.worldObj.isRemote) {
				this.decrSteam(3);
			}
			int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			ForgeDirection dir = ForgeDirection.getOrientation(meta);
			float[] M = { this.xCoord+0.5F,this.yCoord+0.5F,this.zCoord+0.5F };
			float[] N = { this.xCoord+0.5F+range*dir.offsetX,this.yCoord+0.5F+range*dir.offsetY,this.zCoord+0.5F+range*dir.offsetZ };
			float theta = (float)Math.PI/2.0F; //half angle of cone
			//List entities = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord+(dir.offsetX < 0 ? dir.offsetX * blocksInFront : 0), yCoord+(dir.offsetY < 0 ? dir.offsetY * blocksInFront : 0), zCoord+(dir.offsetZ < 0 ? dir.offsetZ * blocksInFront : 0), xCoord+1+(dir.offsetX > 0 ? dir.offsetX * blocksInFront : 0), yCoord+1+(dir.offsetY > 0 ? dir.offsetY * blocksInFront : 0), zCoord+1+(dir.offsetZ > 0 ? dir.offsetZ * blocksInFront : 0)));
			List entities = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord-20, yCoord-20, zCoord-20, xCoord+20, yCoord+20, zCoord+20));
			for (int i = 0; i<200; i++) {
				float[] X = { (worldObj.rand.nextFloat()*40.0F)-20.0F+xCoord,(worldObj.rand.nextFloat()*40.0F)-20.0F+yCoord,(worldObj.rand.nextFloat()*40.0F)-20.0F+zCoord};
				if (isLyingInCone(X,M,N,theta) && this.worldObj.rayTraceBlocks(Vec3.createVectorHelper(X[0],X[1],X[2]), Vec3.createVectorHelper(this.xCoord+0.5F+dir.offsetX, this.yCoord+0.5F+dir.offsetY, this.zCoord+0.5F+dir.offsetZ)) == null) {
					Vec3 vec = Vec3.createVectorHelper(X[0]-M[0], X[1]-M[1], X[2]-M[2]);
					vec = vec.normalize();
					this.worldObj.spawnParticle("smoke",X[0],X[1],X[2],-vec.xCoord*0.5F, -vec.yCoord*0.5F, -vec.zCoord*0.5F);
				}
			}
			
			for (Object obj : entities) {
				Entity entity = (Entity) obj;
				float[] X = { (float) entity.posX, (float) entity.posY, (float) entity.posZ };
				
				
				if (isLyingInCone(X,M,N,theta) && this.worldObj.rayTraceBlocks(Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ), Vec3.createVectorHelper(this.xCoord+0.5F+dir.offsetX, this.yCoord+0.5F+dir.offsetY, this.zCoord+0.5F+dir.offsetZ)) == null) {
					if (!(entity instanceof EntityPlayer) || !(((EntityPlayer)entity).capabilities.isFlying && ((EntityPlayer)entity).capabilities.isCreativeMode)) {
						Vec3 vec = Vec3.createVectorHelper(X[0]-M[0], X[1]-M[1], X[2]-M[2]);
						vec = vec.normalize();
						vec.yCoord *= 1;
						if (entity.isSneaking()) {
							vec.xCoord *= 0.25F;
							vec.yCoord *= 0.25F;
							vec.zCoord *= 0.25F;
						}
						entity.motionX -= vec.xCoord * 0.025F;
						entity.motionY -= vec.yCoord * 0.05F;
						entity.motionZ -= vec.zCoord * 0.025F;
						
						entity.fallDistance = 0.0F;
					}
				}
				
			}
	
	        List list = worldObj.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord + dir.offsetX * 0.25F, yCoord + dir.offsetY * 0.25F, zCoord + dir.offsetZ * 0.25F, xCoord + 1.0D + dir.offsetX * 0.25F, yCoord + 1.0D + dir.offsetY * 0.25F, zCoord + 1.0D + dir.offsetZ * 0.25F), IEntitySelector.selectAnything);
	        if (list.size() > 0) {
	        	EntityItem item = (EntityItem) list.get(0);
	        	if (this.worldObj.getTileEntity(xCoord-dir.offsetX, yCoord-dir.offsetY, zCoord-dir.offsetZ) != null && this.worldObj.getTileEntity(xCoord-dir.offsetX, yCoord-dir.offsetY, zCoord-dir.offsetZ) instanceof ISidedInventory) {
	    			ISidedInventory inv = (ISidedInventory) this.worldObj.getTileEntity(xCoord-dir.offsetX, yCoord-dir.offsetY, zCoord-dir.offsetZ);
	    			int[] access = inv.getAccessibleSlotsFromSide(dir.getOpposite().flag);
	    			for (int j = 0; j < access.length; j++) {
	    				int i = access[j];
	    				ItemStack checkStack1 = null;
	    				ItemStack checkStack2 = null;
	    				if (inv.getStackInSlot(i) != null) {
		    				checkStack1 = inv.getStackInSlot(i).copy();
		    				checkStack1.stackSize = 1;
		    				checkStack2 = item.getEntityItem().copy();
		    				checkStack2.stackSize = 1;	
	    				}
	    				if ((inv.getStackInSlot(i) == null || (item.getEntityItem().areItemStacksEqual(checkStack1, checkStack2) && inv.getStackInSlot(i).stackSize < inv.getStackInSlot(i).getMaxStackSize())) && inv.isItemValidForSlot(i, item.getEntityItem()) && inv.canInsertItem(i, item.getEntityItem(), dir.getOpposite().flag)) {
	    					ItemStack stack = item.getEntityItem().copy();
	    					boolean setDead = true;
	    					if (inv.getStackInSlot(i) != null) {
	    						
	    						if ((inv.getStackInSlot(i).stackSize + stack.stackSize) > stack.getMaxStackSize()) {
	    							setDead = false;
	    							int total = inv.getStackInSlot(i).stackSize + stack.stackSize;
	    							stack.stackSize = stack.getMaxStackSize();
	    							total -= stack.getMaxStackSize();
	    							checkStack2.stackSize = total;
	    							item.setEntityItemStack(checkStack2);
	    							//item.getEntityItem().stackSize = (inv.getStackInSlot(i).stackSize + stack.stackSize - stack.getMaxStackSize());
	    						}
	    						else
	    						{
	    							stack.stackSize = inv.getStackInSlot(i).stackSize + item.getEntityItem().stackSize;
	    						}
	    					}
	    					inv.setInventorySlotContents(i, stack);
	    					if (setDead) {
	    						item.setDead();
	    					}
	    					break;
	    				}
	    			}
	        	}
	        	 else if (this.worldObj.getTileEntity(xCoord-dir.offsetX, yCoord-dir.offsetY, zCoord-dir.offsetZ) != null && this.worldObj.getTileEntity(xCoord-dir.offsetX, yCoord-dir.offsetY, zCoord-dir.offsetZ) instanceof IInventory) {
	    			IInventory inv = (IInventory) this.worldObj.getTileEntity(xCoord-dir.offsetX, yCoord-dir.offsetY, zCoord-dir.offsetZ);
	    			for (int i = 0; i < inv.getSizeInventory(); i++) {
	    				ItemStack checkStack1 = null;
	    				ItemStack checkStack2 = null;
	    				if (inv.getStackInSlot(i) != null) {
		    				checkStack1 = inv.getStackInSlot(i).copy();
		    				checkStack1.stackSize = 1;
		    				checkStack2 = item.getEntityItem().copy();
		    				checkStack2.stackSize = 1;	
	    				}
	    				if ((inv.getStackInSlot(i) == null || (item.getEntityItem().areItemStacksEqual(checkStack1, checkStack2) && inv.getStackInSlot(i).stackSize < inv.getStackInSlot(i).getMaxStackSize())) && inv.isItemValidForSlot(i, item.getEntityItem())) {
	    					ItemStack stack = item.getEntityItem().copy();
	    					boolean setDead = true;
	    					if (inv.getStackInSlot(i) != null) {
	    						
	    						if ((inv.getStackInSlot(i).stackSize + stack.stackSize) > stack.getMaxStackSize()) {
	    							setDead = false;
	    							int total = inv.getStackInSlot(i).stackSize + stack.stackSize;
	    							stack.stackSize = stack.getMaxStackSize();
	    							total -= stack.getMaxStackSize();
	    							checkStack2.stackSize = total;
	    							item.setEntityItemStack(checkStack2);
	    							//item.getEntityItem().stackSize = (inv.getStackInSlot(i).stackSize + stack.stackSize - stack.getMaxStackSize());
	    						}
	    						else
	    						{
	    							stack.stackSize = inv.getStackInSlot(i).stackSize + item.getEntityItem().stackSize;
	    						}
	    					}
	    					inv.setInventorySlotContents(i, stack);
	    					if (setDead) {
	    						item.setDead();
	    					}
	    					break;
	    				}
	    			}
	    		}
        	}
		}
	}
	
 	
 	@Override
    public void writeToNBT(NBTTagCompound access)
    {
        super.writeToNBT(access);
        access.setBoolean("powered", powered);
        access.setShort("range", (short) this.range);
    }
 	
	@Override
    public void readFromNBT(NBTTagCompound access)
    {
        super.readFromNBT(access);
        this.powered = access.getBoolean("powered");
        this.range = access.getShort("range");
        
    }
	 
	
	@Override
	public Packet getDescriptionPacket()
	{
        NBTTagCompound access = super.getDescriptionTag();
        access.setBoolean("active", this.getSteamShare() > 0 && !this.powered);
        access.setShort("range", (short) this.range);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}



    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.active = access.getBoolean("active");
        this.range = access.getShort("range");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    
    }
	
	
	public void updateRedstoneState(boolean flag) {
		if (flag != powered) {
			this.powered = flag;
	        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	@Override
	public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float xO, float yO, float zO) {
		if (player.isSneaking()) {
			switch (range) {
			case 9:
				range = 11;
				break;
			case 11:
				range = 13;
				break;
			case 13:
				range = 15;
				break;
			case 15:
				range = 17;
				break;
			case 17:
				range = 19;
				break;
			case 19:
				range = 5;
				break;
			case 5:
				range = 7;
				break;
			case 7:
				range = 9;
				break;
			}
			//Steamcraft.log.debug(range);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return true;
		}
		else
		{
			int steam = this.getSteamShare();
	
			this.getNetwork().split(this, true);
			ForgeDirection myDir = ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
			ForgeDirection[] directions = new ForgeDirection[5];
			int i = 0;
			for (ForgeDirection direction : ForgeDirection.values()) {
				if (direction != myDir && direction != myDir.getOpposite()) {
					directions[i] = direction;
					i++;
				}
			}
			this.setDistributionDirections(directions);
			
			SteamNetwork.newOrJoin(this);
			this.getNetwork().addSteam(steam);
			return false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void displayWrench(Post event) {
		GL11.glPushMatrix();
		int color = Minecraft.getMinecraft().thePlayer.isSneaking() ? 0xC6C6C6 : 0x777777;
		int x = event.resolution.getScaledWidth() / 2  -8;
		int y = event.resolution.getScaledHeight() / 2  - 8;
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("steamcraft.fan.range") + " " + this.range + " " + StatCollector.translateToLocal("steamcraft.fan.blocks"), x + 15, y + 13, color);
		GL11.glPopMatrix();
	}
}
