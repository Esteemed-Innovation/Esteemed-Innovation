package eiteam.esteemedinnovation.misc;

import eiteam.esteemedinnovation.init.misc.OreDictEntries;
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
    public static ArrayList<Item> slabWoods = new ArrayList<>();
    public static ArrayList<Item> blockCoals = new ArrayList<>();
    public static ArrayList<Item> saplings = new ArrayList<>();
    public static ArrayList<Item> dirts = new ArrayList<>();
    public static ArrayList<Item> grasses = new ArrayList<>();
    public static ArrayList<Item> gravels = new ArrayList<>();
    public static ArrayList<Item> ores = new ArrayList<>();

    public static ArrayList<MutablePair<Item, Integer>> thinIronPlates = new ArrayList<>();

    public static void initializeOreDicts(String name, ItemStack stack) {
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            for (int i = 0; i < 15; i++) {
                initializeOreDicts(name, new ItemStack(stack.getItem(), 1, i));
            }
            return;
        }
        if (name.equals(OreDictEntries.STONE_ORE)) {
            stones.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals(OreDictEntries.COBBLESTONE_ORE)) {
            MutablePair<Item, Integer> pair = MutablePair.of(stack.getItem(), stack.getItemDamage());
            cobblestones.add(pair);
        }

        if (name.startsWith(OreDictEntries.PREFIX_NUGGET)) {
            nuggets.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
            if (name.endsWith(OreDictEntries.MATERIAL_GOLD)) {
                goldNuggets.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
            }
        }

        if (name.startsWith(OreDictEntries.PREFIX_INGOT)) {
            ingots.put(MutablePair.of(stack.getItem(), stack.getItemDamage()), name);
        }

        if (name.equals(OreDictEntries.TREE_LEAVES)) {
            leaves.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals(OreDictEntries.SAND_ORE)) {
            sands.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.startsWith(OreDictEntries.PREFIX_BLOCK)) {
            blocks.put(MutablePair.of(stack.getItem(), stack.getItemDamage()), name);
            if (name.endsWith(OreDictEntries.MATERIAL_COAL)) {
                blockCoals.add(stack.getItem());
            }
        }

        if (name.startsWith(OreDictEntries.PREFIX_GEM)) {
            gems.put(MutablePair.of(stack.getItem(), stack.getItemDamage()), name);
        }

        if (name.equals(OreDictEntries.STICK_WOOD)) {
            sticks.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.startsWith(OreDictEntries.PREFIX_LOG)) {
            logs.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals(OreDictEntries.PLATE_THIN_IRON)) {
            thinIronPlates.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals(OreDictEntries.SLAB_WOOD)) {
            slabWoods.add(stack.getItem());
        }

        if (name.equals(OreDictEntries.TREE_SAPLING)) {
            saplings.add(stack.getItem());
        }

        if (name.equals(OreDictEntries.DIRT_ORE)) {
            dirts.add(stack.getItem());
        }

        if (name.equals(OreDictEntries.GRASS_ORE)) {
            grasses.add(stack.getItem());
        }

        if (name.equals(OreDictEntries.GRAVEL_ORE)) {
            gravels.add(stack.getItem());
        }

        if (name.startsWith(OreDictEntries.PREFIX_ORE)) {
            ores.add(stack.getItem());
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
