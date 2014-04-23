package flaxbeard.steamcraft.api.book;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class BookPageCrafting extends BookPage {
	
    private static final ResourceLocation craftSquareTexture = new ResourceLocation("steamcraft:textures/gui/craftingSquare.png");
    private ItemStack output;
    private ItemStack[] inputs;

	public BookPageCrafting(String string,ItemStack op, ItemStack... ip) {
		super(string);
		output = op;
		inputs = ip;
	}

	@Override
	public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage) {
		book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x+45, y+55, 0, 0, 97, 59);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	if (inputs.length>(3*i) + j) {
            		if (!(inputs[(3*i)+j] == null)) {
			            fontRenderer.setUnicodeFlag(false);
			            this.drawItemStack(inputs[(3*i)+j], x+49+j*19, y+59+i*19, inputs[(3*i)+j].stackSize > 1 ? Integer.toString(inputs[(3*i)+j].stackSize) : "", renderer, fontRenderer);
			            fontRenderer.setUnicodeFlag(true);
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
