package eiteam.esteemedinnovation.api;

import eiteam.esteemedinnovation.api.network.ITransporter;
import eiteam.esteemedinnovation.modules.transport.steam.SteamNetwork;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public interface SteamTransporter extends ITransporter {

    /**
     * The pressure of the device
     */
    float getPressure();

    /**
     * Sets the pressure of the device.
     * @param pressure
     */
    void setPressure(float pressure);

    /**
     * How resistant the device is to pressure meltdowns
     */
    float getPressureResistance();

    /**
     * How much steam the device can store
     */
    int getCapacity();

    int getSteamShare();

    /**
     * Explodes the transporter and removes it from the steamnet. This should handle any reductions in steamnet steam
     * or pressure. You do not have to actually make an explosion if you don't want to. This is called if
     * {@link #shouldExplode()} has been called. It must be called during the block/tile entity's update/tick.
     */
    void explode();

    /**
     * Queues up the block for an explosion.
     */
    void shouldExplode();

    /**
     * @param amount How much steam can be inserted per
     * @param direction The side of the device
     */
    void insertSteam(int amount, Direction direction);

    void decrSteam(int i);

    boolean doesConnect(Direction direction);

    /**
     * Called to ensure that the device can have a steam gauge put on it to check how much steam it has
     *
     * @param face - The side of the device
     *
     * @return true if steam gauges can be put on it
     */
    boolean acceptsGauge(Direction direction);

    int getDimension();

    int getSteam();

    void updateSteam(int steam);

    /**
     * @return Whether steam can pass through this transporter. Returns false for closed valve pipes, for example.
     */
    default boolean canSteamPassThrough() {
        return true;
    }
}
