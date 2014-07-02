package flaxbeard.steamcraft.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import flaxbeard.steamcraft.Steamcraft;

public class UtilSteamTransport {
	public static void generalPressureEvent(World world, int x, int y, int z, float pressure, int capacity) {
		if (pressure > 1.2F) {
			ISteamTransporter trans = (ISteamTransporter) world.getTileEntity(x, y, z);
			float resistance = trans.pressureResistance;
			int steam = trans.getSteam();
			int oneInX = Math.max(1, (int)Math.floor((double)(500.0F  - (pressure / (1.1F + resistance) * 100)) ));
			//System.out.println(steam + "/" + capacity +" = " +(((float)steam) / ((float)capacity)));
			System.out.println("100 - (" + pressure + " / (1.1F + "+resistance+") *100 )" + "chance of explosion: 1 in "+oneInX);
			if (oneInX <= 1 ||  world.rand.nextInt(oneInX - 1) == 0) {
				System.out.println("boom!");
				trans.explode();
				world.createExplosion(null, x+0.5F, y+0.5F, z+0.5F, 4.0F, true);
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
						float targetpercent = ((float)trans.getSteam()+target.getSteam())/((float)trans.getCapacity()+target.getCapacity());
						int change = (int) (Math.floor(trans.getSteam()*target.getCapacity()-target.getSteam()*trans.getCapacity())/(trans.getCapacity()+target.getCapacity()));
						if (change > 0 && change <= trans.getSteam()) {
							trans.decrSteam(change);
							target.insertSteam(change, direction.getOpposite());
						}
					}
				}
				else if (tile instanceof IFluidHandler && Steamcraft.steamRegistered) {
					IFluidHandler target = (IFluidHandler) tile;
					float cap = 0;
					float steam = 0;
					if (target.getTankInfo(direction.getOpposite()) != null) {
						for (FluidTankInfo info : target.getTankInfo(direction.getOpposite())) {
							if (info.fluid == null) {
								cap += info.capacity/10.0F;
							}
							else if (info.fluid.getFluid() == FluidRegistry.getFluid("steam")) {
								steam += info.fluid.amount/10.0F;
								cap += info.capacity/10.0F;
							}
						}
					}
					float pressure = (float)steam/(float)cap;
					if (target.canFill(direction.getOpposite(), FluidRegistry.getFluid("steam")) && trans.getPressure() > pressure) {
						float targetpercent = ((float)trans.getSteam()+steam)/((float)trans.getCapacity()+cap);
						int change = (int) (Math.floor(trans.getSteam()*cap-steam*trans.getCapacity())/(trans.getCapacity()+cap));
						if (change > 0 && change <= trans.getSteam()) {
							trans.decrSteam(change-target.fill(direction.getOpposite(), new FluidStack(FluidRegistry.getFluid("steam"), change*10), true)/10);
						}
					}
				}
			}
		}
	}
	
	public static void preExplosion(World worldObj, int xCoord,
			int yCoord, int zCoord, ForgeDirection[] values) {
		ISteamTransporter trans = (ISteamTransporter) worldObj.getTileEntity(xCoord, yCoord, zCoord);
		for (ForgeDirection direction : values) {
			if (worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ) != null) {
				TileEntity tile = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
				if (tile instanceof ISteamTransporter) {
					ISteamTransporter target = (ISteamTransporter) tile;
					int change = (int)(-10.0F * ((float)target.getCapacity() / 100.0F));
					trans.decrSteam(change);
				}
			}
		}
	}
}
