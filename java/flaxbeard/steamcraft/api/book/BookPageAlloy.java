package flaxbeard.steamcraft.api.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;

public class BookPageAlloy extends BookPage {
	
    private static final ResourceLocation craftSquareTexture = new ResourceLocation("steamcraft:textures/gui/craftingSquare.png");
    private CrucibleLiquid output;
    private CrucibleFormula formula;

	public BookPageAlloy(String string,CrucibleLiquid op, CrucibleFormula form) {
		super(string);
		output = op;
		formula = form;
	}

	@Override
	public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage) {
		book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x+45, y+65, 0, 82, 97, 59);
        fontRenderer.setUnicodeFlag(false);
        this.drawItemStack(formula.liquid1.ingot, x+40+19, y+65+2, formula.liquid1num > 1 ? Integer.toString(formula.liquid1num) : "", renderer, fontRenderer);
        this.drawItemStack(formula.liquid2.ingot, x+40+19, y+65+20, formula.liquid2num > 1 ? Integer.toString(formula.liquid2num) : "", renderer, fontRenderer);
        this.drawItemStack(output.ingot, x+40+75, y+65+14, formula.output > 1 ? Integer.toString(formula.output) : "", renderer, fontRenderer);
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
