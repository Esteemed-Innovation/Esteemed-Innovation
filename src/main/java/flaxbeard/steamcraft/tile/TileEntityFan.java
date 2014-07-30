package flaxbeard.steamcraft.tile;

import java.util.List;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntityFan extends SteamTransporterTileEntity implements ISteamTransporter,IWrenchable {
	public boolean active;
	public boolean powered = false;
	public boolean lastSteam = false;
	public int rotateTicks = 0;
	private boolean isInitialized = false;
	
	public TileEntityFan(){
		this.addSidesToGaugeBlacklist(ForgeDirection.VALID_DIRECTIONS);

	}
	
 	@Override
    public void writeToNBT(NBTTagCompound access)
    {
        super.writeToNBT(access);
        access.setBoolean("powered", powered);
    }
 	
	@Override
    public void readFromNBT(NBTTagCompound access)
    {
        super.readFromNBT(access);
        this.powered = access.getBoolean("powered");
    }
	 
	
	@Override
	public Packet getDescriptionPacket()
	{
        NBTTagCompound access = super.getDescriptionTag();
        access.setBoolean("active", this.getSteam() > 0 && !this.powered);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}



    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.active = access.getBoolean("active");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    
    }
	
	@Override
	public void updateEntity() {
		if (lastSteam != this.getSteam() > 0) {
	        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		lastSteam = this.getSteam() > 0;
		if (!isInitialized) {
			this.powered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
			this.setDistributionDirections(new ForgeDirection[] { ForgeDirection.getOrientation( this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)).getOpposite()});
			isInitialized = true;
		}
		super.updateEntity();
		if (active && this.worldObj.isRemote) {
			rotateTicks++;
		}
		if (active && this.worldObj.isRemote || (this.getSteam() > 0 && !this.powered)) {
			if (!this.worldObj.isRemote) {
				this.decrSteam(1);
			}
			int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			ForgeDirection dir = ForgeDirection.getOrientation(meta);
			this.worldObj.spawnParticle("smoke", xCoord+(dir.offsetX == 0 ? Math.random() : 0.5F), yCoord+(dir.offsetY == 0 ? Math.random() : 0.5F), zCoord+(dir.offsetZ == 0 ? Math.random() : 0.5F), dir.offsetX*0.2F, dir.offsetY*0.2F, dir.offsetZ*0.2F);
			int blocksInFront = 0;
			boolean blocked = false;
			for (int i = 1; i<9; i++) {
				int x = xCoord+dir.offsetX*i;
				int y = yCoord+dir.offsetY*i;
				int z = zCoord+dir.offsetZ*i;
				if (!this.worldObj.isRemote && this.worldObj.rand.nextInt(20) == 0 && !blocked && this.worldObj.getBlock(x,y,z) != Blocks.air && this.worldObj.getBlock(x,y,z).isReplaceable(worldObj, x,y,z) || this.worldObj.getBlock(x,y,z) instanceof BlockCrops) {
					int tMeta = this.worldObj.getBlockMetadata(x,y,z);				
					this.worldObj.getBlock(x,y,z).dropBlockAsItem(worldObj, x,y,z, tMeta, 0);
					for (int v = 0; v<5; v++) {
						Steamcraft.instance.proxy.spawnBreakParticles(worldObj, xCoord+dir.offsetX*i+0.5F, yCoord+dir.offsetY*i+0.5F, zCoord+dir.offsetZ*i+0.5F, this.worldObj.getBlock(x,y,z), 0.0F, 0.0F, 0.0F);
					}
					this.worldObj.setBlockToAir(x,y,z);
				}
				if (!blocked && (this.worldObj.getBlock(x,y,z).isReplaceable(worldObj, x,y,z) || this.worldObj.isAirBlock(x,y,z)  || this.worldObj.getBlock(x,y,z) instanceof BlockTrapDoor || this.worldObj.getBlock(x,y,z).getCollisionBoundingBoxFromPool(worldObj, x, y, z) == null || (this.worldObj.getBlock(x,y,z).isSideSolid(worldObj, x, y, z, dir) && this.worldObj.getBlock(x,y,z).isSideSolid(worldObj, x, y, z, dir.getOpposite())))) {
					blocksInFront = i;
					if (i != 8)
						this.worldObj.spawnParticle("smoke", xCoord+dir.offsetX*i+(dir.offsetX == 0 ? Math.random() : 0.5F), yCoord+dir.offsetY*i+(dir.offsetY == 0 ? Math.random() : 0.5F), zCoord+dir.offsetZ*i+(dir.offsetZ == 0 ? Math.random() : 0.5F), dir.offsetX*0.2F, dir.offsetY*0.2F, dir.offsetZ*0.2F);
				}
				else
				{
					blocked = true;
				}
			}
			List entities = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord+(dir.offsetX < 0 ? dir.offsetX * blocksInFront : 0), yCoord+(dir.offsetY < 0 ? dir.offsetY * blocksInFront : 0), zCoord+(dir.offsetZ < 0 ? dir.offsetZ * blocksInFront : 0), xCoord+1+(dir.offsetX > 0 ? dir.offsetX * blocksInFront : 0), yCoord+1+(dir.offsetY > 0 ? dir.offsetY * blocksInFront : 0), zCoord+1+(dir.offsetZ > 0 ? dir.offsetZ * blocksInFront : 0)));
			for (Object obj : entities) {
				Entity entity = (Entity) obj;
				if (!(entity instanceof EntityPlayer) || !(((EntityPlayer)entity).capabilities.isFlying && ((EntityPlayer)entity).capabilities.isCreativeMode)) {
					if (entity instanceof EntityPlayer && ((EntityPlayer)entity).isSneaking()) {
						entity.motionX += dir.offsetX * 0.025F;
						entity.motionY += dir.offsetY * 0.05F;
						entity.motionZ += dir.offsetZ * 0.025F;
					}
					else
					{
						entity.motionX += dir.offsetX * 0.075F;
						entity.motionY += dir.offsetY * 0.1F;
						entity.motionZ += dir.offsetZ * 0.075F;
					}
					entity.fallDistance = 0.0F;
				}
			}
		}
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
		this.isInitialized = false;
		networkName = null;
		network = null;
		this.refresh();
		SteamNetwork.newOrJoin(this);
		return false;
	}
}
