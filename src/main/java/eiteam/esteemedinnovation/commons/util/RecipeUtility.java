package eiteam.esteemedinnovation.commons.util;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A collection of static utility functions relating to Minecraft recipes.
 */
public final class RecipeUtility {
    /**
     * Removes recipes that match the provided predicate. Outputs to the mod log for each recipe removed.
     * @param predicate The predicate to check
     */
    public static void removeRecipe(Predicate<IRecipe> predicate) {
        // The collect call prevents a ConcurrentModification by moving all of the IRecipes into a separate collection.
        CraftingManager.REGISTRY.getKeys().stream()
          .filter(key -> {
              IRecipe recipe = CraftingManager.REGISTRY.getObject(key);
              return recipe != null && predicate.test(recipe);
          })
          .collect(Collectors.toSet())
          .forEach(RecipeUtility::removeRecipe);
    }

    /**
     * Removes the recipe by the provided registry name. Outputs to the mod log for the recipe removed.
     * @param key The registry name of the recipe to remove.
     */
    public static void removeRecipe(ResourceLocation key) {
        IRecipe recipe = CraftingManager.REGISTRY.getObject(key);
        if (recipe != null) {
            EsteemedInnovation.logger.info(String.format("Removing recipe for %s (key: %s)", recipe.getRecipeOutput(), key));
            ((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES).remove(key);
        }
    }

    /**
     * Removes recipes that result in the provided ItemStack.
     * @param removeFor The ItemStack to remove recipes for.
     */
    public static void removeRecipeByOutput(ItemStack removeFor) {
        removeRecipe(recipe -> ItemStackUtility.compareItemStacks(recipe.getRecipeOutput(), removeFor));
    }

    /**
     * Removes recipes that result in the provided Item.
     * @param removeFor The Item to remove recipes for, assumes meta 0.
     */
    public static void removeRecipeByOutput(Item removeFor) {
        removeRecipeByOutput(new ItemStack(removeFor));
    }
}
