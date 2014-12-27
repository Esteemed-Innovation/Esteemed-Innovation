package flaxbeard.steamcraft.integration.natura;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.item.ItemSteamedFood;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class NaturaIntegration {
	public static Item imphide;
	
	//Our items
	public static Item steamedImphide;
	
	public static void grabItems(){
		imphide = GameRegistry.findItem("Natura", "impmeat");
	}
	
	public static void registerStuff(){
		steamedImphide = new ItemSteamedFood((ItemFood) imphide).setUnlocalizedName("steamcraft:steamedImphide").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamedImphide, "steamedImphide");
		SteamcraftRegistry.addSteamFood(imphide, steamedImphide);
		
		if(Loader.isModLoaded("IC2") && Config.enableIC2Integration) 
			IC2NaturaIntegration.addIC2Recipes();
	}
}
