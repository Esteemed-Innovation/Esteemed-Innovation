package flaxbeard.steamcraft.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.block.BlockRuptureDisc;

public class TileEntityWhistle extends TileEntity {
		
	public void updateEntity(){
		if (getPressure() > 1.05F) {
			this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "mob.ghast.scream", 1.0F, 1.0F);
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
			if (worldObj.isRemote){
				System.out.println(source.getSteam());
			}
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
