package flaxbeard.steamcraft.integration;

import net.minecraft.item.ItemStack;
import thermalfoundation.item.TFItems;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.SteamcraftRecipes;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;

public class ThermalFoundationIntegration {

	public static void addThermalFoundationLiquid() {
		CrucibleLiquid liquidEnderium = new CrucibleLiquid("enderium", TFItems.ingotEnderium, new ItemStack(SteamcraftItems.steamcraftPlate,1,11), TFItems.nuggetEnderium, null,15,106,106);
		SteamcraftRegistry.liquids.add(liquidEnderium);
		

		SteamcraftRegistry.registerSmeltThingOredict("ingotEnderium", liquidEnderium, 9);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetEnderium", liquidEnderium, 1);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftEnderium", liquidEnderium, 6);
		if (Config.enableEnderiumPlate) {
			SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Enderium",new ItemStack(SteamcraftItems.exosuitPlate,1,13),"Enderium","Enderium","steamcraft.plate.enderium"));
			SteamcraftRecipes.addExosuitPlateRecipes("exoEnderium","plateSteamcraftEnderium",new ItemStack(SteamcraftItems.exosuitPlate,1,13),liquidEnderium);
		}
	}

}
