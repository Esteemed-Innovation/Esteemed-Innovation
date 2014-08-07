package flaxbeard.steamcraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.block.BlockSteamcraftCrucible;
import flaxbeard.steamcraft.client.render.model.ModelCrucible;
import flaxbeard.steamcraft.tile.TileEntityCrucible;

@SideOnly(Side.CLIENT)
public class TileEntityCrucibleRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {

	private static final ModelCrucible model = new ModelCrucible();
	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/crucible.png");
	private static final ResourceLocation texture2 = new ResourceLocation("minecraft:textures/blocks/cobblestone.png");
	private static float px = (1.0F/16.0F);
	@Override
	public void renderTileEntityAt(TileEntity var1, double x, double y,
			double z, float var8) {

		
		TileEntityCrucible crucible = (TileEntityCrucible) var1;
		int meta = crucible.getWorldObj().getBlockMetadata(var1.xCoord,var1.yCoord,var1.zCoord);
		////Steamcraft.log.debug(meta);
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(x, y, z);

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		int ticks = crucible.tipTicks;
		Minecraft.getMinecraft().renderEngine.bindTexture(texture2);
		GL11.glRotatef(90.0F*(meta+(meta%2*2)), 0F, 1F, 0F);
		GL11.glScalef(1F, -1F, -1F);
		model.renderNoRotate();
		GL11.glScalef(1F, -1F, -1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		if (ticks > 135) {
			ticks = (int) ((ticks-90)/5.0F*90);
			GL11.glRotatef((MathHelper.sin((float) (Math.PI*(ticks/90.0F))))*5.0F, 1F, 0F, 0F);
		}
		else if (ticks > 120) {
			ticks = (int) ((ticks-90)/15.0F*90);
			GL11.glRotatef((MathHelper.sin((float) (Math.PI*(ticks/90.0F))))*15.0F, 1F, 0F, 0F);
		}
		else if (ticks > 90) {
			ticks = (int) ((ticks-90)/30.0F*90);
			GL11.glRotatef((MathHelper.sin((float) (Math.PI*(ticks/90.0F))))*30.0F, -1F, 0F, 0F);
		}
		else
		{
			GL11.glRotatef((MathHelper.sin((float) (Math.PI*(ticks/90.0F))))*75.0F, 1F, 0F, 0F);
		}
		GL11.glScalef(1F, -1F, -1F);
		
		model.renderAll();
//		ModelRenderer water = (new ModelRenderer(model, 0, 0)).setTextureSize(64, 64);
		float height = (-5.0F+(crucible.getFill()/90.0F)*11.0F)/16.0F;
//		//Steamcraft.log.debug(6.0F + (crucible.getFill()/90.0F)*-13.0F);
		if (crucible.getFill() > 0) {
			GL11.glRotatef(90.0F,1F,0F,0F);
			GL11.glTranslatef(-0.5F, -0.5F, height);
			renderLiquid(crucible);
		}

	//	UtilsFX.renderQuadFromIcon(true, icon, 1.0F, 0.0F,0.0F, 0.0F, ConfigBlocks.blockMetalDevice.getMixedBrightnessForBlock(crucible.getWorldObj(), crucible.xCoord, crucible.yCoord, crucible.zCoord), 771, 1.0F);
		GL11.glScalef(1F, -1F, -1F);
		
		GL11.glPopMatrix();
	}
	
	private void renderLiquid(TileEntityCrucible crucible) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		IIcon icon = ((BlockSteamcraftCrucible)SteamcraftBlocks.crucible).liquidIcon;
	    Tessellator tessellator = Tessellator.instance;
	    float f1 = (float) (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * 0.8);
	    float f2 = icon.getMinV();
	    float f3 = icon.getMinU();
	    float f4 = (float) (icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * 0.8);
	    float pix2 = 2.0F/16.0F;
		tessellator.startDrawingQuads();
	 //   tessellator.setBrightness(brightness);
		CrucibleLiquid liquid = crucible.contents.get(0);
		tessellator.setColorRGBA(liquid.cr, liquid.cg, liquid.cb, 256);
	//	//Steamcraft.log.debug(liquid.cr);
	    tessellator.setNormal(0.0F, 0.0F, 1.0F);
	    tessellator.addVertexWithUV(0.125D, 0.125D, 0.0D, f1, f4);
	    tessellator.addVertexWithUV(0.875D, 0.125D, 0.0D, f3, f4);
	    tessellator.addVertexWithUV(0.875D, 0.875D, 0.0D, f3, f2);
	    tessellator.addVertexWithUV(0.125D, 0.875D, 0.0D, f1, f2);
	    tessellator.draw();
	}

	@Override
	public void renderInventoryTileEntityAt(TileEntity var1, double x,
			double y, double z, float var8) {
		TileEntityCrucible crucible = (TileEntityCrucible) var1;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(x, y, z);

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture2);
		GL11.glScalef(1F, -1F, -1F);
		model.renderNoRotate();
		GL11.glScalef(1F, -1F, -1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GL11.glScalef(1F, -1F, -1F);
		
		model.renderAll();
		GL11.glScalef(1F, -1F, -1F);
		
		GL11.glPopMatrix();
	}

}
