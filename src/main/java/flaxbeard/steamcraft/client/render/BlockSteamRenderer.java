package flaxbeard.steamcraft.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.block.BlockBoiler;

public class BlockSteamRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0f);
		renderer.setRenderBoundsFromBlock(block);
		IIcon icon = block.getIcon(0, world.getBlockMetadata(x, y, z));
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		renderer.setOverrideBlockTexture(block.getIcon(0, world.getBlockMetadata(x, y, z)));
	    renderer.renderStandardBlock(block, x, y, z);
	    renderer.clearOverrideBlockTexture();

	    return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return Steamcraft.steamRenderID;
	}

}
