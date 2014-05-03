package flaxbeard.steamcraft.integration;

import thaumcraft.common.config.ConfigItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;

public class ThaumcraftIntegration {
	public static void addThaumiumLiquid() {
		CrucibleLiquid liquidThaumium = new CrucibleLiquid("thaumium", new ItemStack(ConfigItems.itemResource,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,5), new ItemStack(ConfigItems.itemNugget,1,6), null,105,87,163);
		SteamcraftRegistry.liquids.add(liquidThaumium);
		
		SteamcraftRegistry.registerSmeltThingOredict("ingotThaumium", liquidThaumium, 9);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetThaumium", liquidThaumium, 1);
		SteamcraftRegistry.registerSmeltThingOredict("plateThaumium", liquidThaumium, 6);
		SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Thaumium","plateThaumium","Thaumium","Thaumium"));

		
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemSwordThaumium, liquidThaumium, 18);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemPickThaumium, liquidThaumium, 27);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemAxeThaumium, liquidThaumium, 27);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemHoeThaumium, liquidThaumium, 18);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemShovelThaumium, liquidThaumium, 9);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemBootsThaumium, liquidThaumium, 36);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemChestThaumium, liquidThaumium, 81);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemHelmetThaumium, liquidThaumium, 45);
		SteamcraftRegistry.registerSmeltTool(ConfigItems.itemLegsThaumium, liquidThaumium, 63);
	}
}
