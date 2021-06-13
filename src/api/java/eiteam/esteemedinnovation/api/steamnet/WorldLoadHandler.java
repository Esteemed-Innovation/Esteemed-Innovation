package eiteam.esteemedinnovation.api.steamnet;

import eiteam.esteemedinnovation.api.steamnet.data.SteamNetworkData;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoadHandler {
    @SubscribeEvent
    public void handleWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            SteamNetworkData.get(world);
            SteamNetworkRegistry.initialize();
        }
    }
}
