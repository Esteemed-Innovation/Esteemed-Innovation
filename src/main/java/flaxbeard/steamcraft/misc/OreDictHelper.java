package flaxbeard.steamcraft.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;

public class OreDictHelper {
    public static ArrayList<MutablePair<Item, Integer>> stones = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> cobblestones = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> nuggets = new ArrayList<>();
    public static HashMap<MutablePair<Item, Integer>, String> ingots = new HashMap<>();
    public static ArrayList<MutablePair<Item, Integer>> leaves = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> goldNuggets = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> sands = new ArrayList<>();
    public static HashMap<MutablePair<Item, Integer>, String> blocks = new HashMap<>();
    public static HashMap<MutablePair<Item, Integer>, String> gems = new HashMap<>();
    public static ArrayList<MutablePair<Item, Integer>> sticks = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> logs = new ArrayList<>();

    public static ArrayList<MutablePair<Item, Integer>> plateSteamcraftIrons = new ArrayList<>();

    private OreDictHelper() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
    
    public static void initializeOreDicts(String name, ItemStack stack) {
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            for (int i = 0; i < 15; i++) {
                initializeOreDicts(name, new ItemStack(stack.getItem(), 1, i));
            }
            return;
        }
        if (name.equals("stone")) {
            stones.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals("cobblestone")) {
            MutablePair<Item, Integer> pair = MutablePair.of(stack.getItem(), stack.getItemDamage());
            cobblestones.add(pair);
        }

        if (name.startsWith("nugget")) {
            nuggets.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
            if (name.endsWith("Gold")) {
                goldNuggets.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
            }
        }

        if (name.startsWith("ingot")) {
            ingots.put(MutablePair.of(stack.getItem(), stack.getItemDamage()), name);
        }

        if (name.equals("treeLeaves")) {
            leaves.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals("sand")) {
            sands.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.startsWith("block")) {
            blocks.put(MutablePair.of(stack.getItem(), stack.getItemDamage()), name);
        }

        if (name.startsWith("gem")) {
            gems.put(MutablePair.of(stack.getItem(), stack.getItemDamage()), name);
        }

        if (name.equals("stickWood")) {
            sticks.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.startsWith("log")) {
            logs.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals("plateSteamcraftIron")) {
            plateSteamcraftIrons.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }
    }

    /**
     * Gets whether the arraylist has the given value. This checks for WILDCARD OreDict metadata.
     * @param list The ArrayList to check.
     * @param value The value to check for.
     * @return Whether it contains that given value.
     */
    public static boolean arrayContains(ArrayList list, MutablePair<Item, Integer> value) {
        if (value.right == OreDictionary.WILDCARD_VALUE) {
            for (int i = 0; i < 15; i++) {
                if (list.contains(MutablePair.of(value.left, i))) {
                    return true;
                }
            }
        }
        return list.contains(value);
    }

    /**
     * Gets whether the HashMap has the given key. This checks for WILDCARD OreDict metadata.
     * @param hash The HashMap to check
     * @param key The key to check for.
     * @return Whether it contains that key.
     */
    public static boolean containsKey(HashMap hash, MutablePair<Item, Integer> key) {
        if (key.right == OreDictionary.WILDCARD_VALUE) {
            for (int i = 0; i < 15; i++) {
                if (hash.containsKey(MutablePair.of(key.left, i))) {
                    return true;
                }
            }
        }
        return hash.containsKey(key);
    }

    /**
     * Checks if the ArrayList of the MutablePair<Item, Integer> format has the given Item.
     * @param list The list to check.
     * @param left The Item to check for.
     * @return Whether it has the item.
     */
    public static boolean arrayHasItem(ArrayList<MutablePair<Item, Integer>> list, Item left) {
        for (MutablePair<Item, Integer> s : list) {
            if (s.left == left) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see #arrayHasItem(ArrayList, Item)
     * @param hash The hash to check.
     * @param left The Item to check for.
     * @return Whether it has the item.
     */
    public static boolean hashHasItem(HashMap<MutablePair<Item, Integer>, String> hash, Item left) {
        for (MutablePair<Item, Integer> s : hash.keySet()) {
            if (s.left == left) {
                return true;
            }
        }
        return false;
    }
}
