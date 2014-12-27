package flaxbeard.steamcraft.api;

import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.util.Coord4;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashSet;

public interface ISteamTransporter {

    /**
     * The pressure of the device
     */
    public float getPressure();

    /**
     * How resistant the device is to pressure meltdowns
     */
    public float getPressureResistance();

    /**
     * Called to ensure that the device can have steam put into it
     *
     * @param face - The side of the device
     *
     * @return true if steam can be inserted
     */
    public boolean canInsert(ForgeDirection face);

    /**
     * How much steam the device can store
     */
    public int getCapacity();

    public int getSteamShare();

    public void explode();

    /**
     * @param amount - How much steam can be inserted per
     * @param face   - The side of the device
     */
    public void insertSteam(int amount, ForgeDirection face);

    public void decrSteam(int i);

    public boolean doesConnect(ForgeDirection face);

    /**
     * Called to ensure that the device can have a steam gauge put on it to check how much steam it has
     *
     * @param face - The side of the device
     *
     * @return true if steam gauges can be put on it
     */
    public abstract boolean acceptsGauge(ForgeDirection face);

    public HashSet<ForgeDirection> getConnectionSides();

    public World getWorld();

    public String getNetworkName();

    public void setNetworkName(String name);

    public SteamNetwork getNetwork();

    public void setNetwork(SteamNetwork steamNetwork);

    public void refresh();

    public Coord4 getCoords();

    public int getDimension();

    public int getSteam();

    public void updateSteam(int steam);

    public String getName();

    public void wasAdded();

}
