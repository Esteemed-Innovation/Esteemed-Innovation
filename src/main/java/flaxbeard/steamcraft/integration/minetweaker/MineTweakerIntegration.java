package flaxbeard.steamcraft.integration.minetweaker;

import minetweaker.MineTweakerAPI;

public class MineTweakerIntegration {
    
	private MineTweakerIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static void postInit() {
        MineTweakerAPI.registerClass(CarvingTableTweaker.class);
        MineTweakerAPI.registerClass(CrucibleTweaker.class);
        MineTweakerAPI.registerClass(RockSmasherTweaker.class);
        MineTweakerAPI.registerClass(SteamHeaterTweaker.class);
    }
}
