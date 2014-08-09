package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.client.render.model.ModelValve;
import flaxbeard.steamcraft.tile.TileEntityValvePipe;

public class TileEntityValvePipeRenderer extends TileEntitySpecialRenderer implements IInventoryTESR{
	private static final ModelValve model = new ModelValve();
	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/blocks/blockCopper.png");


	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
			TileEntityValvePipe valve = (TileEntityValvePipe) var1;
		    GL11.glPushMatrix();
			GL11.glTranslatef((float)var2+0.5F, (float)var4+0.5F, (float)var6+0.5F);
			int meta = var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
			ForgeDirection dir = ForgeDirection.getOrientation(meta);
			float outset = 3.5F+(valve.open ? 0.0F : -1.0F)+(valve.open ? -0.1F*valve.turnTicks : 0.1F*valve.turnTicks);
			GL11.glTranslatef(outset*dir.offsetX/16.0F, outset*dir.offsetY/16.0F, outset*dir.offsetZ/16.0F);
			if (dir == ForgeDirection.UP) {
				GL11.glRotatef(-90, 0, 0, 1);
			}
			if (dir == ForgeDirection.DOWN) {
				GL11.glRotatef(90, 0, 0, 1);
			}
			if (meta == 2) {
				GL11.glRotated(-90.0F, 0, 1, 0);
			}
			if (meta == 3) {
				GL11.glRotated(90.0F, 0, 1, 0);
			}
			if (meta == 5) {
				GL11.glRotated(180.0F, 0, 1, 0);
			}
			GL11.glRotated((225.0F*(valve.isOpen() ? valve.turnTicks : 10-valve.turnTicks)/10.0F), 1, 0, 0);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);

			model.render();
			GL11.glPopMatrix();
		
	}


	@Override
	public void renderInventoryTileEntityAt(TileEntity var1, double x,
			double y, double z, float var8) {
		
	}

}
