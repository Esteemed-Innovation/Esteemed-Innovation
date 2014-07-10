package flaxbeard.steamcraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.block.BlockBoiler;
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
    	try {
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
           // System.out.println(this.furnaceInventory.myTank.getCapacity());
            float fill = (float)(this.furnaceInventory.getTankInfo(ForgeDirection.UP)[0].fluid.amount/(float)this.furnaceInventory.myTank.getCapacity());
            drawFluid(new FluidStack(FluidRegistry.WATER,1), (int)(fill*58.0F), k + 81, l + 14, 16, 58, false);
            this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
            this.drawTexturedModalRect(k + 80, l + 13, 190, 0, 18, 60);
            fill = this.furnaceInventory.pressure;
            fill = Math.min(fill, 1.0F);
            FluidStack stack = new FluidStack(FluidRegistry.WATER,1);
            if (FluidRegistry.isFluidRegistered("steam")) {
            	stack = new FluidStack(FluidRegistry.getFluid("steam"),1);
            }
            drawFluid(stack, (int)(fill*58.0F), k + 104, l + 14, 16, 58, true);
            this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
            this.drawTexturedModalRect(k + 103, l + 13, 190, 0, 18, 60);
           
            GL11.glDisable(3042);
    	} catch (NullPointerException e){
    		//System.out.println("Did the boiler explode while the GUI was open?");
    	}
        
    }
    
    private void drawFluid(FluidStack fluid, int level, int x, int y, int width, int height, boolean steam) {
		if (fluid == null || fluid.getFluid() == null) {
			return;
		}
		IIcon icon = fluid.getFluid().getIcon(fluid);
		if (steam) {
			icon = BlockBoiler.steamIcon;
		}
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		//RenderUtils.setGLColorFromInt(fluid.getFluid().getColor(fluid));
		int fullX = width / 16;
		int fullY = height / 16;
		int lastX = width - fullX * 16;
		int lastY = height - fullY * 16;
		int fullLvl = (height - level) / 16;
		int lastLvl = (height - level) - fullLvl * 16;
		for (int i = 0; i < fullX; i++) {
			for (int j = 0; j < fullY; j++) {
				if (j >= fullLvl) {
					drawCutIcon(icon, x + i * 16, y + j * 16, 16, 16, j == fullLvl ? lastLvl : 0);
				}
			}
		}
		for (int i = 0; i < fullX; i++) {
			drawCutIcon(icon, x + i * 16, y + fullY * 16, 16, lastY, fullLvl == fullY ? lastLvl : 0);
		}
		for (int i = 0; i < fullY; i++) {
			if (i >= fullLvl) {
				drawCutIcon(icon, x + fullX * 16, y + i * 16, lastX, 16, i == fullLvl ? lastLvl : 0);
			}
		}
		drawCutIcon(icon, x + fullX * 16, y + fullY * 16, lastX, lastY, fullLvl == fullY ? lastLvl : 0);
	}

	private void drawCutIcon(IIcon icon, int x, int y, int width, int height, int cut) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x, y + height, zLevel, icon.getMinU(), icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + height, zLevel, icon.getInterpolatedU(width), icon.getInterpolatedV(height));
		tess.addVertexWithUV(x + width, y + cut, zLevel, icon.getInterpolatedU(width), icon.getInterpolatedV(cut));
		tess.addVertexWithUV(x, y + cut, zLevel, icon.getMinU(), icon.getInterpolatedV(cut));
		tess.draw();
	}
    
}
