package flaxbeard.steamcraft.tile;

import net.minecraftforge.common.util.ForgeDirection;


public class TileEntityCreativeTank extends TileEntitySteamTank {
	public int getCapacity(){
		return 0;
	}
	
	public void updateEntity(){
		super.updateEntity();
		if (this.getNetwork() != null){
			if (this.getNetwork().getPressure() < 1F){
				float toAdd = (1F - this.getNetwork().getPressure()) * 0.1F * this.getNetwork().getCapacity();
				this.insertSteam((int)(Math.floor((double)toAdd)), ForgeDirection.UP);
			}
		}
	}
}
