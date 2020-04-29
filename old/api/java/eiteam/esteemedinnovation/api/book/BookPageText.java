package eiteam.esteemedinnovation.api.book;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public class BookPageText extends BookPage {
    @Nonnull
    public static String lastViewing = "";
    private String text;

    public BookPageText(String name, String string2) {
        super(name);
        text = string2;
    }

    public BookPageText(String name, String string2, boolean title) {
        super(name, title);
        text = string2;
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        super.renderPage(x, y, fontRenderer, book, renderer, isFirstPage, mx, my);
        if (!lastViewing.equals(book.getCurrentEntry())) {
            lastViewing = book.getCurrentEntry();
        }
        int yOffset = y + 30;
        if (isFirstPage || shouldDisplayTitle) {
            yOffset = y + 40;
        }

        String output = I18n.format(text).replace("\\n", "\n");
        fontRenderer.drawSplitString(output, x + 40, yOffset, 110, 0);
    }
}
