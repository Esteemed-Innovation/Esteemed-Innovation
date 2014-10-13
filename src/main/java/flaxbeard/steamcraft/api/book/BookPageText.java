package flaxbeard.steamcraft.api.book;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;

import java.util.Iterator;
import java.util.List;

public class BookPageText extends BookPage {
    private String text;

    public BookPageText(String string, String string2) {
        super(string);
        text = string2;
    }

    public BookPageText(String string, String string2, Boolean title) {
        super(string, title);
        text = string2;
    }


    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        super.renderPage(x, y, fontRenderer, book, renderer, isFirstPage, mx, my);
        if (!BookPageItem.lastViewing.equals(book.viewing)) {
            BookPageItem.abdoName = Minecraft.getMinecraft().thePlayer.worldObj.rand.nextInt(7);
            BookPageItem.lastViewing = book.viewing;
        }
        int yOffset = y + 30;
        if (isFirstPage || shouldDisplayTitle) {
            yOffset = y + 40;
        }
        String s = I18n.format(text);
        String stringLeft = s;
        while (stringLeft.indexOf("<br>") != -1) {
            String output = stringLeft.substring(0, stringLeft.indexOf("<br>"));
            if ((Minecraft.getMinecraft().gameSettings.thirdPersonView != 0 || Minecraft.getMinecraft().thePlayer.getCommandSenderName().equals("MasterAbdoTGM50")) && Config.easterEggs) {
                output = BookPageItem.doLizbeth(output);
            }
            int l = fontRenderer.splitStringWidth(output, 110);
            fontRenderer.drawSplitString(output, x + 40, yOffset, 110, 0);
            yOffset += this.getSplitStringHeight(fontRenderer, output, x + 40, yOffset, 110);
            yOffset += 10;
            stringLeft = stringLeft.substring(stringLeft.indexOf("<br>") + 4, stringLeft.length());

        }
        String output = stringLeft;
        if ((Minecraft.getMinecraft().gameSettings.thirdPersonView != 0 || Minecraft.getMinecraft().thePlayer.getCommandSenderName().equals("MasterAbdoTGM50")) && Config.easterEggs) {
            output = BookPageItem.doLizbeth(output);
        }
        int l = fontRenderer.splitStringWidth(output, 110);
        fontRenderer.drawSplitString(output, x + 40, yOffset, 110, 0);
    }


    protected int getSplitStringHeight(FontRenderer fontRenderer, String par1Str, int par2, int par3, int par4) {
        List list = fontRenderer.listFormattedStringToWidth(par1Str, par4);
        int initialPar3 = par3;
        for (Iterator iterator = list.iterator(); iterator.hasNext(); par3 += fontRenderer.FONT_HEIGHT) {
            String s1 = (String) iterator.next();
        }
        return par3 - initialPar3;
    }
}
