package eiteam.esteemedinnovation.api.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import scala.Tuple4;

import java.util.ArrayList;

public class BookPage {
    protected ArrayList<Tuple4<Integer, Integer, ItemStack, Boolean>> items = new ArrayList<>();
    protected boolean shouldDisplayTitle;
    private String name;

    public BookPage(String name) {
        this.name = name;
    }

    public BookPage(String name, boolean title) {
        this.name = name;
        shouldDisplayTitle = title;
    }

    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        if (isFirstPage || shouldDisplayTitle) {
            String s = I18n.format(name);
            int l = fontRenderer.getStringWidth(s);
            fontRenderer.drawString("\u00A7l" + "\u00A7n" + s, (int) (x + GuiJournal.BOOK_IMAGE_WIDTH / 2F - (l / 1.6) - 3),
              y + 30, 0x3F3F3F);
        }
    }

    protected void drawItemStack(ItemStack stack, int x, int y, String str, RenderItem itemRender, FontRenderer fontRendererObj, boolean canHyperlink) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 32F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();
        itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        if (stack != null && stack.getItem() != null) {
            font = stack.getItem().getFontRenderer(stack);
        }
        if (font == null) {
            font = fontRendererObj;
        }
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        itemRender.renderItemOverlayIntoGUI(font, stack, x, y, str);
        itemRender.zLevel = 0.0F;
        items.add(new Tuple4<>(x, y, stack, canHyperlink));
        GlStateManager.popMatrix();
    }

    public void renderPageAfter(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
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

    public int getTicks(ItemStack[] stacks) {
        return MathHelper.floor((Minecraft.getMinecraft().player.ticksExisted % (stacks.length * 20D)) / 20D);
    }

    public ItemStack getStackFromTicks(ItemStack[] stacks) {
        return stacks[getTicks(stacks)];
    }
}
