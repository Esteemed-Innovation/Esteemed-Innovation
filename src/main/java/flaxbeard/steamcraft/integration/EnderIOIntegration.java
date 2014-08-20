package flaxbeard.steamcraft.integration;

import net.minecraft.item.ItemStack;
import crazypants.enderio.EnderIO;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.SteamcraftRecipes;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;

public class EnderIOIntegration {

	public static void addEIOLiquid() {
		CrucibleLiquid liquidVibrant = new CrucibleLiquid("vibrant", new ItemStack(EnderIO.itemAlloy,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,10), new ItemStack(EnderIO.itemMaterial,1,4), null,124,179,56);
		SteamcraftRegistry.liquids.add(liquidVibrant);
		
		SteamcraftRegistry.registerSmeltThing(EnderIO.itemAlloy,2, liquidVibrant, 9);
		SteamcraftRegistry.registerSmeltThing(EnderIO.itemMaterial,4, liquidVibrant, 1);

		SteamcraftRegistry.registerSmeltThingOredict("ingotVibrant", liquidVibrant, 9);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetVibrant", liquidVibrant, 1);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftVibrant", liquidVibrant, 6);
		if (Config.enableVibrantPlate) {
			SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Vibrant",new ItemStack(SteamcraftItems.exosuitPlate,1,12),"Vibrant","Vibrant","steamcraft.plate.vibrant"));
			SteamcraftRecipes.addExosuitPlateRecipes("exoVibrant","plateSteamcraftVibrant",new ItemStack(SteamcraftItems.exosuitPlate,1,12),liquidVibrant);
		}
	}

}
