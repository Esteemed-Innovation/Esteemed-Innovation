package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.client.render.model.ModelChargingPad;
import flaxbeard.steamcraft.tile.TileEntityChargingPad;

public class TileEntityChargingPadRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
	private static final ModelChargingPad model = new ModelChargingPad();

	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/charger.png");

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
		TileEntityChargingPad pad = (TileEntityChargingPad) var1;


		
		GL11.glPushMatrix();
		GL11.glTranslatef((float)var2+0.5F, (float)var4+0.5F, (float)var6+0.5F);
		int meta = var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
		int rotation = 0;
		switch (meta) {
		case 2:
			rotation = 90;
			break;
		case 3:
			rotation = 270;
			break;
		case 4:
			rotation = 180;
			break;
		case 5:
			rotation = 0;
			break;
		}
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		GL11.glRotatef(90.0F, 0F, 1F, 0F);

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//System.out.println(pad.extendTicks);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		GL11.glTranslated(0.0F, 0.32D+0.95D*Math.sin(Math.toRadians((90D/40D)*pad.extendTicks)), 0.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		model.render(pad.extendTicks);
		
		GL11.glPopMatrix();

	}


	@Override
	public void renderInventoryTileEntityAt(TileEntity var1, double x,
			double y, double z, float var8) {
		GL11.glPushMatrix();
		int meta = 4;
		int rotation = 0;
		switch (meta) {
		case 2:
			rotation = 90;
			break;
		case 3:
			rotation = 270;
			break;
		case 4:
			rotation = 180;
			break;
		case 5:
			rotation = 0;
			break;
		}
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		GL11.glRotatef(90.0F, 0F, 1F, 0F);

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//System.out.println(pad.extendTicks);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		GL11.glTranslated(0.0F, 0.32D+0.95D*Math.sin(Math.toRadians((90D/40D)*0)), 0.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		model.render(0);
		
		GL11.glPopMatrix();

	}

}
