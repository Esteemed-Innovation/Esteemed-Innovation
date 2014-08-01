package flaxbeard.steamcraft.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.block.BlockBoiler;
import flaxbeard.steamcraft.misc.WorldContainer;
import flaxbeard.steamcraft.tile.TileEntityBoiler;

public class BlockBoilerRenderer implements ISimpleBlockRenderingHandler {
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		int x = 0;
		int y = 0;
		int z = 0;
	    Tessellator tessellator = Tessellator.instance;
		IIcon icon = block.getBlockTextureFromSide(3);

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, (double)((float)x), (double)y, (double)z, icon);
        tessellator.draw();
        
		icon = block.getBlockTextureFromSide(0);
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, (double)((float)x), (double)y, (double)z, icon);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, icon);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, icon);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, icon);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, icon);
        tessellator.draw();
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		TileEntityBoiler boiler = (TileEntityBoiler) world.getTileEntity(x, y, z);
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (BlockSteamPipeRenderer.updateWrenchStatus() || (boiler.disguiseBlock == null || boiler.disguiseBlock == Blocks.air)) {
			renderer.renderStandardBlock(block, x, y, z);
		}
		else
		{
			WorldContainer con = new WorldContainer(world,boiler.disguiseMeta);
			RenderBlocks ren2 = new RenderBlocks(con);
			ren2.setRenderBoundsFromBlock(block);
		    if (boiler.disguiseMeta != 0) {
		    	ren2.setOverrideBlockTexture(boiler.disguiseBlock.getIcon(0, boiler.disguiseMeta));
		    }
		    ren2.renderStandardBlock(boiler.disguiseBlock, x, y, z);
		    ren2.clearOverrideBlockTexture();
		    ren2 = null;
		    con = null;
		    IIcon icon = boiler.isBurning() ? ((BlockBoiler)block).camoOnIcon : ((BlockBoiler)block).camoIcon;
		    int meta = world.getBlockMetadata(x, y, z);
		    switch (meta) {
	    		case 2:
	    			renderer.renderFaceZNeg(block, x, y, z, icon);
	    			break;
		    	case 5:
		    		renderer.renderFaceXPos(block, x, y, z, icon);
		    		break;
	    		case 3:
	    			renderer.renderFaceZPos(block, x, y, z, icon);
	    			break;
		    	case 4:
		    		renderer.renderFaceXNeg(block, x, y, z, icon);
		    		break;
		    }
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return Steamcraft.boilerRenderID;
	}

}
