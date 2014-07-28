package flaxbeard.steamcraft.integration;

import net.minecraft.item.ItemStack;
import twilightforest.item.TFItems;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.SteamcraftRecipes;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;

public class TwilightForestIntegration {
    
	public static void addTwilightForestLiquid() {
		CrucibleLiquid liquidFiery = new CrucibleLiquid("fiery", new ItemStack(TFItems.fieryIngot), new ItemStack(SteamcraftItems.steamcraftPlate,1,8), null, null,91,69,69);
		SteamcraftRegistry.liquids.add(liquidFiery);
		
		SteamcraftRegistry.registerSmeltThingOredict("fieryIngot", liquidFiery, 9);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetFiery", liquidFiery, 1);
		SteamcraftRegistry.registerSmeltThingOredict("fieryNugget", liquidFiery, 1);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftFiery", liquidFiery, 6);
		SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Fiery",new ItemStack(SteamcraftItems.exosuitPlate,1,8),"Fiery","Fiery","steamcraft.plate.fiery"));
		SteamcraftRecipes.addExosuitPlateRecipes("exoFiery","plateSteamcraftFiery",new ItemStack(SteamcraftItems.exosuitPlate,1,8),liquidFiery);
		SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Yeti",new ItemStack(SteamcraftItems.exosuitPlate,1,9),"Yeti","Yeti","steamcraft.plate.yeti"));
		SteamcraftRecipes.addExosuitPlateRecipes("exoYeti",new ItemStack(TFItems.alphaFur),new ItemStack(SteamcraftItems.exosuitPlate,1,9));

	}
}
