package flaxbeard.steamcraft.api.book;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.gui.GuiSteamcraftBook;

public class BookPageCrafting extends BookPage {
	
    private static final ResourceLocation craftSquareTexture = new ResourceLocation("steamcraft:textures/gui/craftingSquare.png");
    private ItemStack output;
    private Object[] inputs = new Object[9];

	public BookPageCrafting(String string,ItemStack op, Object... ip) {
		super(string);
		output = op;
		inputs = ip;
	}
	
	public BookPageCrafting(String string, String... keys) {
		this(string, getRecipes(keys));
	}
	
	public static IRecipe[] getRecipes(String... keys) {
		ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();
		for (String key : keys) {
			recipes.add(BookRecipeRegistry.getRecipe(key));
		}
		return recipes.toArray(new IRecipe[0]);
	}

	public BookPageCrafting(String string, IRecipe... recipes) {
		super(string);
		output = recipes[0].getRecipeOutput();
		for (IRecipe recipe : recipes) {
			if (recipe instanceof ShapedOreRecipe) {
				for (int i = 0; i< 9; i++) {
					ArrayList newList = new ArrayList();
					if (inputs[i] != null) {
						if (inputs[i] instanceof Collection) {
							newList.addAll((Collection) inputs[i]);
						}
						else
						{
							newList.add(inputs[i]);
						}
					}
					if (((ShapedOreRecipe)recipe).getInput().length > i && ((ShapedOreRecipe)recipe).getInput()[i] != null) {
						if (((ShapedOreRecipe)recipe).getInput()[i] instanceof Collection) {
							newList.addAll((Collection) ((ShapedOreRecipe)recipe).getInput()[i]);
						}
						else
						{
							newList.add(((ShapedOreRecipe)recipe).getInput()[i]);
						}
					}
					inputs[i] = newList;
				}
			}
			else if (recipe instanceof ShapedRecipes) {
				for (int i = 0; i< 10; i++) {
					ArrayList newList = new ArrayList();
					if (inputs[i] != null) {
						if (inputs[i] instanceof Collection) {
							newList.addAll((Collection) inputs[i]);
						}
						else
						{
							newList.add(inputs[i]);
						}
					}
					if (((ShapedRecipes)recipe).recipeItems.length > i && ((ShapedRecipes)recipe).recipeItems[i] != null) {
						newList.add(((ShapedRecipes)recipe).recipeItems[i]);
						
					}
					
					inputs[i] = newList;
				}
			}
			else if (recipe instanceof ShapelessRecipes) {
				inputs = ArrayUtils.addAll(inputs, ((ShapelessRecipes)recipe).recipeItems.toArray(new Object[0]));
			}
			else if (recipe instanceof ShapelessOreRecipe) {
				inputs = ArrayUtils.addAll(inputs, ((ShapelessOreRecipe)recipe).getInput().toArray(new Object[0]));
			}
		}
		for (int i = 0; i< 9; i++) {
			if (inputs[i] instanceof ArrayList) {
				ArrayList<ItemStack> seen = new ArrayList<ItemStack>();
				for (ItemStack input : ((ArrayList<ItemStack>)inputs[i])) {
					
				}
			}
		}
	}

	@Override
	public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage) {
		book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x+45, y+55, 0, 0, 97, 59);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	if (inputs.length>(3*i) + j) {
            		if (!(inputs[(3*i)+j] == null)) {
            			if (inputs[(3*i)+j] instanceof ItemStack) {
            				ItemStack item = (ItemStack) inputs[(3*i)+j];
				            fontRenderer.setUnicodeFlag(false);
				            this.drawItemStack(item, x+49+j*19, y+59+i*19, item.stackSize > 1 ? Integer.toString(item.stackSize) : "", renderer, fontRenderer);
				            fontRenderer.setUnicodeFlag(true);
            			}
            			if (inputs[(3*i)+j] instanceof ItemStack[]) {
            				ItemStack[] item = (ItemStack[]) inputs[(3*i)+j];
            				int ticks = MathHelper.floor_double((Minecraft.getMinecraft().thePlayer.ticksExisted % (item.length*20.0D))/20.0D);
				            fontRenderer.setUnicodeFlag(false);
				            this.drawItemStack(item[ticks], x+49+j*19, y+59+i*19, item[ticks].stackSize > 1 ? Integer.toString(item[ticks].stackSize) : "", renderer, fontRenderer);
				            fontRenderer.setUnicodeFlag(true);
            			}
            			if (inputs[(3*i)+j] instanceof ArrayList && ((ArrayList)inputs[(3*i)+j]).size() > 0) {
            				
            				ArrayList<ItemStack> list2 = new ArrayList<ItemStack>();
            				for (ItemStack item : ((ArrayList<ItemStack>)inputs[(3*i)+j])) {
            					if (item.getItemDamage() == 32767) {
            						ArrayList list = new ArrayList<ItemStack>();
            						item.getItem().getSubItems(item.getItem(), null, list);
            						for (Object item2 : list) {
            							list2.add((ItemStack) item2);
            						}
            					}
            					else
            					{
            						list2.add(item);
            					}
            				}
            				ItemStack[] item = list2.toArray(new ItemStack[0]);
            				int ticks = MathHelper.floor_double((Minecraft.getMinecraft().thePlayer.ticksExisted % (item.length*20.0D))/20.0D);
				            fontRenderer.setUnicodeFlag(false);
	        				System.out.println(item.length);
				            this.drawItemStack(item[ticks], x+49+j*19, y+59+i*19, item[ticks].stackSize > 1 ? Integer.toString(item[ticks].stackSize) : "", renderer, fontRenderer);
				            fontRenderer.setUnicodeFlag(true);
            			}


            		}
            	}
            }
        }
        fontRenderer.setUnicodeFlag(false);
        this.drawItemStack(output, x+45+76, y+55+23, output.stackSize > 1 ? Integer.toString(output.stackSize) : "", renderer, fontRenderer);
        fontRenderer.setUnicodeFlag(true);

	}
	
    private void drawItemStack(ItemStack stack, int x, int y, String str, RenderItem itemRender, FontRenderer fontRendererObj)
    {
    	GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRendererObj;
        itemRender.renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
        itemRender.renderItemOverlayIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, x, y, str);
        itemRender.zLevel = 0.0F;
        GL11.glPopMatrix();
    }
}
