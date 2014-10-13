package flaxbeard.steamcraft.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.block.BlockRuptureDisc;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRuptureDiscRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId,
                                     RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
                                    Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        float px = 1.0F / 16.0F;
        float halfpx = 1.0F / 32.0F;
        int meta = BlockRuptureDisc.getMeta(world.getBlockMetadata(x, y, z));
        ForgeDirection dir = ForgeDirection.getOrientation(meta).getOpposite();
        setBoundsMeta(block, 4 * px, 4 * px, px, 12 * px, 12 * px, 2 * px, meta);
        if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null && world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof TileEntitySteamPipe) {
            setBoundsMeta(block, 4 * px, 4 * px, -4 * px, 12 * px, 12 * px, -3 * px, meta);
        }
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 4 * px, 4 * px, 2 * px, 5 * px, 12 * px, 3 * px, meta);
        if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null && world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof TileEntitySteamPipe) {
            setBoundsMeta(block, 4 * px, 4 * px, -3 * px, 5 * px, 12 * px, -2 * px, meta);
        }
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 11 * px, 4 * px, 2 * px, 12 * px, 12 * px, 3 * px, meta);
        if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null && world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof TileEntitySteamPipe) {
            setBoundsMeta(block, 11 * px, 4 * px, -3 * px, 12 * px, 12 * px, -2 * px, meta);
        }
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 5 * px, 4 * px, 2 * px, 11 * px, 5 * px, 3 * px, meta);
        if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null && world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof TileEntitySteamPipe) {
            setBoundsMeta(block, 5 * px, 4 * px, -3 * px, 11 * px, 5 * px, -2 * px, meta);
        }
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 5 * px, 11 * px, 2 * px, 11 * px, 12 * px, 3 * px, meta);
        if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null && world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof TileEntitySteamPipe) {
            setBoundsMeta(block, 5 * px, 11 * px, -3 * px, 11 * px, 12 * px, -2 * px, meta);
        }
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 5 * px, 5 * px, 0.0F, 11 * px, 11 * px, px, meta);
        if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null && world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof TileEntitySteamPipe) {
            setBoundsMeta(block, 5 * px, 5 * px, -5 * px, 11 * px, 11 * px, -4 * px, meta);
        }
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);


        return true;
    }

    private void setBoundsMeta(Block block, float x, float y, float z, float x2, float y2, float z2, int meta) {
        switch (meta) {
            case 5:
                block.setBlockBounds(z, y, x, z2, y2, x2);
                break;
            case 2:
                block.setBlockBounds(1 - x2, y, 1 - z2, 1 - x, y2, 1 - z);
                break;
            case 4:
                block.setBlockBounds(1 - z2, y, 1 - x2, 1 - z, y2, 1 - x);
                break;
            case 3:
                block.setBlockBounds(x, y, z, x2, y2, z2);
                break;

        }
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return Steamcraft.gaugeRenderID;
    }

}
