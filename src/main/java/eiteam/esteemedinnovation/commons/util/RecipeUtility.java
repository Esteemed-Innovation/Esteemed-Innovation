package eiteam.esteemedinnovation.commons.util;

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
}
