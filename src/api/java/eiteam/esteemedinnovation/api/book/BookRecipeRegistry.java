package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.api.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.HashMap;

public class BookRecipeRegistry {
    public static HashMap<String, IRecipe> recipes = new HashMap<>();

    public static void addRecipe(String key, IRecipe recipe) {
        recipes.put(key, recipe);
    }

    public static void addRecipe(String key, ItemStack output, Object... params) {
        ShapedOreRecipe recipe = new ShapedOreRecipe(null, output, params);
        addRecipe(key, recipe);
    }

    public static IRecipe getRecipe(String key) {
        return recipes.get(key);
    }
}
