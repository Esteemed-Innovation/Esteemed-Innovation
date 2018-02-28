package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.api.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.HashMap;

public class BookRecipeRegistry {
    public static HashMap<String, IRecipe> recipes = new HashMap<>();

    public static void addRecipe(String key, IRecipe recipe) {
        recipes.put(key, recipe);
    }

    /**
     * @Deprecated Use {@link #addRecipe(ResourceLocation, ItemStack, Object...)} instead
     */
    @Deprecated
    public static void addRecipe(String key, ItemStack output, Object... params) {
        addRecipe(new ResourceLocation(Constants.EI_MODID, key), output, params);
    }

    public static void addRecipe(ResourceLocation group, ItemStack output, Object... params) {
        ShapedOreRecipe recipe = new ShapedOreRecipe(group, output, params);
        addRecipe(group.getResourcePath(), recipe);
    }

    public static IRecipe getRecipe(String key) {
        return recipes.get(key);
    }
}
