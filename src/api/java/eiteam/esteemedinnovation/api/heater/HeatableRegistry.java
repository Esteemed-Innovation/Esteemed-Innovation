package eiteam.esteemedinnovation.api.heater;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * This acts as a registry for custom handlers for the Heater module
 */
public class HeatableRegistry {

    private static final List<IHeatHandler> heatables = new ArrayList<>();

    public static void addHeatable(IHeatHandler heatable) {
        heatables.add(heatable);
    }

    /**
     * Used by the {@link eiteam.esteemedinnovation.heater.TileEntitySteamHeater} to get the matching {@link ISteamable} it faces
     * @param world The world
     * @param pos The position in the world
     * @return The {@link ISteamable} at the location given by params
     */
    public static ISteamable getSteamable(World world, BlockPos pos) {
        for (IHeatHandler h : heatables) {
            ISteamable steamable = h.apply(world, pos);
            if(steamable != null) {
                return steamable;
            }
        }
        return null;
    }

    public interface IHeatHandler {
        /**
         * Used to get the ISteamable to handle steaming
         * @param world The world
         * @param pos The position in the world
         * @return The ISteamable matching the {@link net.minecraft.tileentity.TileEntity} or null
         */
        ISteamable apply(World world, BlockPos pos);
    }
}
