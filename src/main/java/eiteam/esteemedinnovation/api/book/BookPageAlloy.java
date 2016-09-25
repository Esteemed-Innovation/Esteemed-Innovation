package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.crucible.CrucibleFormula;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.gui.GuiJournal;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class BookPageAlloy extends BookPage implements ICraftingPage {
    private static final ResourceLocation craftSquareTexture = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/craftingSquare.png");
    private CrucibleLiquid output;
    private CrucibleFormula formula;
    private ItemStack[] item1;
    private ItemStack[] item2;

    public BookPageAlloy(String name, CrucibleLiquid op, CrucibleFormula form) {
        super(name);
        output = op;
        formula = form;
        List<ItemStack> ores1 = OreDictionary.getOres(OreDictionary.getOreName(OreDictionary.getOreIDs(formula.liquid1.ingot)[0]));
        item1 = ores1.toArray(new ItemStack[ores1.size()]);
        List<ItemStack> ores2 = OreDictionary.getOres(OreDictionary.getOreName(OreDictionary.getOreIDs(formula.liquid2.ingot)[0]));
        item2 = ores2.toArray(new ItemStack[ores2.size()]);
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x + 45, y + 65, 0, 82, 97, 59);
        fontRenderer.setUnicodeFlag(false);
        int item1Ticks = getTicks(item1);
        int item2Ticks = getTicks(item2);
        drawItemStack(item1[item1Ticks], x + 40 + 19, y + 65 + 2, formula.liquid1num > 1 ? Integer.toString(formula.liquid1num) : "", renderer, fontRenderer, true);
        drawItemStack(item2[item2Ticks], x + 40 + 19, y + 65 + 20, formula.liquid2num > 1 ? Integer.toString(formula.liquid2num) : "", renderer, fontRenderer, true);
        drawItemStack(output.ingot, x + 40 + 75, y + 65 + 14, formula.output > 1 ? Integer.toString(formula.output) : "", renderer, fontRenderer, false);
        fontRenderer.setUnicodeFlag(true);
    }

    @Override
    public ItemStack[] getCraftedItem() {
        return new ItemStack[] {
          output.ingot,
          output.nugget,
          output.plate
        };
    }
}
