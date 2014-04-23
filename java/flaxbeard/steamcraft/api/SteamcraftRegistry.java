package flaxbeard.steamcraft.api;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.MutablePair;

import flaxbeard.steamcraft.api.book.BookPage;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.misc.Tuple3;

public class SteamcraftRegistry {
	private static int nextEnhancementID = 0;
	public static ArrayList<CrucibleLiquid> liquids = new ArrayList<CrucibleLiquid>();
	public static HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,Integer>> smeltThings = new HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,Integer>>();
	public static HashMap<Tuple3,MutablePair<Integer,ItemStack>> dunkThings = new HashMap<Tuple3,MutablePair<Integer,ItemStack>>();
	public static HashMap<MutablePair<Item,IEnhancement>,IIcon> enhancementIcons = new HashMap<MutablePair<Item,IEnhancement>,IIcon>();
	public static HashMap<String,IEnhancement> enhancements = new HashMap<String,IEnhancement>();
	public static ArrayList<String> categories = new ArrayList<String>();
	public static ArrayList<MutablePair<String, String>> research = new ArrayList<MutablePair<String,String>>();
	public static HashMap<String,BookPage[]> researchPages = new HashMap<String,BookPage[]>();
	
	public static void addCategory(String string) {
		categories.add(string);
	}
	
	public static void addResearch(String string, String category, BookPage... pages) {
		research.add(MutablePair.of(string,category));
		researchPages.put(string, pages);
	}
	
	public static CrucibleLiquid getLiquidFromName(String name) {
		for (CrucibleLiquid liquid : liquids) {
			if (liquid.name.equals(name)) {
				return liquid;
			}
		}
		return null;
	}
	
	public static void registerDunkThing(Item item, int meta, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
		dunkThings.put(new Tuple3(item,meta,liquid), MutablePair.of(liquidAmount, result));
	}
	
	public static void registerDunkThing(Item item, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
		dunkThings.put(new Tuple3(item,-1, liquid), MutablePair.of(liquidAmount, result));
	}
	
	public static void registerDunkThingOredict(String dict, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
		ArrayList<ItemStack> ores = OreDictionary.getOres(dict);
		for (ItemStack ore : ores) {
			registerDunkThing(ore.getItem(),ore.getItemDamage(),liquid,liquidAmount,result);
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
	
	public static void registerSmeltTool(Item item, CrucibleLiquid liquid, int m) {
		for (int i = 0; i < item.getMaxDamage(); i++) {
			smeltThings.put(MutablePair.of(item,i), MutablePair.of(liquid, MathHelper.floor_double(m*((float)(item.getMaxDamage()-i)/(float)item.getMaxDamage()))));
		}
	}
	
	public static void registerLiquid(CrucibleLiquid liquid) {
		liquids.add(liquid);
	}
	
	public static void registerEnhancement(IEnhancement enhancement) {
		enhancements.put(enhancement.getID(),enhancement);
	}
}
