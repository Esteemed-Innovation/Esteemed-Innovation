package flaxbeard.steamcraft.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;

public class TileEntitySteamPipe extends TileEntity implements ISteamTransporter {
	
	private int steam;
	private int capacity = 10;

	@Override
	public float getPressure() {
		return this.getSteam()/this.getCapacity();
	}

	@Override
	public boolean canInsert(ForgeDirection face) {
		return true;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public int getSteam() {
		return steam;
	}

	@Override
	public void insertSteam(int amount, ForgeDirection face) {
		this.steam += amount;
	}
	
	@Override
	public void updateEntity() {
		UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,ForgeDirection.values());
		UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
	}

	@Override
	public void decrSteam(int i) {
		this.steam -= i;
	}

}
