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
    float getPressure();

    /**
     * How resistant the device is to pressure meltdowns
     */
    float getPressureResistance();

    /**
     * Called to ensure that the device can have steam put into it
     *
     * @param face - The side of the device
     *
     * @return true if steam can be inserted
     */
    boolean canInsert(ForgeDirection face);

    /**
     * How much steam the device can store
     */
    int getCapacity();

    int getSteamShare();

    void explode();

    /**
     * @param amount - How much steam can be inserted per
     * @param face   - The side of the device
     */
    void insertSteam(int amount, ForgeDirection face);

    void decrSteam(int i);

    boolean doesConnect(ForgeDirection face);

    /**
     * Called to ensure that the device can have a steam gauge put on it to check how much steam it has
     *
     * @param face - The side of the device
     *
     * @return true if steam gauges can be put on it
     */
    boolean acceptsGauge(ForgeDirection face);

    HashSet<ForgeDirection> getConnectionSides();

    World getWorld();

    String getNetworkName();

    void setNetworkName(String name);

    SteamNetwork getNetwork();

    void setNetwork(SteamNetwork steamNetwork);

    void refresh();

    Coord4 getCoords();

    int getDimension();

    int getSteam();

    void updateSteam(int steam);

    String getName();

    void wasAdded();
}
