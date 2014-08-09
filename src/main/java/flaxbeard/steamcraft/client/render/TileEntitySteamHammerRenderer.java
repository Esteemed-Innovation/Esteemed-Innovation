package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import flaxbeard.steamcraft.client.render.model.ModelHammer;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;

public class TileEntitySteamHammerRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {

	private static final ModelHammer model = new ModelHammer();
	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/hammer.png");
	private static float px = (1.0F/16.0F);
	
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double x, double y,
			double z, float var8) {

		GameSettings settings = Minecraft.getMinecraft().gameSettings;

		TileEntitySteamHammer hammer = (TileEntitySteamHammer) var1;
		int meta = hammer.getWorldObj().getBlockMetadata(var1.xCoord,var1.yCoord,var1.zCoord);
		////Steamcraft.log.debug(meta);
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x, y, z);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(90.0F*(meta+(meta%2*2)), 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		model.renderNoRotate();
		GL11.glTranslatef(0, 10*px, px);
		int ticks = (hammer.hammerTicks)%360;
		if (ticks <= 30) {
			ticks = ticks*6;
		}
		else if (ticks > 30 && ticks < 40) {
			ticks = 180+(ticks-30);
		}
		else if (ticks >= 40 && ticks < 50) {
			ticks = 180+10-(ticks-40);
		}
		else if (ticks >= 50 && ticks < 180) {
			ticks = 180;
		}
		float sin = MathHelper.sin((float) Math.toRadians(ticks-60));
		GL11.glRotatef(40+sin*(42.5F+9.5F*(1-sin))-52F, 1F, 0F, 0F);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F,1.0F,(13.0F+((int) (sin*42.5F-42.5F))/9.0F)/13.0F);
		model.renderConnector((int) (sin*42.5F-42.5F));
		GL11.glPopMatrix();

		GL11.glRotatef(40+sin*(42.5F+9.5F*(1-sin))-52F, -1F, 0F, 0F);
		GL11.glTranslatef(0, -6*px, 0);
		GL11.glRotatef(10+sin*42.5F-42.5F, 1F, 0F, 0F);

		model.render();
		GL11.glPopMatrix();
		
		if (hammer.getStackInSlot(0) != null) {
		    GL11.glPushMatrix();
			GL11.glTranslatef((float)x + 0.5F, (float)y + (1.0F/16.0F) - 0.02F, (float)z + 0.5F);
			GL11.glScalef(2.0F, 2.0F, 2.0F);
			GL11.glRotatef(0.0F+90.0F*meta+(meta % 2 == 0 ? 180.0F : 0.0F), 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.0F, -(7.0F/32.0F), 0.0F);
	        ItemStack is = hammer.getStackInSlot(0).copy();
	        is.stackSize = 1;
			EntityItem item = new EntityItem(var1.getWorldObj(), 0.0F, 0.0F, 0.0F,is);
			item.hoverStart = 0.0F;
			boolean fancy = settings.fancyGraphics;
			settings.fancyGraphics = true;
		    RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
		    settings.fancyGraphics = fancy;
		    GL11.glPopMatrix();
		}
	}
	
	@Override
	public void renderInventoryTileEntityAt(TileEntity var1, double x,
			double y, double z, float var8) {
		TileEntitySteamHammer hammer = (TileEntitySteamHammer) var1;
		int meta = 3;
		////Steamcraft.log.debug(meta);
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x, y, z);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(90.0F*(meta+(meta%2*2)), 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		model.renderNoRotate();
		GL11.glTranslatef(0, 10*px, px);
		int ticks = (Minecraft.getMinecraft().thePlayer.ticksExisted*5)%360;
		if (ticks <= 30) {
			ticks = ticks*6;
		}
		else if (ticks > 30 && ticks < 40) {
			ticks = 180+(ticks-30);
		}
		else if (ticks >= 40 && ticks < 50) {
			ticks = 180+10-(ticks-40);
		}
		else if (ticks >= 50 && ticks < 180) {
			ticks = 180;
		}
		float sin = MathHelper.sin((float) Math.toRadians(ticks-60));
		GL11.glRotatef(40+sin*(42.5F+9.5F*(1-sin))-52F, 1F, 0F, 0F);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F,1.0F,(13.0F+((int) (sin*42.5F-42.5F))/9.0F)/13.0F);
		model.renderConnector((int) (sin*42.5F-42.5F));
		GL11.glPopMatrix();
		GL11.glRotatef(40+sin*(42.5F+9.5F*(1-sin))-52F, -1F, 0F, 0F);
		GL11.glTranslatef(0, -6*px, 0);
		GL11.glRotatef(10+sin*42.5F-42.5F, 1F, 0F, 0F);

		model.render();
		GL11.glPopMatrix();
	}

}
