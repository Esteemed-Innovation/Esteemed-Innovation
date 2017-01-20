package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.crucible.CrucibleFormula;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.mold.MoldRegistry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BookPageAlloy extends BookPage implements CraftingPage {
    // TODO: Package API resources with the API instead of the mod.
    private static final ResourceLocation craftSquareTexture = new ResourceLocation(Constants.EI_MODID + ":textures/gui/craftingSquare.png");
    private CrucibleLiquid output;
    private CrucibleFormula formula;
    private ItemStack[] outItems;
    private ItemStack[] item1;
    private ItemStack[] item2;

    public BookPageAlloy(String name, CrucibleLiquid op, CrucibleFormula form) {
        super(name);
        output = op;
        formula = form;
        outItems = formula.getOutputLiquid().getDisplayItems(formula.getOutputAmount(), false);
        item1 = formula.getLiquid1().getDisplayItems(formula.getLiquid1Amount());
        item2 = formula.getLiquid2().getDisplayItems(formula.getLiquid2Amount());
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        book.getMC().getTextureManager().bindTexture(craftSquareTexture);

        ((Gui) book).drawTexturedModalRect(x + 45, y + 65, 0, 82, 97, 59);
        fontRenderer.setUnicodeFlag(false);
        int item1Ticks = getTicks(item1);
        int item2Ticks = getTicks(item2);
        int outTicks = getTicks(outItems);
        drawItemStack(item1[item1Ticks], x + 40 + 19, y + 65 + 2, formula.getLiquid1Amount() > 1 ? Integer.toString(formula.getLiquid1Amount()) : "", renderer, fontRenderer, true);
        drawItemStack(item2[item2Ticks], x + 40 + 19, y + 65 + 20, formula.getLiquid2Amount() > 1 ? Integer.toString(formula.getLiquid2Amount()) : "", renderer, fontRenderer, true);
        drawItemStack(outItems[outTicks], x + 40 + 75, y + 65 + 14, formula.getOutputAmount() > 1 ? Integer.toString(formula.getOutputAmount()) : "", renderer, fontRenderer, false);
        fontRenderer.setUnicodeFlag(true);
    }

    @Override
    public ItemStack[] getCraftedItem() {
        List<ItemStack> items = new ArrayList<>();
        for (Item mold : MoldRegistry.molds) {
            ItemStack out = CrucibleRegistry.getMoldingOutput(output, mold);
            if (out != null) {
                items.add(out);
            }
        }
        return items.toArray(new ItemStack[items.size()]);
    }
}
