package flaxbeard.steamcraft.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.block.BlockRuptureDisc;

public class TileEntityRuptureDisc extends TileEntity {
	
	public void updateEntity(){
		if (getPressure() > 1.1F) {
			if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) < 6) {
				this.worldObj.createExplosion(null, xCoord+0.5F, yCoord+0.5F, zCoord+0.5F, 0.0F, true);
				this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) + 10, 2);
			}

		}
		if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 9) {
			int i = 0;
			if (this.getSteam() > 0) {
				this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:leaking", 2.0F, 0.9F);
			}
			while (this.getSteam() > 0 && i < 10) {
				this.drainSteam(1);
				float offset = 10.0F/16.0F;
				float xOffset = myDir().getOpposite().offsetX * offset;
				float yOffset = myDir().getOpposite().offsetY * offset;
				float zOffset = myDir().getOpposite().offsetZ * offset;
	
				this.worldObj.spawnParticle("smoke", xCoord+0.5F+xOffset, yCoord+0.5F+yOffset, zCoord+0.5F+zOffset, myDir().offsetX*0.1F, myDir().offsetY*0.1F, myDir().offsetZ*0.1F);
				i++;
			}
		}
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
			return source.getSteam();
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
	
	
}
