package flaxbeard.steamcraft.api.book;

import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import scala.Tuple4;

import java.util.ArrayList;

public class BookPage {
    protected ArrayList<Tuple4> items = new ArrayList<Tuple4>();
    protected boolean shouldDisplayTitle;
    private String name;

    public BookPage(String string) {
        name = string;
    }

    public BookPage(String string, Boolean title) {
        name = string;
        shouldDisplayTitle = title;
    }

    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        if (isFirstPage || shouldDisplayTitle) {
            String s = I18n.format(name);
            int l = fontRenderer.getStringWidth(s);
            fontRenderer.drawString("\u00A7l" + "\u00A7n" + s, (int) (x + book.bookImageWidth / 2 - (l / 1.6) - 3), y + 30, 0x3F3F3F);
        }
    }

    protected void drawItemStack(ItemStack stack, int x, int y, String str, RenderItem itemRender, FontRenderer fontRendererObj, boolean canHyperlink) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRendererObj;
        itemRender.renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
        itemRender.renderItemOverlayIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, x, y, str);
        itemRender.zLevel = 0.0F;
        items.add(new Tuple4(x, y, stack, canHyperlink));
        GL11.glPopMatrix();
    }

    public void renderPageAfter(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        for (Tuple4 item : items) {
            int ix = (Integer) item._1();
            int iy = (Integer) item._2();
            if (mx >= ix && mx <= ix + 16 && my >= iy && my <= iy + 16) {
                fontRenderer.setUnicodeFlag(false);
                book.renderToolTip((ItemStack) item._3(), mx, my, (Boolean) item._4());
                if (org.lwjgl.input.Mouse.isButtonDown(0) && (Boolean) item._4()) {
                    book.itemClicked((ItemStack) item._3());
                }
                fontRenderer.setUnicodeFlag(true);
            }
        }
        items.clear();
    }
}
