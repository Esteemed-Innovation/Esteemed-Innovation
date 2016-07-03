package flaxbeard.steamcraft.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.block.BlockSteamGauge;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class BlockSteamGaugeRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float px = 1.0F / 16.0F;
        float halfpx = 1.0F / 32.0F;
        setBoundsMeta(block, 4 * px, 4 * px, 7 * px, 12 * px, 12 * px, 9 * px, 0);
        renderer.setRenderBoundsFromBlock(block);

        int x = 0;
        int y = 0;
        int z = 0;
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, (double) ((float) x), (double) y, (double) z, ((BlockSteamGauge) block).back);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, (double) ((float) x), (double) y, (double) z, ((BlockSteamGauge) block).front);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, ((BlockSteamGauge) block).top);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, ((BlockSteamGauge) block).top);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, ((BlockSteamGauge) block).top);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, ((BlockSteamGauge) block).top);
        tessellator.draw();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        float px = 1.0F / 16.0F;
        float halfpx = 1.0F / 32.0F;
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection dir = ForgeDirection.getOrientation(meta).getOpposite();
        setBoundsMeta(block, 4 * px, 4 * px, 0.0F, 12 * px, 12 * px, px, meta);
        if (world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null && world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof TileEntitySteamPipe) {
            setBoundsMeta(block, 4 * px, 4 * px, -5 * px, 12 * px, 12 * px, -4 * px, meta);
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
