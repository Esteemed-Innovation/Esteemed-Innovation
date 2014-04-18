package flaxbeard.steamcraft.api;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.MutablePair;

public class SteamcraftRegistry {
	private static int nextEnhancementID = 0;
	public static ArrayList<CrucibleLiquid> liquids = new ArrayList<CrucibleLiquid>();
	public static HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,Integer>> smeltThings = new HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,Integer>>();
	public static HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,ItemStack>> dunkThings = new HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,ItemStack>>();
	public static HashMap<MutablePair<Item,IEnhancement>,IIcon> enhancementIcons = new HashMap<MutablePair<Item,IEnhancement>,IIcon>();
	public static HashMap<String,IEnhancement> enhancements = new HashMap<String,IEnhancement>();
	
	public static void registerDunkThing(Item item, int i, CrucibleLiquid liquid, ItemStack m) {
		dunkThings.put(MutablePair.of(item, i), MutablePair.of(liquid, m));
	}
	
	public static void registerDunkThing(Item item, CrucibleLiquid liquid, ItemStack m) {
		dunkThings.put(MutablePair.of(item, -1), MutablePair.of(liquid, m));
	}
	
	public static void registerDunkThingOredict(String dict, CrucibleLiquid liquid, ItemStack m) {
		ArrayList<ItemStack> ores = OreDictionary.getOres(dict);
		for (ItemStack ore : ores) {
			registerDunkThing(ore.getItem(),ore.getItemDamage(),liquid,m);
		}
	}
	
	
	public static void registerSmeltThing(Item item, int i, CrucibleLiquid liquid, int m) {
		smeltThings.put(MutablePair.of(item, i), MutablePair.of(liquid, m));
	}
	
	public static void registerSmeltThing(Item item, CrucibleLiquid liquid, int m) {
		smeltThings.put(MutablePair.of(item, -1), MutablePair.of(liquid, m));
	}
	
	public static void registerSmeltThingOredict(String dict, CrucibleLiquid liquid, int m) {
		ArrayList<ItemStack> ores = OreDictionary.getOres(dict);
		for (ItemStack ore : ores) {
			registerSmeltThing(ore.getItem(),ore.getItemDamage(),liquid,m);
		}
	}
	
	public static void registerLiquid(CrucibleLiquid liquid) {
		liquids.add(liquid);
	}
	
	public static void registerEnhancement(IEnhancement enhancement) {
		enhancements.put(enhancement.getID(),enhancement);
	}
}
