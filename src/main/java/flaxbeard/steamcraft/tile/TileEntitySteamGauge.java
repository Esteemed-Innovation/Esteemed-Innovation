package flaxbeard.steamcraft.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;

public class TileEntitySteamGauge extends TileEntity {

	private int lastCompOutput = 0;
	private ForgeDirection[] cardinals = new ForgeDirection[]{
			ForgeDirection.NORTH,
			ForgeDirection.SOUTH,
			ForgeDirection.EAST,
			ForgeDirection.WEST
	};
	
	public void updateEntity(){
		if (!worldObj.isRemote){
			int compOutput = getComparatorOutput(); 
			if (compOutput != lastCompOutput){
				lastCompOutput = compOutput;
				markDirty();
//				for (int i = 0; i <= 3; i++){
//					if (cardinals[i] != myDir()){
//						ForgeDirection d = cardinals[i];
//						worldObj.getBlock(xCoord, yCoord, zCoord);
//						worldObj.markBlockForUpdate(xCoord + d.offsetX, yCoord, zCoord + d.offsetZ);
//					}
//				}
			}
		}
	}
	
	public float getPressure(){
		ForgeDirection d = myDir().getOpposite();
		ISteamTransporter source = null;
		TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord+ d.offsetZ);
		if (te != null && te instanceof ISteamTransporter){
			if (te instanceof TileEntitySteamCharger){
				TileEntitySteamCharger charger = (TileEntitySteamCharger) te;
				return charger.getSteamInItem();
			} else {
				source = (ISteamTransporter) te;
				return source.getPressure();
			}
			
		}
		return 0.0F;
	}
	
	public int getComparatorOutput(){
		int out = (int)(15 * (100*((double)getPressure() * 0.01D) ));
		////Steamcraft.log.debug(out);
		return out;
	}
	
	private ForgeDirection myDir(){
		return ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
	}
	
	
}
