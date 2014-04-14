package flaxbeard.steamcraft.api;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.MutablePair;

public class CrucibleLiquid {
	public static ArrayList<CrucibleLiquid> liquids = new ArrayList<CrucibleLiquid>();
	public static HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,Integer>> smeltThings = new HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,Integer>>();
	public static HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,ItemStack>> dunkThings = new HashMap<MutablePair<Item,Integer>,MutablePair<CrucibleLiquid,ItemStack>>();
	
	public ItemStack ingot;
	public ItemStack plate;
	public ItemStack nugget;
	public CrucibleFormula recipe;
	//public Color color;
	public int cr;
	public int cg;
	public int cb;
	
	public CrucibleLiquid(ItemStack ingot1, ItemStack plate1, ItemStack nugget1, CrucibleFormula formula1, int r, int g, int b) {
		this.ingot = ingot1;
		this.plate = plate1;
		this.nugget = nugget1;
		this.recipe = formula1;
		//this.color = new Color(r, g, b);
		this.cr = r;
		this.cg = g;
		this.cb = b;
	}
	
	public static void registerDunkThing(Item item, int i, CrucibleLiquid liquid, ItemStack m) {
		CrucibleLiquid.dunkThings.put(MutablePair.of(item, i), MutablePair.of(liquid, m));
	}
	
	public static void registerDunkThing(Item item, CrucibleLiquid liquid, ItemStack m) {
		CrucibleLiquid.dunkThings.put(MutablePair.of(item, -1), MutablePair.of(liquid, m));
	}
	
	public static void registerDunkThingOredict(String dict, CrucibleLiquid liquid, ItemStack m) {
		ArrayList<ItemStack> ores = OreDictionary.getOres(dict);
		for (ItemStack ore : ores) {
			registerDunkThing(ore.getItem(),ore.getItemDamage(),liquid,m);
		}
	}
	
	
	public static void registerSmeltThing(Item item, int i, CrucibleLiquid liquid, int m) {
		CrucibleLiquid.smeltThings.put(MutablePair.of(item, i), MutablePair.of(liquid, m));
	}
	
	public static void registerSmeltThing(Item item, CrucibleLiquid liquid, int m) {
		CrucibleLiquid.smeltThings.put(MutablePair.of(item, -1), MutablePair.of(liquid, m));
	}
	
	public static void registerSmeltThingOredict(String dict, CrucibleLiquid liquid, int m) {
		ArrayList<ItemStack> ores = OreDictionary.getOres(dict);
		for (ItemStack ore : ores) {
			registerSmeltThing(ore.getItem(),ore.getItemDamage(),liquid,m);
		}
	}
	
	public void registerLiquid(CrucibleLiquid liquid) {
		liquids.add(liquid);
	}
}
