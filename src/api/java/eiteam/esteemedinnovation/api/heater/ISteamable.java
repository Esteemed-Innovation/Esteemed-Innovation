package eiteam.esteemedinnovation.api.heater;

import net.minecraft.tileentity.TileEntityFurnace;

public interface ISteamable {
    /**
     * Similar to {@link TileEntityFurnace#canSmelt()}, indicates whether the {@link eiteam.esteemedinnovation.heater.TileEntitySteamHeater} should work on the tile at the given moment
     * @return true if steaming logic should happen
     */
    boolean acceptsSteam();

    /**
     * Called every tick to perform logic related to cooking. Can be called multiple times per tick depending on number of {@link eiteam.esteemedinnovation.heater.TileEntitySteamHeater} attached.
     */
    void steam();

    /**
     * Called when {@link ISteamable#acceptsSteam} returns false
     */
    void stopSteam();

    /**
     * Identical to {@link TileEntityFurnace#isBurning()}
     * @return true if burning
     */
    boolean isBurning();
}
