package flaxbeard.steamcraft.integration;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.SteamcraftRecipes;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;

import crazypants.enderio.EnderIO;
import net.minecraft.item.ItemStack;

public class EnderIOIntegration {

	private EnderIOIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static void postInit() {
        CrucibleLiquid liquidVibrant = new CrucibleLiquid("vibrant",
          new ItemStack(EnderIO.itemAlloy, 1, 2), new ItemStack(SteamcraftItems.steamcraftPlate,
          1, 10), new ItemStack(EnderIO.itemMaterial, 1, 4), null, 124, 179, 56);
        SteamcraftRegistry.registerLiquid(liquidVibrant);

        SteamcraftRegistry.registerMeltRecipe(EnderIO.itemAlloy, 2, liquidVibrant, 9);
        SteamcraftRegistry.registerMeltRecipe(EnderIO.itemMaterial, 4, liquidVibrant, 1);

        SteamcraftRegistry.registerMeltRecipeOreDict("ingotVibrant", liquidVibrant, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetVibrant", liquidVibrant, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftVibrant", liquidVibrant, 6);
        if (Config.enableVibrantPlate) {
            SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Vibrant",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 12), "Vibrant", "Vibrant",
              "steamcraft.plate.vibrant"));
            SteamcraftRecipes.addExosuitPlateRecipes("exoVibrant", "plateSteamcraftVibrant",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 12), liquidVibrant);
        }
    }

}
