package eiteam.esteemedinnovation.commons.util;

import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.init.misc.OreDictEntries;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class OreDictHelper {
    public static List<Pair<Item, Integer>> stones = new ArrayList<>();
    public static List<Pair<Item, Integer>> cobblestones = new ArrayList<>();
    public static List<Pair<Item, Integer>> stoneGrinderNuggets = new ArrayList<>();
    public static List<Pair<Item, Integer>> nuggets = new ArrayList<>();
    public static Map<Pair<Item, Integer>, String> ingots = new HashMap<>();
    public static List<Pair<Item, Integer>> leaves = new ArrayList<>();
    public static List<Pair<Item, Integer>> goldNuggets = new ArrayList<>();
    public static List<Pair<Item, Integer>> sands = new ArrayList<>();
    public static Map<Pair<Item, Integer>, String> blocks = new HashMap<>();
    public static Map<Pair<Item, Integer>, String> gems = new HashMap<>();
    public static List<Pair<Item, Integer>> sticks = new ArrayList<>();
    public static List<Pair<Item, Integer>> logs = new ArrayList<>();
    public static List<Pair<Item, Integer>> planks = new ArrayList<>();
    public static Map<Pair<Item, Integer>, Pair<Item, Integer>> logToPlank = new HashMap<>();
    public static List<Item> slabWoods = new ArrayList<>();
    public static List<Item> blockCoals = new ArrayList<>();
    public static List<Item> saplings = new ArrayList<>();
    public static List<Item> dirts = new ArrayList<>();
    public static List<Item> grasses = new ArrayList<>();
    public static List<Item> gravels = new ArrayList<>();
    public static List<Item> ores = new ArrayList<>();
    public static List<Pair<Item, Integer>> thinIronPlates = new ArrayList<>();

    public static void initializeOreDicts(String name, ItemStack stack) {
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            for (int i = 0; i < 15; i++) {
                initializeOreDicts(name, new ItemStack(stack.getItem(), 1, i));
            }
            return;
        }
        if (name.equals(OreDictEntries.STONE_ORE)) {
            stones.add(Pair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals(OreDictEntries.COBBLESTONE_ORE)) {
            Pair<Item, Integer> pair = Pair.of(stack.getItem(), stack.getItemDamage());
            cobblestones.add(pair);
        }

        if (name.startsWith(OreDictEntries.PREFIX_NUGGET)) {
            nuggets.add(Pair.of(stack.getItem(), stack.getItemDamage()));
            if (name.endsWith(OreDictEntries.MATERIAL_GOLD)) {
                goldNuggets.add(Pair.of(stack.getItem(), stack.getItemDamage()));
            }
            if (!Config.blacklistedStoneGrinderNuggets.contains(name)) {
                stoneGrinderNuggets.add(Pair.of(stack.getItem(), stack.getItemDamage()));
            }
        }

        if (name.startsWith(OreDictEntries.PREFIX_INGOT)) {
            ingots.put(Pair.of(stack.getItem(), stack.getItemDamage()), name);
        }

        if (name.equals(OreDictEntries.TREE_LEAVES)) {
            leaves.add(Pair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals(OreDictEntries.SAND_ORE)) {
            sands.add(Pair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.startsWith(OreDictEntries.PREFIX_BLOCK)) {
            blocks.put(Pair.of(stack.getItem(), stack.getItemDamage()), name);
            if (name.endsWith(OreDictEntries.MATERIAL_COAL)) {
                blockCoals.add(stack.getItem());
            }
        }

        if (name.startsWith(OreDictEntries.PREFIX_GEM)) {
            gems.put(Pair.of(stack.getItem(), stack.getItemDamage()), name);
        }

        if (name.equals(OreDictEntries.STICK_WOOD)) {
            sticks.add(Pair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.startsWith(OreDictEntries.PREFIX_LOG)) {
            logs.add(Pair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals(OreDictEntries.PLANK_WOOD)) {
            planks.add(Pair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals(OreDictEntries.PLATE_THIN_IRON)) {
            thinIronPlates.add(Pair.of(stack.getItem(), stack.getItemDamage()));
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

    public static void initializeGeneral() {
        InventoryCrafting temporaryCraftingGrid = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return false;
            }
        }, 3, 3);

        for (int i = 0; i < temporaryCraftingGrid.getSizeInventory(); i++) {
            temporaryCraftingGrid.setInventorySlotContents(i, null);
        }

        for (Pair<Item, Integer> log : logs) {
            temporaryCraftingGrid.setInventorySlotContents(0, new ItemStack(log.getLeft(), 1, log.getRight()));
            CraftingManager.getInstance().getRecipeList().stream()
              .filter(recipe -> recipe.matches(temporaryCraftingGrid, null))
              .forEach(recipe -> {
                ItemStack result = recipe.getRecipeOutput();
                if (result != null && arrayContains(planks, Pair.of(result.getItem(), result.getItemDamage()))) {
                    logToPlank.put(log, Pair.of(result.getItem(), result.getItemDamage()));
                }
            });
        }
    }

    /**
     * Gets whether the collection has the given value. This checks for WILDCARD OreDict metadata.
     * @param list The collection to check.
     * @param value The value to check for.
     * @return Whether it contains that given value.
     */
    public static boolean arrayContains(Collection list, Pair<Item, Integer> value) {
        if (value.getRight() == OreDictionary.WILDCARD_VALUE) {
            for (int i = 0; i < 15; i++) {
                if (list.contains(Pair.of(value.getLeft(), i))) {
                    return true;
                }
            }
        }
        return list.contains(value);
    }

    /**
     * Gets whether the Map has the given key. This checks for WILDCARD OreDict metadata.
     * @param hash The Map to check
     * @param key The key to check for.
     * @return Whether it contains that key.
     */
    public static boolean containsKey(Map hash, Pair<Item, Integer> key) {
        if (key.getRight() == OreDictionary.WILDCARD_VALUE) {
            for (int i = 0; i < 15; i++) {
                if (hash.containsKey(Pair.of(key.getLeft(), i))) {
                    return true;
                }
            }
        }
        return hash.containsKey(key);
    }

    /**
     * Checks if the iteration of Pair<Item, Integer>s has the given Item.
     * @param list The list to check.
     * @param left The Item to check for.
     * @return Whether it has the item.
     */
    public static boolean listHasItem(Iterable<Pair<Item, Integer>> list, Item left) {
        for (Pair<Item, Integer> s : list) {
            if (s.getLeft() == left) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see #listHasItem(Iterable, Item)
     * @param hash The hash to check.
     * @param left The Item to check for.
     * @return Whether it has the item.
     */
    public static boolean mapHasItem(Map<Pair<Item, Integer>, String> hash, Item left) {
        for (Pair<Item, Integer> s : hash.keySet()) {
            if (s.getLeft() == left) {
                return true;
            }
        }
        return false;
    }
}
