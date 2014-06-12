package flaxbeard.steamcraft.api.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import flaxbeard.steamcraft.misc.Tuple3;

public class BookPageAlloy extends BookPage {
	
    private static final ResourceLocation craftSquareTexture = new ResourceLocation("steamcraft:textures/gui/craftingSquare.png");
    private CrucibleLiquid output;
    private CrucibleFormula formula;
    private ItemStack[] item1;
    private ItemStack[] item2;

	public BookPageAlloy(String string,CrucibleLiquid op, CrucibleFormula form) {
		super(string);
		output = op;
		formula = form;
		item1 = OreDictionary.getOres(OreDictionary.getOreID(formula.liquid1.ingot)).toArray(new ItemStack[0]);
		item2 = OreDictionary.getOres(OreDictionary.getOreID(formula.liquid2.ingot)).toArray(new ItemStack[0]);

	}

	@Override
	public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
		book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x+45, y+65, 0, 82, 97, 59);
        fontRenderer.setUnicodeFlag(false);
		int ticks = MathHelper.floor_double((Minecraft.getMinecraft().thePlayer.ticksExisted % (item1.length*20.0D))/20.0D);
        this.drawItemStack(item1[ticks], x+40+19, y+65+2, formula.liquid1num > 1 ? Integer.toString(formula.liquid1num) : "", renderer, fontRenderer);
		ticks = MathHelper.floor_double((Minecraft.getMinecraft().thePlayer.ticksExisted % (item2.length*20.0D))/20.0D);
        this.drawItemStack(item2[ticks], x+40+19, y+65+20, formula.liquid2num > 1 ? Integer.toString(formula.liquid2num) : "", renderer, fontRenderer);
        this.drawItemStack(output.ingot, x+40+75, y+65+14, formula.output > 1 ? Integer.toString(formula.output) : "", renderer, fontRenderer);
        fontRenderer.setUnicodeFlag(true);
		
	    for (Tuple3 item : items) {
	    	int ix = (Integer) item.first;
	    	int iy = (Integer) item.second;
	    	if (mx >= ix && mx <= ix+16 && my >=iy && my <= iy+16) {
	    		fontRenderer.setUnicodeFlag(false);
	    		book.renderToolTip((ItemStack) item.third, mx, my);
	    		fontRenderer.setUnicodeFlag(true);
	    	}
	    }
        items.clear();
	}
}
