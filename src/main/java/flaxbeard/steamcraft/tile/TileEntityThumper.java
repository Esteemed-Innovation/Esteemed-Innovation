package flaxbeard.steamcraft.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntityThumper extends SteamTransporterTileEntity implements ISteamTransporter{
	
	public int progress = 0;
	private boolean isRunning = false;
	
	public TileEntityThumper(){
		super(new ForgeDirection[]{ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST});
		this.addSidesToGaugeBlacklist(ForgeDirection.VALID_DIRECTIONS);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote){
			if (this.isRunning){
				if (progress < 110) {
					progress++;
				} else {
					this.progress = 0;
				}
			} else {
				this.progress = 0;
			}
			
		} else {
			if (this.getSteam() >= 200 && this.progress == 0) {
				if (! this.isRunning){
					this.isRunning = true;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
				this.progress++;
				this.decrSteam(200);

			} else if (progress > 0 && !this.isRunning){
				this.isRunning = true;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			if (this.progress == 15) {
				this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:hiss", Block.soundTypeAnvil.getVolume(), 0.9F);
			}
		
			if (progress > 0 && progress < 110) {
				progress++;
			}
			if (progress >= 110) {
				progress = 0;
		        this.worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.explode", 8.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
	//	        List players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord-4.5F, yCoord-4.5F, zCoord-4.5F, xCoord+5.5F, yCoord+5.5F, zCoord+5.5F));
	//	        for (Object obj : players) {
	//	        	if (obj instanceof EntityPlayer && this.worldObj.isRemote) {
	//	        		EntityPlayer player = (EntityPlayer) obj;
	//		        	player.rotationPitch += (this.worldObj.rand.nextInt(9) - 4)*1F;
	//		        	player.rotationYaw += (this.worldObj.rand.nextInt(9) - 4)*1F;
	//	        	}
	//
	//	        }
		        if (!worldObj.isRemote) {
		        	for (int z = 0; z<4; z++) {
				        boolean hasTarget = false;
				        int i = 0;
				        ChunkCoordinates target = new ChunkCoordinates(xCoord,yCoord-10,zCoord);
				        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
				        ForgeDirection[] moveDirs;
				        ForgeDirection[] moveDirsNotUp;
				        ForgeDirection[] forbiddenDirs;
						if (meta == 1 || meta == 3) {
							ForgeDirection[] moveDirs2 = { ForgeDirection.DOWN, ForgeDirection.EAST,ForgeDirection.EAST,ForgeDirection.EAST, ForgeDirection.EAST, ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.WEST,ForgeDirection.WEST,ForgeDirection.WEST, ForgeDirection.WEST, ForgeDirection.WEST };
							ForgeDirection[] moveDirsNotUp2 = { ForgeDirection.DOWN, ForgeDirection.EAST,ForgeDirection.EAST,ForgeDirection.EAST, ForgeDirection.EAST,ForgeDirection.EAST,ForgeDirection.WEST,ForgeDirection.WEST,ForgeDirection.WEST ,ForgeDirection.WEST ,ForgeDirection.WEST };
							ForgeDirection[] forbiddenDirs2 = { ForgeDirection.NORTH, ForgeDirection.SOUTH };
	
							moveDirs = moveDirs2;
							moveDirsNotUp = moveDirsNotUp2;
							forbiddenDirs = forbiddenDirs2;
						}
						else
						{
							ForgeDirection[] moveDirs2 = { ForgeDirection.DOWN, ForgeDirection.NORTH,ForgeDirection.NORTH,ForgeDirection.NORTH,ForgeDirection.NORTH,ForgeDirection.NORTH, ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.SOUTH, ForgeDirection.SOUTH,ForgeDirection.SOUTH,ForgeDirection.SOUTH };
							ForgeDirection[] moveDirsNotUp2 = { ForgeDirection.DOWN, ForgeDirection.NORTH,ForgeDirection.NORTH,ForgeDirection.NORTH, ForgeDirection.NORTH,ForgeDirection.NORTH,ForgeDirection.SOUTH,ForgeDirection.SOUTH,ForgeDirection.SOUTH, ForgeDirection.SOUTH, ForgeDirection.SOUTH };
							ForgeDirection[] forbiddenDirs2 = { ForgeDirection.EAST, ForgeDirection.WEST };
	
							moveDirs = moveDirs2;
							moveDirsNotUp = moveDirsNotUp2;
							forbiddenDirs = forbiddenDirs2;
						}
				      
				        while (!hasTarget && i < 160) {
				        	if (!worldObj.isAirBlock(target.posX, target.posY, target.posZ) && (worldObj.getBlock(target.posX, target.posY, target.posZ).getBlockHardness(worldObj,target.posX, target.posY, target.posZ) != -1.0F) && !worldObj.canBlockSeeTheSky(target.posX-1, target.posY+1, target.posZ) && !worldObj.canBlockSeeTheSky(target.posX+1, target.posY+1, target.posZ) && !worldObj.canBlockSeeTheSky(target.posX, target.posY+1, target.posZ-1) && !worldObj.canBlockSeeTheSky(target.posX, target.posY+1, target.posZ+1) && !worldObj.canBlockSeeTheSky(target.posX, target.posY+1, target.posZ)) {
				        		hasTarget = true;
				        	}
				        	else
				        	{
				        		if (target.posY < yCoord-3) {
				        			ForgeDirection direction = moveDirs[worldObj.rand.nextInt(moveDirs.length)];
				        			if (this.worldObj.rand.nextInt(50) == 0) {
					        			direction = forbiddenDirs[worldObj.rand.nextInt(forbiddenDirs.length)];
				        			}
				        			target = new ChunkCoordinates(target.posX + direction.offsetX, target.posY + direction.offsetY, target.posZ + direction.offsetZ);
				        		}
				        		else
				        		{
				        			
				        			ForgeDirection direction = moveDirsNotUp[worldObj.rand.nextInt(moveDirsNotUp.length)];
				        			if (this.worldObj.rand.nextInt(50) == 0) {
					        			direction = forbiddenDirs[worldObj.rand.nextInt(forbiddenDirs.length)];
				        			}
				        			target = new ChunkCoordinates(target.posX + direction.offsetX, target.posY + direction.offsetY, target.posZ + direction.offsetZ);
				        		}
				        	}
				        	i++;
				        }
				        if (hasTarget) {
				        	Block block = worldObj.getBlock(target.posX, target.posY, target.posZ);
				        	if (Config.dropItem) {
				        		block.dropBlockAsItem(worldObj, target.posX, target.posY, target.posZ, this.worldObj.getBlockMetadata(target.posX,target.posY,target.posZ), 0);
				        	}
				        	worldObj.setBlockToAir(target.posX, target.posY, target.posZ);
				        }
				        else
				        {
				        }
		        	}
				}
			}
		}
		//this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

	}
	
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
	        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 5, this.zCoord + 1);
	}

	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.progress = par1NBTTagCompound.getShort("progress");

    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("progress",(short) this.progress);

    }
	
	@Override
	public Packet getDescriptionPacket()
	{
        NBTTagCompound access = super.getDescriptionTag();

        access.setInteger("progress", progress);
        access.setBoolean("isRunning", isRunning);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.progress = access.getInteger("progress");
    	this.isRunning = access.getBoolean("isRunning");

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
