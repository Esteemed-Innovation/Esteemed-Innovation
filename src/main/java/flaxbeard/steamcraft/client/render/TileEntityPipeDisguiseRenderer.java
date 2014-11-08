package flaxbeard.steamcraft.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPipeDisguiseRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
//		TileEntitySteamPipe pipe = (TileEntitySteamPipe) te;
//		Minecraft mc = Minecraft.getMinecraft();
//		GL11.glPushMatrix();
//		GL11.glTranslatef((float)x+0.5F, (float)y+0.5F, (float)z+0.5F);
//		GL11.glScalef(1.0025F, 1.0025F, 1.0025F);
//		GL11.glTranslatef(-pipe.xCoord-0.5F, -pipe.yCoord-0.5F, -pipe.zCoord-0.5F);
//
//		if (pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.air && (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemWrench))) {
//			WorldContainer wc = new WorldContainer(pipe.getWorldObj(),pipe.disguiseMeta);
//			RenderBlocks renderBlocks = new RenderBlocks(wc);
//			renderBlocks.renderAllFaces = true;
//			try
//			{
//				Block block = pipe.disguiseBlock;
//				GL11.glPushMatrix();
//				Tessellator tess = Tessellator.instance;
//				tess.startDrawingQuads();
//				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
//				boolean rendered = renderBlocks.renderBlockByRenderType(block, pipe.xCoord, pipe.yCoord, pipe.zCoord);
//				tess.draw();
//				GL11.glPopMatrix();
//			}
//			catch (Exception e)
//			{
//				
//			}
//			renderBlocks = null;
//			wc = null;
//		}
//		GL11.glPopMatrix();
    }

}
