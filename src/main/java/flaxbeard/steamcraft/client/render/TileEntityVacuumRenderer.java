package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.client.render.model.ModelVacuum;
import flaxbeard.steamcraft.tile.TileEntityVacuum;

public class TileEntityVacuumRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
	private static final ModelVacuum model = new ModelVacuum();

	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/mortarItem.png");
	private static final ResourceLocation fanTexture = new ResourceLocation("steamcraft:textures/models/fan.png");

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
		TileEntityVacuum vacuum = (TileEntityVacuum) var1;

		GL11.glPushMatrix();
		GL11.glTranslatef((float)var2+0.5F, (float)var4+0.5F, (float)var6+0.5F);
		int meta = var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
		if (meta == 0) {
			GL11.glRotatef(-90.0F, 0F, 0F, 1F);
		}
		if (meta == 1) {
			GL11.glRotatef(90.0F, 0F, 0F, 1F);
		}
		if (meta == 3) {
			GL11.glRotatef(-90.0F, 0F, 1F, 0F);
		}
		if (meta == 2) {
			GL11.glRotatef(90.0F, 0F, 1F, 0F);
		}
		if (meta == 4) {
			GL11.glRotatef(180.0F, 0F, 1F, 0F);
		}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		Minecraft.getMinecraft().renderEngine.bindTexture(fanTexture);
		model.render();
		for (int i = 0; i<4; i++) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			
			GL11.glRotatef(90.0F*i, 1F, 0F, 0F);
			GL11.glRotatef(10.0F, 0F, 1F, 0F);

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			model.renderSide();

			GL11.glPopMatrix();
			
		}
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(-vacuum.rotateTicks*25.0F, 1F, 0F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		model.renderPole();
		for (int i = 0; i<4; i++) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			
			GL11.glRotatef(90.0F*i, 1F, 0F, 0F);
			GL11.glRotatef(10.0F, 0F, 1F, 0F);

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			model.renderBlade();

			GL11.glPopMatrix();
			
		}

		GL11.glPopMatrix();

	}


	@Override
	public void renderInventoryTileEntityAt(TileEntity var1, double x,
			double y, double z, float var8) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);

		GL11.glTranslatef(-0.5F, -0.5F,-0.5F);
		Minecraft.getMinecraft().renderEngine.bindTexture(fanTexture);

		model.render();
		for (int i = 0; i<4; i++) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			
			GL11.glRotatef(90.0F*i, 1F, 0F, 0F);
			GL11.glRotatef(10.0F, 0F, 1F, 0F);

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			model.renderSide();

			GL11.glPopMatrix();
			
		}
		model.renderPole();
		for (int i = 0; i<4; i++) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			
			GL11.glRotatef(90.0F*i, 1F, 0F, 0F);
			GL11.glRotatef(10.0F, 0F, 1F, 0F);

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			model.renderBlade();

			GL11.glPopMatrix();
			
		}
		GL11.glPopMatrix();

	}

}
