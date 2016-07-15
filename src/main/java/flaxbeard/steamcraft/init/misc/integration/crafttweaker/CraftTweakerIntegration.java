package flaxbeard.steamcraft.init.misc.integration.crafttweaker;

import minetweaker.MineTweakerAPI;
import flaxbeard.steamcraft.init.misc.MiscellaneousCategory;

public class CraftTweakerIntegration extends MiscellaneousCategory {
    @Override
    public void postInit() {
        MineTweakerAPI.registerClass(CarvingTableTweaker.class);
        MineTweakerAPI.registerClass(CrucibleTweaker.class);
        MineTweakerAPI.registerClass(RockSmasherTweaker.class);
        MineTweakerAPI.registerClass(SteamHeaterTweaker.class);
    }
}
