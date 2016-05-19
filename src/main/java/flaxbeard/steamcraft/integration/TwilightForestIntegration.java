package flaxbeard.steamcraft.integration;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.SteamcraftRecipes;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;

import twilightforest.item.TFItems;

import net.minecraft.item.ItemStack;

public class TwilightForestIntegration {

	private TwilightForestIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static void postInit() {
        CrucibleLiquid liquidFiery = new CrucibleLiquid("fiery", new ItemStack(TFItems.fieryIngot),
          new ItemStack(SteamcraftItems.steamcraftPlate, 1, 8), null, null, 91, 69, 69);
        SteamcraftRegistry.registerLiquid(liquidFiery);

        SteamcraftRegistry.registerMeltRecipeOreDict("fieryIngot", liquidFiery, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetFiery", liquidFiery, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("fieryNugget", liquidFiery, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftFiery", liquidFiery, 6);
        if (Config.enableFieryPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Fiery",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 8), "Fiery", "Fiery",
              "steamcraft.plate.fiery"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoFiery", "plateSteamcraftFiery",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 8), liquidFiery);
        }
        if (Config.enableYetiPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Yeti",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 9), "Yeti", "Yeti",
              "steamcraft.plate.yeti"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoYeti", new ItemStack(TFItems.alphaFur),
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 9));
        }
    }
}
