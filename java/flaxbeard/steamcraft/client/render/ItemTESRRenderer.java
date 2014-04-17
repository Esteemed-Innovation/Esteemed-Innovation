package flaxbeard.steamcraft.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;


public class ItemTESRRenderer implements IItemRenderer {
	IInventoryTESR render;
	private TileEntity dummytile;
	  
	public ItemTESRRenderer(IInventoryTESR render, TileEntity dummy) {
	    this.render = render;
	    this.dummytile = dummy;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == IItemRenderer.ItemRenderType.ENTITY)
			GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
		this.render.renderInventoryTileEntityAt(dummytile, 0.0F, 0.0F, 0.0F, 0.0F);
	}
}