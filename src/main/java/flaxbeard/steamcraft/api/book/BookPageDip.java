package flaxbeard.steamcraft.api.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.Tuple3;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;

public class BookPageDip extends BookPage {
	
    private static final ResourceLocation craftSquareTexture = new ResourceLocation("steamcraft:textures/gui/craftingSquare.png");
    private CrucibleLiquid input;
    private int amount;
    private ItemStack[] item1;
    private ItemStack inputItem;
    private ItemStack resultItem;

	public BookPageDip(String string,CrucibleLiquid ifluid, int am, ItemStack ip, ItemStack res) {
		super(string);
		input = ifluid;
		item1 = OreDictionary.getOres(OreDictionary.getOreID(input.nugget)).toArray(new ItemStack[0]);
		inputItem = ip;
		resultItem = res;
		amount = am;
	}

	@Override
	public void renderPage(int x, int y, FontRenderer fontRenderer, GuiSteamcraftBook book, RenderItem renderer, boolean isFirstPage, int mx, int my) {
		book.mc.getTextureManager().bindTexture(craftSquareTexture);
        book.drawTexturedModalRect(x+45, y+65, 0, 146, 97, 59);
        fontRenderer.setUnicodeFlag(false);
		int ticks = MathHelper.floor_double((Minecraft.getMinecraft().thePlayer.ticksExisted % (item1.length*20.0D))/20.0D);
        this.drawItemStack(item1[ticks], x+40+19+28, y+65+14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer);
        this.drawItemStack(inputItem, x+43, y+65+14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer);
        this.drawItemStack(resultItem, x+40+90, y+65+14, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer);

        //this.drawItemStack(item1[ticks], x+40+19, y+65+2, amount > 1 ? Integer.toString(amount) : "", renderer, fontRenderer);

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
