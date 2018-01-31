package eiteam.esteemedinnovation.api.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessRecipeFactory implements IRecipeFactory{
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        String group = JsonUtils.getString(json, "group", "");

        NonNullList<Ingredient> ings = NonNullList.create();
        for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients"))
            ings.add(CraftingHelper.getIngredient(ele, context));

        if (ings.isEmpty())
            throw new JsonParseException("No ingredients for shapeless recipe");

        ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(group.isEmpty() ? null : new ResourceLocation(group), ings, itemstack);
        BookRecipeRegistry.addRecipe(JsonUtils.getString(json, "book_key"), shapelessRecipe);

        return shapelessRecipe;
    }

    public static class ShapelessRecipe extends ShapelessOreRecipe {
        public ShapelessRecipe(ResourceLocation group, NonNullList<Ingredient> input, ItemStack result) {
            super(group, input, result);
        }
    }
}
