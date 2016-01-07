package flaxbeard.steamcraft.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;

public class OreDictHelper {
    public static ArrayList<MutablePair<Item, Integer>> stones = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> cobblestones = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> nuggets = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> ingots = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> leaves = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> goldNuggets = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> sands = new ArrayList<>();

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
            ingots.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals("treeLeaves")) {
            leaves.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals("sand")) {
            sands.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }
    }
}
