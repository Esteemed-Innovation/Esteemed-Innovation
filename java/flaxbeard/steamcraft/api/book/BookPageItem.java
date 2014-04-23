package flaxbeard.steamcraft.api.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.gui.GuiSteamcraftBook;

public class BookPageItem extends BookPageText {
	private ItemStack[] item;
	private String name;
	private String text;
	
	public BookPageItem(String string, String string2, ItemStack... is) {
		super(string, string2);
		item = is;
		text = string2;
		name = string;
	}
	
	@Override
	public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage) {
		String s;
		int l;
	  	int yOffset = y+55;
		if (isFirstPage) {
			yOffset = y+65;
			s = I18n.format(name);
			l = fontRenderer.getStringWidth(s);
		  	fontRenderer.drawString("\u00A7l"+"\u00A7n"+s, x + book.bookImageWidth/2 - l/2 - 5, y+30, 0x3F3F3F);
	    }

		s = I18n.format(text);
		String stringLeft = s;
		while (stringLeft.indexOf("<br>") != -1) {
			String output = stringLeft.substring(0, stringLeft.indexOf("<br>"));
		    l = fontRenderer.splitStringWidth(output, 110);
		    fontRenderer.drawSplitString(output, x +40, yOffset, 110, 0);
		    yOffset+=this.getSplitStringHeight(fontRenderer, output, x +40, yOffset, 110);
		    yOffset+=10;
		    stringLeft = stringLeft.substring(stringLeft.indexOf("<br>")+4, stringLeft.length());
		}
		String output = stringLeft;
	    l = fontRenderer.splitStringWidth(output, 110);
	    fontRenderer.drawSplitString(output, x +40, yOffset, 110, 0);
	    
	    int size = item.length;
	    int i = 0;
	    for (ItemStack stack : item) {
	    	this.drawItemStack(stack, (int)( x + book.bookImageWidth/2 - 12 - (size-1)*9 + i*18), isFirstPage ? y+45 : y+35, "", renderer, fontRenderer);
	    	i++;
	    }
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
