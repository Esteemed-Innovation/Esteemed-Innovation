package flaxbeard.steamcraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.tile.TileEntityBoiler;

@SideOnly(Side.CLIENT)
public class GuiBoiler extends GuiContainer
{
    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("steamcraft:textures/gui/boiler.png");
    private TileEntityBoiler furnaceInventory;

    public GuiBoiler(InventoryPlayer par1InventoryPlayer, TileEntityBoiler par2TileEntityBoiler)
    {
        super(new ContainerBoiler(par1InventoryPlayer, par2TileEntityBoiler));
        this.furnaceInventory = par2TileEntityBoiler;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        
      
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GL11.glEnable(3042);
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        
        int i1;


        i1 = this.furnaceInventory.getBurnTimeRemainingScaled(14);
        this.drawTexturedModalRect(k + 58, l + 15 + 14 - i1, 176, 14 - i1, 14, i1);
        GL11.glDisable(3042);
        int fill = (int) ((this.furnaceInventory.getTankInfo(ForgeDirection.UP)[0].fluid.amount/10000.0F)*58.0F);
        for (int i = 0; i < 2; i++) {
	        for (int j = 0; j < 8; j++) {
	    		if (Math.min(8, fill - j*8) > 0) {
			        this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			        //this.drawTexturedModelRectFromIcon(k + 81, l + 14 + 42 - j*16 + (16 - (Math.min(16, fill - j*16))), FluidRegistry.WATER.getIcon(), 16, Math.min(16, fill - j*16));
		        	this.drawTexturedModalRect(k + 81 + i*8, l + 14 + 50 - j*8 + (8 - (Math.min(8, fill - j*8))), (int)(256*FluidRegistry.WATER.getIcon().getMinU()), (int)(256*FluidRegistry.WATER.getIcon().getMinV()), 8, Math.min(8, fill - j*8));
			        GL11.glDisable(3042);
	    		}
	    	}
        }
        
        fill = (int) ((this.furnaceInventory.steam/5000.0F)*58.0F);
        //System.out.println(this.furnaceInventory.steam);
        fill = Math.min(fill, 58);
        for (int i = 0; i < 2; i++) {
	        for (int j = 0; j < 8; j++) {
	    		if (Math.min(8, fill - j*8) > 0) {
			        this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			        //this.drawTexturedModelRectFromIcon(k + 81, l + 14 + 42 - j*16 + (16 - (Math.min(16, fill - j*16))), FluidRegistry.WATER.getIcon(), 16, Math.min(16, fill - j*16));
		        	this.drawTexturedModalRect(k + 104 + i*8, l + 14 + 50 - j*8 + (8 - (Math.min(8, fill - j*8))), (int)(256*FluidRegistry.WATER.getIcon().getMinU()), (int)(256*FluidRegistry.WATER.getIcon().getMinV()), 8, Math.min(8, fill - j*8));
			        GL11.glDisable(3042);
	    		}
	    	}
        }
        
        
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        this.drawTexturedModalRect(k + 80, l + 13, 177, 16, 18, 60);
        GL11.glDisable(3042);
    }
    
    
}
