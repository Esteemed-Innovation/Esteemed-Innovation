package flaxbeard.steamcraft.api.book;

import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class BookPageDip extends BookPage implements ICraftingPage {

    private static final ResourceLocation craftSquareTexture = new ResourceLocation("steamcraft:textures/gui/craftingSquare.png");
    private CrucibleLiquid input;
    private int amount;
    private ItemStack[] item1;
    private ItemStack inputItem;
    private ItemStack resultItem;

    public BookPageDip(String string, CrucibleLiquid ifluid, int am, ItemStack ip, ItemStack res) {
        super(string);
        input = ifluid;
        item1 = OreDictionary.getOres(OreDictionary.getOreName(OreDictionary.getOreIDs(this.input.nugget)[0])).toArray(new ItemStack[0]);
        inputItem = ip;
        resultItem = res;
        amount = am;
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x + 45, y + 65, 0, 146, 97, 59);
        fontRenderer.setUnicodeFlag(false);
        int ticks = MathHelper.floor_double((Minecraft.getMinecraft().thePlayer.ticksExisted % (item1.length * 20.0D)) / 20.0D);
        this.drawItemStack(item1[ticks], x + 40 + 19 + 28, y + 65 + 14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer, true);
        this.drawItemStack(inputItem, x + 43, y + 65 + 14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer, true);
        this.drawItemStack(resultItem, x + 40 + 90, y + 65 + 14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer, false);

        //this.drawItemStack(item1[ticks], x+40+19, y+65+2, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer);

        fontRenderer.setUnicodeFlag(true);
    }

    @Override
    public ItemStack[] getCraftedItem() {
        return new ItemStack[]{resultItem};
    }
}
