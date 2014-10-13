package flaxbeard.steamcraft.api.book;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.HashMap;

public class BookRecipeRegistry {
    public static HashMap<String, IRecipe> recipes = new HashMap<String, IRecipe>();

    public static void addRecipe(String key, IRecipe recipe) {
        GameRegistry.addRecipe(recipe);
        recipes.put(key, recipe);
    }

    public static void addRecipe(String key, ItemStack output, Object... params) {
        ShapedOreRecipe recipe = new ShapedOreRecipe(output, params);
        addRecipe(key, recipe);
    }

    public static IRecipe getRecipe(String key) {
        return recipes.get(key);
    }
}
