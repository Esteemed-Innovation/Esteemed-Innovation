package flaxbeard.steamcraft.integration.minetweaker;

import minetweaker.MineTweakerAPI;

public class MineTweakerIntegration {
    public static void postInit() {
        MineTweakerAPI.registerClass(CarvingTableTweaker.class);
        MineTweakerAPI.registerClass(CrucibleTweaker.class);
        MineTweakerAPI.registerClass(RockSmasherTweaker.class);
        MineTweakerAPI.registerClass(SteamHeaterTweaker.class);
    }
}
