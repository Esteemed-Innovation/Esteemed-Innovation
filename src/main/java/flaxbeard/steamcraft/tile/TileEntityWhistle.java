package flaxbeard.steamcraft.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.block.BlockRuptureDisc;
import flaxbeard.steamcraft.client.audio.HornSound;

public class TileEntityWhistle extends TileEntity {
	
	private float volume = 0F;
	private boolean isSoundRegistered = false;
	private boolean isSounding = false;
	private int steamTick = 0;
	private boolean isReallyDead = false;
	@SideOnly(Side.CLIENT)
	private HornSound mySound;
	
	@Override
	public Packet getDescriptionPacket()
	{
		super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setBoolean("isSounding", isSounding);
        
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}


	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.isSounding = access.getBoolean("isSounding");
    	
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	
	public void updateEntity(){
		if (worldObj.isRemote) {
			this.updateSound();
		}
		else
		{
			if (getPressure() > 1.02F){
				if (!this.isSounding){
					this.isSounding = true;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			} else {
				if (this.isSounding){
					this.isSounding = false;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
		}
		
		
	}
	
	@SideOnly(Side.CLIENT)
	private void updateSound() {
		if (!isSoundRegistered){
			if (worldObj.isRemote){
				mySound = new HornSound(this);
				Minecraft.getMinecraft().getSoundHandler().playSound(mySound);
			}
			isSoundRegistered = true;
		}
		
		if (this.isSounding){
			if (steamTick == 0){
				ForgeDirection d = myDir().getOpposite();
				ISteamTransporter source = null;
				TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord+ d.offsetZ);
				float offset = 2.0F/16.0F;
				if (te != null && te instanceof TileEntitySteamPipe){
					offset = 6.0F/16.0F;
				}
				float offset2 = (2.0F/16.0F/3.0F);

				float xOffset = myDir().getOpposite().offsetX * offset;
				float zOffset = myDir().getOpposite().offsetZ * offset;
				float xOffset2 = myDir().getOpposite().offsetX * offset2;
				float zOffset2 = myDir().getOpposite().offsetZ * offset2;
				worldObj.spawnParticle("smoke", xCoord+0.5D+xOffset, yCoord+0.7D, zCoord+0.5D+zOffset, 0f-xOffset2, 0.05f, 0f-zOffset2);
			}
			steamTick++;
			if (steamTick >= 4){
				this.steamTick = 0;
			}

			if (volume < 0.75F){
				volume += 0.01F;
			}
		} else if (volume > 0F){
			volume -= 0.25F;
		} else {
			volume = 0F;
		}
		//volume = 0f;
		
	}


	private void drainSteam(int s) {
		ForgeDirection d = myDir().getOpposite();
		ISteamTransporter source = null;
		TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord+ d.offsetZ);
		if (te != null && te instanceof ISteamTransporter){
			source = (ISteamTransporter) te;
			source.decrSteam(s);
		}
	}
	
	private int getSteam(){
		ForgeDirection d = myDir().getOpposite();
		ISteamTransporter source = null;
		TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord+ d.offsetZ);
		if (te != null && te instanceof ISteamTransporter){
			source = (ISteamTransporter) te;
			if (worldObj.isRemote){
				////Steamcraft.log.debug(source.getSteam());
			}
			return source.getSteamShare();
		}
		return 0;
	}
	
	private float getPressure(){
		ForgeDirection d = myDir().getOpposite();
		ISteamTransporter source = null;
		TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord+ d.offsetZ);
		if (te != null && te instanceof ISteamTransporter){
			source = (ISteamTransporter) te;
			return source.getPressure();
		}
		return 0.0F;
	}

	private ForgeDirection myDir(){
		return ForgeDirection.getOrientation(BlockRuptureDisc.getMeta(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)));
	}
	
	public float getVolume(){
		return volume;
	}
	
}
