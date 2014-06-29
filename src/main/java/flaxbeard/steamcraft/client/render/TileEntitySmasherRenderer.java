package flaxbeard.steamcraft.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.client.render.model.ModelSmasher;
import flaxbeard.steamcraft.tile.TileEntitySmasher;

public class TileEntitySmasherRenderer extends TileEntitySpecialRenderer {
	private static final ModelSmasher model = new ModelSmasher();
	private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/smasher.png");


	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
			TileEntitySmasher smasher = (TileEntitySmasher) var1;
		    GL11.glPushMatrix();
			GL11.glTranslatef((float)var2+0.5F, (float)var4+0.5F, (float)var6+0.5F);
			int meta = var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
			ForgeDirection dir = ForgeDirection.getOrientation(meta);
			switch (meta) {
			case 0:
			GL11.glRotatef(90.0F, 0F, 1F, 0F);
			break;
			case 1:
			GL11.glRotatef(90.0F, 0F, 1F, 0F);
			break;
			case 2:
			GL11.glRotatef(90.0F, 0F, 1F, 0F);
			break;
			case 3:
			GL11.glRotatef(270.0F, 0F, 1F, 0F);
			break;
			case 4:
			GL11.glRotatef(180.0F, 0F, 1F, 0F);
			break;
			case 5:
			GL11.glRotatef(0.0F, 0F, 1F, 0F);
			break;
			}
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

			Minecraft.getMinecraft().renderEngine.bindTexture(texture);

			model.renderAnchored();
			double dist = 0.0f;
			if (smasher.extendedTicks <= 5) {
				dist = (Math.sin(Math.toRadians(18D*smasher.extendedTicks))*0.51F);
			}
			else if (smasher.extendedTicks <= 15) {
				dist = 0.51F;
			}
			else
			{
				dist = 0.51F-(Math.sin(Math.toRadians(9D*(smasher.extendedTicks-15)))*0.51F);
			}
			GL11.glTranslated(dist, 0.0F, 0.0F);
			model.renderPiston(0.0F);
			RenderBlocks renderBlocks = new RenderBlocks(smasher.getWorldObj());
			renderBlocks.renderAllFaces = true;
			
			if (smasher.spinup >= 41 &&  smasher.extendedTicks < 3 && smasher.getWorldObj().getBlockMetadata(smasher.xCoord, smasher.yCoord, smasher.zCoord) % 2 == 0) {
				GL11.glTranslated(-dist, 0.0F, 0.0F);
				GL11.glTranslatef(1.5F, 1.0F,1.0F);
				GL11.glScaled(1.0F-dist*2F, 1.0F, 1.0F);
				GL11.glTranslatef(-1.5F, -1.0F,-1.0F);
				GL11.glTranslatef(1.0F, 0.0F, 0.0F);
	

				
				GL11.glTranslatef((float) - smasher.xCoord, (float) - smasher.yCoord, (float) - smasher.zCoord);
				try
				{
					Block block = smasher.smooshingBlock;
					int blockMetadata = smasher.smooshingMeta;
					GL11.glPushMatrix();
					RenderHelper.disableStandardItemLighting();
					Tessellator tess = Tessellator.instance;
					tess.startDrawingQuads();
					Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
					

					//GL11.glScalef(0.5F, 1.0F, 1.0F);
	
					GL11.glBlendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
					//renderBlocks.overrideBlockTexture = block.getIcon(blockMetadata, 0);
					boolean rendered = renderBlocks.renderBlockByRenderType(block, smasher.xCoord, smasher.yCoord, smasher.zCoord);
					//renderBlocks.overrideBlockTexture = null;
					tess.draw();
					GL11.glPopMatrix();
	
	//				if (te.copiedBlock.canRenderInPass(0))
	//				{
	//					GL14.glBlendColor(0, 0, 0, scale);
	//					rendered |= renderBlocks.renderBlockByRenderType(te.copiedBlock, x, y, z);
	//					next();
	//				}
	//				if (te.copiedBlock.canRenderInPass(1))
	//				{
	//					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	//					rendered |= renderBlocks.renderBlockByRenderType(te.copiedBlock, x, y, z);
	//					next();
	//				}
	//
	//				if (!rendered)
	//					drawShape(shape, rp);
	//
	//				GL11.glPopMatrix();
	//
	//				if (te.copiedTileEntity != null)
	//				{
	//					clean();
	//					TileEntityRendererDispatcher.instance.renderTileEntity(te.copiedTileEntity, partialTick);
	//				}
	//
	//				MalisisDoorsSettings.simpleMixedBlockRendering.set(smbr);
					renderBlocks = null;
				}
				catch (Exception e)
				{
					
				}
			}
			GL11.glPopMatrix();
	}



}
