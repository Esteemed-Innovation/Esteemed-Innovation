package flaxbeard.steamcraft.integration.nei;

import flaxbeard.steamcraft.integration.nei.handlers.*;

import codechicken.nei.api.API;

public class NEIIntegration {
	
	private NEIIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static void preInit() {
		API.registerRecipeHandler(new RockSmasherHandler());
		API.registerUsageHandler(new RockSmasherHandler());
		
		API.registerRecipeHandler(new SteamHeaterHandler());
		API.registerUsageHandler(new SteamHeaterHandler());
	}
}
