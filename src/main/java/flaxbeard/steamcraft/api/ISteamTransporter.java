package flaxbeard.steamcraft.api;

import java.util.HashSet;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface ISteamTransporter {
	public int getLastSteam();
	public float getPressure();
	public float getPressureResistance();
	public boolean canInsert(ForgeDirection face);
	public int getCapacity();
	public int getSteam();
	public void explode();
	public void insertSteam(int amount, ForgeDirection face);
	public void decrSteam(int i);
	public boolean doesConnect(ForgeDirection face);
	public abstract boolean acceptsGauge(ForgeDirection face);
	public HashSet<ForgeDirection> getConnectionSides();
	public World getWorldObj();
	public Tuple3<Integer, Integer, Integer> getCoords();
}
