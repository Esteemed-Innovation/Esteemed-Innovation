package eiteam.esteemedinnovation.api.heater;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This acts as a registry for custom handlers for the Heater module
 */
public final class HeatableRegistry {
    private static final List<HeatHandler> heatables = new ArrayList<>();

    public static void addHeatable(HeatHandler heatable) {
        heatables.add(heatable);
    }

    /**
     * Used by the steam heater tile entity to get the matching {@link Steamable} it faces
     * @param world The world
     * @param pos The position in the world
     * @return The {@link Steamable} at the location given by params
     */
    @Nullable
    public static Steamable getSteamable(World world, BlockPos pos) {
        for (HeatHandler h : heatables) {
            Steamable steamable = h.apply(world, pos);
            if (steamable != null) {
                return steamable;
            }
        }
        return null;
    }

    public interface HeatHandler {
        /**
         * Used to get the {@link Steamable} to handle steaming
         * @param world The world
         * @param pos The position in the world
         * @return The {@link Steamable} matching the {@link net.minecraft.tileentity.TileEntity} or null
         */
        Steamable apply(World world, BlockPos pos);
    }
}
