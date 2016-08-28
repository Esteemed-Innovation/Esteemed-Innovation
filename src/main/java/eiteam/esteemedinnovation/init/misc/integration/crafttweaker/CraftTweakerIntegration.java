package eiteam.esteemedinnovation.init.misc.integration.crafttweaker;

import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import eiteam.esteemedinnovation.init.misc.MiscellaneousCategory;

public class CraftTweakerIntegration extends MiscellaneousCategory {
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MineTweakerAPI.registerClass(CarvingTableTweaker.class);
        MineTweakerAPI.registerClass(CrucibleTweaker.class);
        MineTweakerAPI.registerClass(RockSmasherTweaker.class);
        MineTweakerAPI.registerClass(SteamHeaterTweaker.class);
    }
}
