package flaxbeard.steamcraft.integration.nei;

import flaxbeard.steamcraft.integration.nei.handlers.RockSmasherHandler;

import codechicken.nei.api.API;

public class NEIIntegration {
	public static void preInit() {
		API.registerRecipeHandler(new RockSmasherHandler());
		API.registerUsageHandler(new RockSmasherHandler());
	}
}
