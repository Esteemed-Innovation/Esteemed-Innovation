package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.gui.GuiJournal;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class BookPageDip extends BookPage implements ICraftingPage {
    private static final ResourceLocation craftSquareTexture = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/craftingSquare.png");
    private CrucibleLiquid input;
    private int amount;
    private ItemStack[] item1;
    private ItemStack inputItem;
    private ItemStack resultItem;

    public BookPageDip(String name, CrucibleLiquid ifluid, int am, ItemStack ip, ItemStack res) {
        super(name);
        input = ifluid;
        List<ItemStack> ores = OreDictionary.getOres(OreDictionary.getOreName(OreDictionary.getOreIDs(input.nugget)[0]));
        item1 = ores.toArray(new ItemStack[ores.size()]);
        inputItem = ip;
        resultItem = res;
        amount = am;
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x + 45, y + 65, 0, 146, 97, 59);
        fontRenderer.setUnicodeFlag(false);
        int ticks = getTicks(item1);
        drawItemStack(item1[ticks], x + 40 + 19 + 28, y + 65 + 14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer, true);
        drawItemStack(inputItem, x + 43, y + 65 + 14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer, true);
        drawItemStack(resultItem, x + 40 + 90, y + 65 + 14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer, false);

        //this.drawItemStack(item1[ticks], x+40+19, y+65+2, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer);

        fontRenderer.setUnicodeFlag(true);
    }

    @Override
    public ItemStack[] getCraftedItem() {
        return new ItemStack[] { resultItem };
    }
}
