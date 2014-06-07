package flaxbeard.steamcraft.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import thaumcraft.common.config.ConfigItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.item.ItemExosuitUpgrade;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;

public class ThaumcraftIntegration {
    public static Item goggleUpgrade;
  //  public static Item thaumSource;
    
	public static void addThaumiumLiquid() {
		CrucibleLiquid liquidThaumium = new CrucibleLiquid("thaumium", new ItemStack(ConfigItems.itemResource,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,5), new ItemStack(ConfigItems.itemNugget,1,6), null,105,87,163);
		SteamcraftRegistry.liquids.add(liquidThaumium);
		
		goggleUpgrade = new ItemExosuitUpgrade(ExosuitSlot.headGoggles, "steamcraft:textures/models/armor/gogglesUpgrade.png",null,0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:goggleUpgrade").setTextureName("steamcraft:gogglesUpgrade");
		GameRegistry.registerItem(goggleUpgrade, "goggleUpgrade");
		//thaumSource = new ItemThaumcraftPowerSource().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:thaumSource").setTextureName("steamcraft:thaumSource");
		//GameRegistry.registerItem(thaumSource, "thaumSource");
		
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

	public static Item gogglesRevealing() {
		return ConfigItems.itemGoggles;
	}
	
	
}
