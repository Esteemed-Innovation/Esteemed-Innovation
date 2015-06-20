package flaxbeard.steamcraft.integration.natura;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.item.ItemSteamedFood;

import mods.natura.common.NContent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class NaturaIntegration {
	
	public static Item steamedImphide;
	
	public static void postInit() {
		steamedImphide = new ItemSteamedFood((ItemFood) NContent.impMeat).setUnlocalizedName("steamcraft:steamedImphide").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamedImphide, "steamedImphide");
		SteamcraftRegistry.addSteamFood(NContent.impMeat, steamedImphide);
		
		if(CrossMod.INDUSTRIALCRAFT) {
			IC2NaturaIntegration.addIC2Recipes();
		}
	}
}
