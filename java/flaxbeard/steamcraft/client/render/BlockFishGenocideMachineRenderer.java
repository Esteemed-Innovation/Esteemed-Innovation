package flaxbeard.steamcraft.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.block.BlockFishGenocideMachine;

public class BlockFishGenocideMachineRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		float px = 1.0F/16.0F;
		float halfpx = 1.0F/32.0F;
		IIcon icon = block.getBlockTextureFromSide(1);
		IIcon icon2 = block.getBlockTextureFromSide(2);
		int meta = 0;

	    Tessellator tessellator = Tessellator.instance;
	    
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, (double)0,0,0, icon2);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, (double)0,0,0, icon2);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0,0,0, icon2);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0,0,0, icon2);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0,0,0, icon);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0,0,0, icon);
        tessellator.draw();

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        
		float px = 1.0F/16.0F;
		float halfpx = 1.0F/32.0F;
		int meta = world.getBlockMetadata(x, y, z);
	    renderer.renderStandardBlock(block, x, y, z);
	    GL11.glEnable(GL11.GL_BLEND);
	    if (((BlockFishGenocideMachine)block).pass == 1) {
		    if (world.getBlock(x+1, y, z) == Blocks.water || world.getBlock(x, y, z-1) == Blocks.water || world.getBlock(x, y, z+1) == Blocks.water || world.getBlock(x-1, y, z) == Blocks.water) {
		    	block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		    	renderer.renderStandardBlock(Blocks.water, x, y, z);
		    }
	    }
	    GL11.glDisable(GL11.GL_BLEND);

	    return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return Steamcraft.genocideRenderID;
	}

}
