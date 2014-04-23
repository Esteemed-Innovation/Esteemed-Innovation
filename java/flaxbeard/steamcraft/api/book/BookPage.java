package flaxbeard.steamcraft.api.book;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;

public class BookPage {
	private String name;
	
	public BookPage(String string) {
		name = string;
	}
	
	public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage) {
		 if (isFirstPage) {
			 String s = I18n.format(name);
		     int l = fontRenderer.getStringWidth(s);
		     fontRenderer.drawString("\u00A7l"+"\u00A7n"+s, x + book.bookImageWidth/2 - l/2 - 5, y+30, 0x3F3F3F);
		 }
	}
}
