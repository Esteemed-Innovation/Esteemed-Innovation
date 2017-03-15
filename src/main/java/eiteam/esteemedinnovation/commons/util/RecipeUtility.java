package eiteam.esteemedinnovation.commons.util;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Iterator;
import java.util.function.Predicate;

public class RecipeUtility {
    /**
     * Removes recipes that match the provided predicate. Outputs to the FML Log for each recipe removed.
     * @param predicate The predicate to check
     */
    public static void removeRecipe(Predicate<IRecipe> predicate) {
        Iterator<IRecipe> iter = CraftingManager.getInstance().getRecipeList().iterator();
        while (iter.hasNext()) {
            IRecipe recipe = iter.next();
            if (predicate.test(recipe)) {
                FMLLog.info("Removing recipe for " + recipe.getRecipeOutput());
                iter.remove();
            }
        }
    }

    /**
     * Removes recipes that result in the provided ItemStack.
     * @param removeFor The ItemStack to remove recipes for.
     */
    public static void removeRecipeByOutput(ItemStack removeFor) {
        removeRecipe(recipe -> {
            ItemStack out = recipe.getRecipeOutput();
            return out != null && ItemStackUtility.compareItemStacks(out, removeFor);
        });
    }

    /**
     * Removes recipes that result in the provided Item.
     * @param removeFor The Item to remove recipes for, assumes meta 0.
     */
    public static void removeRecipeByOutput(Item removeFor) {
        removeRecipeByOutput(new ItemStack(removeFor));
    }
}
