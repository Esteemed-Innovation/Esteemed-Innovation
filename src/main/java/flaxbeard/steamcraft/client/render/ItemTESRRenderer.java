package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.SteamcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;


public class ItemTESRRenderer implements IItemRenderer {
    IInventoryTESR render;
    private TileEntity dummytile;
    private boolean renderBlock = false;
    private RenderBlocks renderBlocksRi = new RenderBlocks();

    public ItemTESRRenderer(IInventoryTESR render, TileEntity dummy) {
        this.render = render;
        this.dummytile = dummy;
    }

    public ItemTESRRenderer(IInventoryTESR render, TileEntity dummy, boolean rBlock) {
        this.render = render;
        this.dummytile = dummy;
        this.renderBlock = rBlock;
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
        if (renderBlock) {
            if (type == IItemRenderer.ItemRenderType.ENTITY) {
                GL11.glTranslatef(0.5F, 0.0F, 0.5F);
            }
            if (type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
            Block block = Block.getBlockFromItem(item.getItem());
            if (block == SteamcraftBlocks.fluidSteamConverter) {
                float ringMin = 4.0F / 16.0F;
                float ringMax = 12.0F / 16.0F;
                float x = ringMin;
                float y = ringMin;
                float z = 0.0001F;
                float x2 = ringMax;
                float y2 = ringMax;
                float z2 = 1.0F;
                block.setBlockBounds(z, y, x, z2, y2, x2);
            }
            this.renderBlocksRi.renderBlockAsItem(block, 4, 1.0F);
        }
        this.render.renderInventoryTileEntityAt(dummytile, 0.0F, 0.0F, 0.0F, 0.0F);
    }
}