package flaxbeard.steamcraft.api.book;

import cpw.mods.fml.relauncher.ReflectionHelper;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;

public class BookPageCrafting extends BookPage implements ICraftingPage {

    private static final ResourceLocation craftSquareTexture = new ResourceLocation("steamcraft:textures/gui/craftingSquare.png");
    private ItemStack output;
    private Object[] inputs = new Object[9];
    private boolean shapeless = false;
    private IRecipe[] recipe;


    public BookPageCrafting(String string, boolean shape, ItemStack op, Object... ip) {
        super(string);
        output = op;
        inputs = ip;
        shapeless = shape;
    }


    public BookPageCrafting(String string, ItemStack op, Object... ip) {
        super(string);
        output = op;
        inputs = ip;
    }

    public BookPageCrafting(String string, String... keys) {
        this(string, getRecipes(keys));
    }

    public BookPageCrafting(String string, IRecipe... recipes) {
        super(string);
        output = recipes[0].getRecipeOutput();
        for (IRecipe recipe : recipes) {
            if (recipe instanceof ShapedOreRecipe) {
                for (int i = 0; i < 9; i++) {
                    ArrayList newList = new ArrayList();
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
                    ArrayList newList = new ArrayList();
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
                inputs = ArrayUtils.addAll(inputs, ((ShapelessRecipes) recipe).recipeItems.toArray(new Object[0]));
            } else if (recipe instanceof ShapelessOreRecipe) {
                shapeless = true;
                for (int i = 0; i < 9; i++) {
                    ArrayList newList = new ArrayList();
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
        for (int i = 0; i < 9; i++) {
            if (inputs[i] instanceof ArrayList) {
                ArrayList<ItemStack> seen = new ArrayList<ItemStack>();
                for (ItemStack input : ((ArrayList<ItemStack>) inputs[i])) {

                }
            }
        }
        recipe = recipes;
    }

    public static IRecipe[] getRecipes(String... keys) {
        ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();
        for (String key : keys) {
            recipes.add(BookRecipeRegistry.getRecipe(key));
        }
        return recipes.toArray(new IRecipe[0]);
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x + 45, y + 55, 0, 0, 97, 59);
        if (shapeless) {
            book.drawTexturedModalRect(x + 120, y + 60, 100, 0, 17, 13);
        }
        int maxX = 3;
        int maxY = 3;
        if (recipe != null && recipe[0] != null && recipe[0] instanceof ShapedOreRecipe) {
            maxX = (Integer) ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) recipe[0], 4);
            maxY = (Integer) ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) recipe[0], 5);

        }
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                if (inputs.length > (maxX * i) + j) {
                    if (!(inputs[(maxX * i) + j] == null)) {
                        if (inputs[(maxX * i) + j] instanceof Item) {
                            ItemStack item = new ItemStack((Item) inputs[(maxX * i) + j]);
                            fontRenderer.setUnicodeFlag(false);
                            this.drawItemStack(item, x + 49 + j * 19, y + 59 + i * 19, "", renderer, fontRenderer, true);
                            fontRenderer.setUnicodeFlag(true);
                        }
                        if (inputs[(maxX * i) + j] instanceof ItemStack) {
                            ItemStack item = (ItemStack) inputs[(maxX * i) + j];
                            fontRenderer.setUnicodeFlag(false);
                            this.drawItemStack(item, x + 49 + j * 19, y + 59 + i * 19, "", renderer, fontRenderer, true);
                            fontRenderer.setUnicodeFlag(true);
                        }
                        if (inputs[(maxX * i) + j] instanceof ItemStack[]) {
                            ItemStack[] item = (ItemStack[]) inputs[(maxX * i) + j];
                            int ticks = MathHelper.floor_double((Minecraft.getMinecraft().thePlayer.ticksExisted % (item.length * 20.0D)) / 20.0D);
                            fontRenderer.setUnicodeFlag(false);
                            this.drawItemStack(item[ticks], x + 49 + j * 19, y + 59 + i * 19, "", renderer, fontRenderer, true);
                            fontRenderer.setUnicodeFlag(true);
                        }
                        if (inputs[(maxX * i) + j] instanceof ArrayList && ((ArrayList) inputs[(maxX * i) + j]).size() > 0) {

                            ArrayList<ItemStack> list2 = new ArrayList<ItemStack>();
                            for (ItemStack item : ((ArrayList<ItemStack>) inputs[(maxX * i) + j])) {
                                if (item.getItemDamage() == 32767) {
                                    ArrayList list = new ArrayList<ItemStack>();
                                    item.getItem().getSubItems(item.getItem(), null, list);
                                    for (Object item2 : list) {
                                        list2.add((ItemStack) item2);
                                    }
                                } else {
                                    list2.add(item);
                                }
                            }
                            ItemStack[] item = list2.toArray(new ItemStack[0]);
                            int ticks = MathHelper.floor_double((Minecraft.getMinecraft().thePlayer.ticksExisted % (item.length * 20.0D)) / 20.0D);
                            fontRenderer.setUnicodeFlag(false);
                            this.drawItemStack(item[ticks], x + 49 + j * 19, y + 59 + i * 19, "", renderer, fontRenderer, true);
                            fontRenderer.setUnicodeFlag(true);
                        }


                    }
                }
            }
        }
        fontRenderer.setUnicodeFlag(false);
        this.drawItemStack(output, x + 45 + 76, y + 55 + 23, output.stackSize > 1 ? Integer.toString(output.stackSize) : "", renderer, fontRenderer, false);
        fontRenderer.setUnicodeFlag(true);
        if (shapeless) {
            int ix = x + 120;
            int iy = y + 60;
            if (mx >= ix && mx <= ix + 17 && my >= iy && my <= iy + 13) {
                fontRenderer.setUnicodeFlag(false);
                book.renderText("steamcraft.book.shapeless", mx, my);
                fontRenderer.setUnicodeFlag(true);
            }
        }
    }

    @Override
    public ItemStack[] getCraftedItem() {
        return new ItemStack[]{output};
    }
}
