package flaxbeard.steamcraft.api;

import flaxbeard.steamcraft.misc.ItemStackUtility;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class SteamingRegistry {
    /**
     * All of the custom steaming recipes for the Steam Heater. These recipes will replace existing smelting recipes.
     * Key: input, Value: output
     */
    private static HashMap<ItemStack, ItemStack> steamingRecipes = new HashMap<>();


    /**
     * Adds a steaming custom recipe (not a replacement recipe).
     * @param input The input ItemStack
     * @param output The output ItemStack
     */
    public static void addSteamingRecipe(ItemStack input, ItemStack output) {
        steamingRecipes.put(input, output);
    }

    /**
     * Removes a steaming custom recipe (not a replacement recipe).
     * @param input The input ItemStack.
     */
    public static void removeSteamingRecipe(ItemStack input) {
        steamingRecipes.remove(input);
    }

    /**
     * Gets the steaming result for the given item. If there is no steaming recipe, returns the result of
     * {@link FurnaceRecipes#getSmeltingResult}.
     */
    public static ItemStack getSteamingResult(@Nonnull ItemStack input) {
        ItemStack steaming = getSteamingResultNoSmelting(input);
        return steaming == null ? FurnaceRecipes.instance().getSmeltingResult(input) : steaming;
    }

    /**
     * Gets the steaming result for the given item. If there is no steaming recipe, returns null.
     */
    public static ItemStack getSteamingResultNoSmelting(@Nonnull ItemStack input) {
        System.out.println("getting result for " + input.toString());
        for (Map.Entry<ItemStack, ItemStack> entry : steamingRecipes.entrySet()) {
            System.out.println(entry.getKey().toString());
            if (ItemStackUtility.compareItemStacks(input, entry.getKey())) {
                System.out.println(input.toString() + " and " + entry.getKey().toString() + " are equal basically");
                return entry.getValue();
            }
        }
        return null;
    }
}
