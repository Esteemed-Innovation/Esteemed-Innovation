package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import flaxbeard.steamcraft.client.render.model.ModelConveyor;
import flaxbeard.steamcraft.tile.TileEntityConveyor;

public class TileEntityConveyorRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {

	private static final ModelConveyor model = new ModelConveyor();
	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/conveyor.png");
	private static float px = (1.0F/16.0F);
	
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double x, double y,
			double z, float var8) {

		TileEntityConveyor conveyor = (TileEntityConveyor) var1;
		int meta = conveyor.getWorldObj().getBlockMetadata(var1.xCoord,var1.yCoord,var1.zCoord);
		////Steamcraft.log.debug(meta);
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x, y, z);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(90.0F*(meta+(meta%2*2)), 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		model.render();
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderInventoryTileEntityAt(TileEntity var1, double x,
			double y, double z, float var8) {
		TileEntityConveyor conveyor = (TileEntityConveyor) var1;
		int meta = 3;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x, y, z);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(90.0F*(meta+(meta%2*2)), 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		model.render();
		GL11.glPopMatrix();
	}

}
