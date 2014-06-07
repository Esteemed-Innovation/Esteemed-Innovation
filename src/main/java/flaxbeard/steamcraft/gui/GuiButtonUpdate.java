package flaxbeard.steamcraft.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftItems;

@SideOnly(Side.CLIENT)
public class GuiButtonUpdate extends GuiButton
{
	private static ResourceLocation book = new ResourceLocation("steamcraft:textures/items/book.png");
    private static final String __OBFID = "CL_00000672";

    public GuiButtonUpdate(int par1, int par2, int par3)
    {
        super(par1, par2, par3, 20, 20, "");
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int x, int y)
    {
       super.drawButton(mc, x, y);
       mc.getMinecraft().getTextureManager().bindTexture(mc.getMinecraft().getTextureManager().getResourceLocation(SteamcraftItems.book.getSpriteNumber()));
       this.drawTexturedModelRectFromIcon(this.xPosition+2, this.yPosition+2, SteamcraftItems.book.getIconFromDamage(0), 16, 16);
    }
}