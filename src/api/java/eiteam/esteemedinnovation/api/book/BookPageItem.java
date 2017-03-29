package eiteam.esteemedinnovation.api.book;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class BookPageItem extends BookPageText {
    private ItemStack[] item;
    private String name;
    private String text;
    private Object[] format;

    public BookPageItem(String name, String text, ItemStack... is) {
        super(name, text);
        item = is;
        this.text = text;
        this.name = name;
    }

    public BookPageItem(String name, String text, boolean title, ItemStack... is) {
        super(name, text, title);
        item = is;
        this.name = name;
        this.text = text;
    }

    public BookPageItem(String name, String text, Object[] format, boolean title, ItemStack... is) {
        super(name, text, title);
        item = is;
        this.text = text;
        this.name = name;
        this.format = format;
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        if (!lastViewing.equals(book.getCurrentEntry())) {
            lastViewing = book.getCurrentEntry();
        }
        int yOffset = y + 55;
        if (isFirstPage || shouldDisplayTitle) {
            yOffset = y + 65;
            String s = I18n.format(name);
            int l = fontRenderer.getStringWidth(s);
            fontRenderer.drawString("\u00A7l" + "\u00A7n" + s, (int) (x + GuiJournal.BOOK_IMAGE_WIDTH / 2F - (l / 1.6) - 3),
              y + 30, 0x3F3F3F);
        }

        String output = format == null ? I18n.format(text) : I18n.format(text, format);
        output = output.replace("\\n", "\n");

        fontRenderer.drawSplitString(output, x + 40, yOffset, 110, 0);

        int size = item.length;
        int i = 0;
        for (ItemStack stack : item) {
            drawItemStack(stack.copy(), x + GuiJournal.BOOK_IMAGE_WIDTH / 2 - 12 - (size - 1) * 9 + i * 18,
              y + (isFirstPage || shouldDisplayTitle ? 45 : 35), "", renderer, fontRenderer, false);
            i++;
        }
    }
}
