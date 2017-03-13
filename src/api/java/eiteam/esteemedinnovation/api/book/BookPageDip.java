package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BookPageDip extends BookPage implements CraftingPage {
    private static final ResourceLocation craftSquareTexture = new ResourceLocation(Constants.EI_MODID + ":textures/gui/book_crafting.png");
    private CrucibleLiquid input;
    private int liquidAmount;
    private ItemStack[] fluidItemRepresentation;
    private ItemStack inputItem;
    private ItemStack resultItem;

    public BookPageDip(String name, CrucibleLiquid ifluid, int am, ItemStack ip, ItemStack res) {
        super(name);
        input = ifluid;
        fluidItemRepresentation = input.getDisplayItems(am);
        inputItem = ip;
        resultItem = res;
        liquidAmount = am;
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        book.getMC().getTextureManager().bindTexture(craftSquareTexture);
        ((Gui) book).drawTexturedModalRect(x + 45, y + 65, 0, 146, 97, 59);
        fontRenderer.setUnicodeFlag(false);
        int ticks = getTicks(fluidItemRepresentation);
        drawItemStack(fluidItemRepresentation[ticks], x + 40 + 19 + 28, y + 65 + 14, liquidAmount > 1 ? Integer.toString(liquidAmount) : "", renderer, fontRenderer, true);
        drawItemStack(inputItem, x + 43, y + 65 + 14, inputItem.stackSize > 1 ? Integer.toString(liquidAmount) : "", renderer, fontRenderer, true);
        drawItemStack(resultItem, x + 40 + 90, y + 65 + 14, resultItem.stackSize > 1 ? Integer.toString(liquidAmount) : "", renderer, fontRenderer, false);

        //this.drawItemStack(fluidItemRepresentation[ticks], x+40+19, y+65+2, liquidAmount > 1 ? Integer.toString(liquidAmount) : "", renderer, fontRenderer);

        fontRenderer.setUnicodeFlag(true);
    }

    @Override
    public ItemStack[] getCraftedItem() {
        return new ItemStack[] { resultItem };
    }
}
