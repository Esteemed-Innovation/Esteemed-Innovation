package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.api.Constants;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;

public class BookPageCrafting extends BookPage implements CraftingPage {
    private static final ResourceLocation craftSquareTexture = new ResourceLocation(Constants.API_MODID + ":textures/gui/book_crafting.png");
    private ItemStack output;
    private Object[] inputs = new Object[9];
    private boolean shapeless;
    private IRecipe[] recipe;

    public BookPageCrafting(String name, boolean shape, ItemStack op, Object... ip) {
        super(name);
        output = op;
        inputs = ip;
        shapeless = shape;
    }

    public BookPageCrafting(String name, ItemStack op, Object... ip) {
        super(name);
        output = op;
        inputs = ip;
    }

    public BookPageCrafting(String name, String... keys) {
        this(name, getRecipes(keys));
    }

    public BookPageCrafting(String name, IRecipe... recipes) {
        super(name);
        output = recipes[0].getRecipeOutput();
        for (IRecipe recipe : recipes) {
            if (recipe instanceof ShapedOreRecipe) {
                for (int i = 0; i < 9; i++) {
                    Collection<Object> newList = new ArrayList<>();
                    if (inputs[i] != null) {
                        if (inputs[i] instanceof Collection) {
                            newList.addAll((Collection) inputs[i]);
                        } else {
                            newList.add(inputs[i]);
                        }
                    }
                    if (((ShapedOreRecipe) recipe).getInput().length > i && ((ShapedOreRecipe) recipe).getInput()[i] != null) {
                        if (((ShapedOreRecipe) recipe).getInput()[i] instanceof Collection) {
                            newList.addAll((Collection) ((ShapedOreRecipe) recipe).getInput()[i]);
                        } else {
                            newList.add(((ShapedOreRecipe) recipe).getInput()[i]);
                        }
                    }
                    inputs[i] = newList;
                }
            } else if (recipe instanceof ShapedRecipes) {
                for (int i = 0; i < 10; i++) {
                    Collection<Object> newList = new ArrayList<>();
                    if (inputs[i] != null) {
                        if (inputs[i] instanceof Collection) {
                            newList.addAll((Collection) inputs[i]);
                        } else {
                            newList.add(inputs[i]);
                        }
                    }
                    if (((ShapedRecipes) recipe).recipeItems.length > i && ((ShapedRecipes) recipe).recipeItems[i] != null) {
                        newList.add(((ShapedRecipes) recipe).recipeItems[i]);
                    }

                    inputs[i] = newList;
                }
            } else if (recipe instanceof ShapelessRecipes) {
                shapeless = true;
                inputs = ArrayUtils.addAll(inputs, ((ShapelessRecipes) recipe).recipeItems.toArray(new Object[((ShapelessRecipes) recipe).recipeItems.size()]));
            } else if (recipe instanceof ShapelessOreRecipe) {
                shapeless = true;
                for (int i = 0; i < 9; i++) {
                    Collection<Object> newList = new ArrayList<>();
                    if (inputs[i] != null) {
                        if (inputs[i] instanceof Collection) {
                            newList.addAll((Collection) inputs[i]);
                        } else {
                            newList.add(inputs[i]);
                        }
                    }
                    if (((ShapelessOreRecipe) recipe).getInput().size() > i && ((ShapelessOreRecipe) recipe).getInput().get(i) != null) {
                        if (((ShapelessOreRecipe) recipe).getInput().get(i) instanceof Collection) {
                            newList.addAll((Collection) ((ShapelessOreRecipe) recipe).getInput().get(i));
                        } else {
                            newList.add(((ShapelessOreRecipe) recipe).getInput().get(i));
                        }
                    }
                    inputs[i] = newList;
                }
            }
        }
        recipe = recipes;
    }

    public static IRecipe[] getRecipes(String... keys) {
        ArrayList<IRecipe> recipes = new ArrayList<>();
        for (String key : keys) {
            recipes.add(BookRecipeRegistry.getRecipe(key));
        }
        return recipes.toArray(new IRecipe[recipes.size()]);
    }

    private void drawItemStackInPage(ItemStack itemStack, FontRenderer fontRenderer, int x, int j, int y, int i, RenderItem renderer) {
        fontRenderer.setUnicodeFlag(false);
        drawItemStack(itemStack, x + 49 + j * 19, y + 59 + i * 19, "", renderer, fontRenderer, true);
        fontRenderer.setUnicodeFlag(true);
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        book.getMC().getTextureManager().bindTexture(craftSquareTexture);

        ((Gui) book).drawTexturedModalRect(x + 45, y + 55, 0, 0, 97, 59);
        if (shapeless) {
            ((Gui) book).drawTexturedModalRect(x + 120, y + 60, 100, 0, 17, 13);
        }

        int maxX = 3;
        int maxY = 3;
        if (recipe != null && recipe[0] != null && recipe[0] instanceof ShapedOreRecipe) {
            maxX = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) recipe[0], 4);
            maxY = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) recipe[0], 5);
        }

        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                int index = (maxX * i) + j;
                if (inputs.length > index) {
                    Object val = inputs[index];
                    if (val != null) {
                        if (val instanceof Item) {
                            ItemStack item = new ItemStack((Item) val);
                            drawItemStackInPage(item, fontRenderer, x, j, y, i, renderer);
                        }
                        if (val instanceof ItemStack) {
                            ItemStack item = (ItemStack) val;
                            drawItemStackInPage(item, fontRenderer, x, j, y, i, renderer);
                        }
                        if (val instanceof ItemStack[]) {
                            ItemStack[] item = (ItemStack[]) val;
                            drawItemStackInPage(getStackFromTicks(item), fontRenderer, x, j, y, i, renderer);
                        }
                        if (val instanceof Collection && !((Collection) val).isEmpty()) {
                            ArrayList<ItemStack> list2 = new ArrayList<>();
                            for (Object obj : (Iterable) val) {
                                if (obj instanceof ItemStack) {
                                    ItemStack item = (ItemStack) obj;
                                    if (item.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                                        NonNullList<ItemStack> list = NonNullList.create();
                                        item.getItem().getSubItems(item.getItem(), CreativeTabs.SEARCH, list);
                                        list2.addAll(list);
                                    } else {
                                        list2.add(item);
                                    }
                                }
                            }
                            ItemStack item = getStackFromTicks(list2.toArray(new ItemStack[list2.size()]));
                            drawItemStackInPage(item, fontRenderer, x, j, y, i, renderer);
                        }
                    }
                }
            }
        }
        fontRenderer.setUnicodeFlag(false);
        drawItemStack(output, x + 45 + 76, y + 55 + 23, output.getCount() > 1 ? Integer.toString(output.getCount()) : "", renderer, fontRenderer, false);
        fontRenderer.setUnicodeFlag(true);
        if (shapeless) {
            int ix = x + 120;
            int iy = y + 60;
            if (mx >= ix && mx <= ix + 17 && my >= iy && my <= iy + 13) {
                fontRenderer.setUnicodeFlag(false);
                book.renderText("esteemedinnovation.book.shapeless", mx, my);
                fontRenderer.setUnicodeFlag(true);
            }
        }
    }

    @Override
    public ItemStack[] getCraftedItem() {
        return new ItemStack[] { output };
    }
}
