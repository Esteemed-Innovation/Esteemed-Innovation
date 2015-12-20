package flaxbeard.steamcraft.api.book;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;

public class BookPageItem extends BookPageText {
    public static String lastViewing = "";
    public static int abdoName = 0;
    private ItemStack[] item;
    private String name;
    private String text;

    public BookPageItem(String string, String string2, ItemStack... is) {
        super(string, string2);
        item = is;
        text = string2;
        name = string;
    }

    public BookPageItem(String string, String string2, boolean title, ItemStack... is) {
        super(string, string2, title);
        item = is;
        text = string2;
        name = string;
    }

    public static String doLizbeth(String str) {
        String name = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
        if (name.equals("MasterAbdoTGM50")) {
            String[] abdoNames = {"Abdo", "Teku", "Tombyn", "Kryse", "Fredje", "Wesley", "Lizbeth"};
            name = abdoNames[abdoName];
        }
        str = str.replace("I am", name + " is");
        str = str.replace("my", name + "'s");
        str = str.replace("I've", name + " has");
        str = str.replace("I'll", name + " will");
        str = str.replace("I ", name + " ");
        str = str.replace("have", "has");
        str = str.replace("stumble", "stumbles");
        str = str.replace("insert", "inserts");
        str = str.replace("need", "needs");

        return str;
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        if (!lastViewing.equals(GuiSteamcraftBook.viewing)) {
            abdoName = Minecraft.getMinecraft().thePlayer.worldObj.rand.nextInt(7);
            lastViewing = GuiSteamcraftBook.viewing;
        }
        String s;
        int l;
        int yOffset = y + 55;
        if (isFirstPage || shouldDisplayTitle) {
            yOffset = y + 65;
            s = I18n.format(name);
            l = fontRenderer.getStringWidth(s);
            fontRenderer.drawString("\u00A7l" + "\u00A7n" + s, (int) (x + book.bookImageWidth / 2 - (l / 1.6) - 3), y + 30, 0x3F3F3F);
        }

        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        s = I18n.format(text);
        String stringLeft = s;
        while (stringLeft.contains("<br>")) {
            String output = stringLeft.substring(0, stringLeft.indexOf("<br>"));
            if (shouldDoLizbeth(settings.thirdPersonView, player)) {
                output = doLizbeth(output);
            }
            fontRenderer.drawSplitString(output, x + 40, yOffset, 110, 0);
            yOffset += this.getSplitStringHeight(fontRenderer, output, x + 40, yOffset, 110);
            yOffset += 10;
            stringLeft = stringLeft.substring(stringLeft.indexOf("<br>") + 4, stringLeft.length());
        }
        String output = stringLeft;
        if (shouldDoLizbeth(settings.thirdPersonView, player)) {
            output = doLizbeth(output);
        }
        fontRenderer.drawSplitString(output, x + 40, yOffset, 110, 0);

        int size = item.length;
        int i = 0;
        for (ItemStack stack : item) {
            this.drawItemStack(stack.copy(), x + book.bookImageWidth / 2 - 12 - (size - 1) * 9 + i * 18, isFirstPage || shouldDisplayTitle ? y + 45 : y + 35, "", renderer, fontRenderer, false);
            i++;
        }
    }

    private boolean shouldDoLizbeth(int view, EntityClientPlayerMP player) {
        return ((view != 0 || player.getCommandSenderName().equals("MasterAbdoTGM50")) &&
          Config.easterEggs);
    }
}
