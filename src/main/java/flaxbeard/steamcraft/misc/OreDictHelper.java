package flaxbeard.steamcraft.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;

public class OreDictHelper {
    public static ArrayList<MutablePair<Item, Integer>> stones = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> cobblestones = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> nuggets = new ArrayList<>();
    public static ArrayList<MutablePair<Item, Integer>> ingots = new ArrayList<>();

    public static void initializeOreDicts(String name, ItemStack stack) {
        if (name.equals("stone")) {
            stones.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.equals("cobblestone")) {
            MutablePair<Item, Integer> pair = MutablePair.of(stack.getItem(), stack.getItemDamage());
            cobblestones.add(pair);
        }

        if (name.startsWith("nugget")) {
            nuggets.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }

        if (name.startsWith("ingot")) {
            ingots.add(MutablePair.of(stack.getItem(), stack.getItemDamage()));
        }
    }
}
