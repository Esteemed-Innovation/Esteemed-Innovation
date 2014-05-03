package flaxbeard.steamcraft.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class UtilSteamTransport {
	public static void generalPressureEvent(World world, int x, int y, int z, float pressure, int capacity) {
		if (pressure > 1.1F) {
			if (world.rand.nextInt(1) == 0) {
				//world.createExplosion(null, x+0.5F, y+0.5F, z+0.5F, 2.0F, true);
			}
		}
	}

	public static void generalDistributionEvent(World worldObj, int xCoord,
			int yCoord, int zCoord, ForgeDirection[] values) {
		ISteamTransporter trans = (ISteamTransporter) worldObj.getTileEntity(xCoord, yCoord, zCoord);
		for (ForgeDirection direction : values) {
			if (worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) != null) {
				TileEntity tile = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
				if (tile instanceof ISteamTransporter) {
					ISteamTransporter target = (ISteamTransporter) tile;
					if (trans.getPressure() > target.getPressure() && target.canInsert(direction.getOpposite())) {
						//System.out.println("UNS");
						float targetpercent = ((float)trans.getSteam()+target.getSteam())/((float)trans.getCapacity()+target.getCapacity());
						//System.out.println(targetpercent);
						int change = (int) (Math.floor(trans.getSteam()*target.getCapacity()-target.getSteam()*trans.getCapacity())/(trans.getCapacity()+target.getCapacity()));
						if (change > 0 && change <= trans.getSteam()) {
							trans.decrSteam(change);
							target.insertSteam(change, direction.getOpposite());
						}
					}
				}
			}
		}
	}
}
