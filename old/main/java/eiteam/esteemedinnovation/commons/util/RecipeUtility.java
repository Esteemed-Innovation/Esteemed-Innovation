package eiteam.esteemedinnovation.commons.util;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
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

    /**
     * Adds a shaped recipe for the provided input shape, input items, and output.
     * @param event The current RegistryEvent.
     * @param createBookRecipeRegistry Whether a book entry should be created for this recipe.
     * @param recipeName The registry name of the recipe (not including the path/mod).
     * @param block The block output by the recipe.
     * @param obj The recipe input shape and character to item/OreDictionary entry mappings, as defined by vanilla
     *            Minecraft's recipe shaping system.
     * @return The recipe created and registered.
     */
    public static IRecipe addRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, Block block, Object... obj) {
        return addRecipe(event, createBookRecipeRegistry, recipeName, new ItemStack(block), obj);
    }

    /**
     * Adds a shaped recipe for the provided input shape, input items, and output.
     * @param event The current RegistryEvent.
     * @param createBookRecipeRegistry Whether a book entry should be created for this recipe.
     * @param recipeName The registry name of the recipe (not including the path/mod).
     * @param item The item output by the recipe.
     * @param obj The recipe input shape and character to item/OreDictionary entry mappings, as defined by vanilla
     *            Minecraft's recipe shaping system.
     * @return The recipe created and registered.
     */
    public static IRecipe addRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, Item item, Object... obj) {
        return addRecipe(event, createBookRecipeRegistry, recipeName, new ItemStack(item), obj);
    }

    /**
     * Adds a shaped recipe for the provided input shape, input items, and output.
     * @param event The current RegistryEvent.
     * @param createBookRecipeRegistry Whether a book entry should be created for this recipe.
     * @param recipeName The registry name of the recipe (not including the path/mod).
     * @param result The item output by the recipe.
     * @param obj The recipe input shape and character to item/OreDictionary entry mappings, as defined by vanilla
     *            Minecraft's recipe shaping system.
     * @return The recipe created and registered.
     */
    public static IRecipe addRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, ItemStack result, Object... obj) {
        ResourceLocation group = new ResourceLocation(Constants.EI_MODID, recipeName);
        ShapedOreRecipe recipe = new ShapedOreRecipe(group, result, obj);
        return addRecipe(event, createBookRecipeRegistry, recipeName, recipe);
    }

    /**
     * Registers a shapeless recipe for the provided inputs and output block.
     * @param event The current RegistryEvent.
     * @param createBookRecipeRegistry Whether a book entry should be created for this recipe.
     * @param recipeName The registry name of the recipe (not including the path/mod).
     * @param block The block yielded by the recipe.
     * @param obj The recipe inputs.
     * @return The recipe created and registered.
     */
    public static IRecipe addShapelessRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, Block block, Object... obj) {
        return addShapelessRecipe(event, createBookRecipeRegistry, recipeName, new ItemStack(block), obj);
    }

    /**
     * Registers a shapeless recipe for the provided inputs and output item.
     * @param event The current RegistryEvent.
     * @param createBookRecipeRegistry Whether a book entry should be created for this recipe.
     * @param recipeName The registry name of the recipe (not including the path/mod).
     * @param item The item yielded by the recipe.
     * @param obj The recipe inputs.
     * @return The recipe created and registered.
     */
    public static IRecipe addShapelessRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, Item item, Object... obj) {
        return addShapelessRecipe(event, createBookRecipeRegistry, recipeName, new ItemStack(item), obj);
    }

    /**
     * Registers a shapeless recipe for the provided inputs and outputs.
     * @param event The current RegistryEvent.
     * @param createBookRecipeRegistry Whether a book entry should be created for this recipe.
     * @param recipeName The registry name of the recipe (not including the path/mod).
     * @param result The item yielded by the recipe.
     * @param obj The recipe inputs.
     * @return The recipe created and registered.
     */
    public static IRecipe addShapelessRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, ItemStack result, Object... obj) {
        ResourceLocation group = new ResourceLocation(Constants.EI_MODID, recipeName);
        ShapelessOreRecipe recipe = new ShapelessOreRecipe(group, result, obj);
        return addRecipe(event, createBookRecipeRegistry, recipeName, recipe);
    }

    /**
     * Registers the provided recipe, setting its proper registry name, registering it, and registering it to the
     * {@link BookRecipeRegistry} if necessary.
     * @param event The current RegistryEvent.
     * @param createBookRecipeRegistry Whether a book entry should be created for this recipe.
     * @param recipeName The registry name of the recipe (not including the path/mod).
     * @param recipe The recipe to register.
     * @return The recipe created and registered.
     */
    public static IRecipe addRecipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, IRecipe recipe) {
        recipe.setRegistryName(new ResourceLocation(Constants.EI_MODID, recipeName));
        event.getRegistry().register(recipe);
        if (createBookRecipeRegistry) {
            BookRecipeRegistry.addRecipe(recipeName, recipe);
        }
        return recipe;
    }

    /**
     * Shortcut method for adding a recipe that takes up all 9 slots of the Crafting Table with an input OreDictionary
     * entry.
     * @param event The current RegistryEvent.
     * @param createBookRecipeRegistry Whether a book entry should be created for this recipe.
     * @param recipeName The registry name of the recipe (not including the path/mod).
     * @param output The ItemStack that is yielded from this recipe.
     * @param input The OreDictionary entry string used for this recipe.
     * @return The recipe created and registered.
     */
    public static IRecipe add3x3Recipe(RegistryEvent.Register<IRecipe> event, boolean createBookRecipeRegistry, String recipeName, ItemStack output, String input) {
        return addRecipe(event, createBookRecipeRegistry, recipeName, output,
          "xxx",
          "xxx",
          "xxx",
          'x', input);
    }
}
