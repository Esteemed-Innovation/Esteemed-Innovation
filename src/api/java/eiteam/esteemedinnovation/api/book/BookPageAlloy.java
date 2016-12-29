package eiteam.esteemedinnovation.api.book;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.crucible.CrucibleFormula;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class BookPageAlloy extends BookPage implements CraftingPage {
    // TODO: Package API resources with the API instead of the mod.
    private static final ResourceLocation craftSquareTexture = new ResourceLocation(Constants.EI_MODID + ":textures/gui/craftingSquare.png");
    private CrucibleLiquid output;
    private CrucibleFormula formula;
    private ItemStack[] item1;
    private ItemStack[] item2;

    public BookPageAlloy(String name, CrucibleLiquid op, CrucibleFormula form) {
        super(name);
        output = op;
        formula = form;
        List<ItemStack> ores1 = OreDictionary.getOres(OreDictionary.getOreName(OreDictionary.getOreIDs(formula.getLiquid1().getIngot())[0]));
        item1 = ores1.toArray(new ItemStack[ores1.size()]);
        List<ItemStack> ores2 = OreDictionary.getOres(OreDictionary.getOreName(OreDictionary.getOreIDs(formula.getLiquid2().getIngot())[0]));
        item2 = ores2.toArray(new ItemStack[ores2.size()]);
    }

    @Override
    public void renderPage(int x, int y, FontRenderer fontRenderer, GuiJournal book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
        book.getMC().getTextureManager().bindTexture(craftSquareTexture);

        ((Gui) book).drawTexturedModalRect(x + 45, y + 65, 0, 82, 97, 59);
        fontRenderer.setUnicodeFlag(false);
        int item1Ticks = getTicks(item1);
        int item2Ticks = getTicks(item2);
        drawItemStack(item1[item1Ticks], x + 40 + 19, y + 65 + 2, formula.getLiquid1Amount() > 1 ? Integer.toString(formula.getLiquid1Amount()) : "", renderer, fontRenderer, true);
        drawItemStack(item2[item2Ticks], x + 40 + 19, y + 65 + 20, formula.getLiquid2Amount() > 1 ? Integer.toString(formula.getLiquid2Amount()) : "", renderer, fontRenderer, true);
        drawItemStack(output.getIngot(), x + 40 + 75, y + 65 + 14, formula.getOutputAmount() > 1 ? Integer.toString(formula.getOutputAmount()) : "", renderer, fontRenderer, false);
        fontRenderer.setUnicodeFlag(true);
    }

    @Override
    public ItemStack[] getCraftedItem() {
        return new ItemStack[] {
          output.getIngot(),
          output.getNugget(),
          output.getPlate()
        };
    }
}
