package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.gui.GuiJournal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;

public class BookPageItem extends BookPageText {
    public static String lastViewing = "";
    public static int abdoName;
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

    public static String doLizbeth(String str) {
        String name = Minecraft.getMinecraft().thePlayer.getDisplayNameString();
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
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        if (!lastViewing.equals(book.viewing)) {
            abdoName = Minecraft.getMinecraft().thePlayer.worldObj.rand.nextInt(7);
            lastViewing = book.viewing;
        }
        String s;
        int l;
        int yOffset = y + 55;
        if (isFirstPage || shouldDisplayTitle) {
            yOffset = y + 65;
            s = I18n.format(name);
            l = fontRenderer.getStringWidth(s);
            fontRenderer.drawString("\u00A7l" + "\u00A7n" + s, (int) (x + GuiJournal.BOOK_IMAGE_WIDTH / 2F - (l / 1.6) - 3),
              y + 30, 0x3F3F3F);
        }

        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        String output = format == null ? I18n.format(text) : I18n.format(text, format);
        output = output.replace("\\n", "\n");
        if (shouldDoLizbeth(settings.thirdPersonView, player)) {
            output = doLizbeth(output);
        }

        fontRenderer.drawSplitString(output, x + 40, yOffset, 110, 0);

        int size = item.length;
        int i = 0;
        for (ItemStack stack : item) {
            drawItemStack(stack.copy(), x + GuiJournal.BOOK_IMAGE_WIDTH / 2 - 12 - (size - 1) * 9 + i * 18,
              y + (isFirstPage || shouldDisplayTitle ? 45 : 35), "", renderer, fontRenderer, false);
            i++;
        }
    }

    private boolean shouldDoLizbeth(int view, EntityPlayerSP player) {
        return ((view != 0 || player.getDisplayNameString().equals("MasterAbdoTGM50")) && Config.easterEggs);
    }
}
