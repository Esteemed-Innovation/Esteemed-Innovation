package flaxbeard.steamcraft.init.misc.integration.crafttweaker;

import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import flaxbeard.steamcraft.init.misc.MiscellaneousCategory;

public class CraftTweakerIntegration extends MiscellaneousCategory {
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MineTweakerAPI.registerClass(CarvingTableTweaker.class);
        MineTweakerAPI.registerClass(CrucibleTweaker.class);
        MineTweakerAPI.registerClass(RockSmasherTweaker.class);
        MineTweakerAPI.registerClass(SteamHeaterTweaker.class);
    }
}
