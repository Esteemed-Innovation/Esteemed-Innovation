package flaxbeard.steamcraft.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.block.BlockSteamCharger;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockSteamChargerRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId,
                                     RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float px = 1.0F / 16.0F;
        float halfpx = 1.0F / 32.0F;
        IIcon icon = block.getBlockTextureFromSide(1);
        IIcon icon2 = block.getBlockTextureFromSide(2);
        int meta = 0;
        //renderer.renderStandardBlock(block, x, y, z);
        setBoundsMeta(block, px, 0.0F, px, 1.0F - px, 0.5F - 2 * px, 1.0F - px, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon2, block, renderer);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0, 0, 0, ((BlockSteamCharger) block).top);
        tessellator.draw();
        setBoundsMeta(block, 0.0F, 0.5F - 2 * px, 0.0F, 1.0F, 0.5F, 1.0F, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon2, block, renderer);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0, 0, 0, ((BlockSteamCharger) block).top);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0, 0, 0, ((BlockSteamCharger) block).top);
        tessellator.draw();

        setBoundsMeta(block, 0 + halfpx, 0.5F, 6 * px + halfpx, 4 * px - halfpx, 0.5F + px, 10 * px - halfpx, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, px, 0.5F + px, 7 * px, 3 * px, 0.5F + 3 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, 0 + halfpx, 0.5F + 3 * px, 6 * px + halfpx, 4 * px - halfpx, 0.5F + 4 * px, 10 * px - halfpx, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, px, 0.5F + 4 * px, 7 * px, 3 * px, 0.5F + 7 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, 3 * px, 0.5F + 5 * px, 7 * px, 4 * px, 0.5F + 7 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, 4 * px, 0.5F + 4 * px + halfpx, 7 * px - halfpx, 5 * px, 0.5F + 7 * px + halfpx, 9 * px + halfpx, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, 5 * px, 0.5F + 5 * px, 7 * px, 6 * px, 0.5F + 7 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, 6 * px, 0.5F + 2 * px, 7 * px, 8 * px, 0.5F + 7 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, 5 * px + halfpx, 0.5F + px, 6 * px + halfpx, 9 * px - halfpx, 0.5F + 2 * px, 10 * px - halfpx, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, 6 * px, 0.5F, 0, 10 * px, 0.5F + px, px, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);
        setBoundsMeta(block, 6 * px, 0.5F + px, 0, 10 * px, 0.5F + 2 * px, 3 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        setBoundsMeta(block, 6 * px, 0.5F, 1.0F - px, 10 * px, 0.5F + px, 1.0F, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);
        setBoundsMeta(block, 6 * px, 0.5F + px, 1.0F - 3 * px, 10 * px, 0.5F + 2 * px, 1.0F, meta);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);
    }

    private void drawSides(IIcon icon, Block block, RenderBlocks renderer) {
        int x = 0;
        int y = 0;
        int z = 0;
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, (double) ((float) x), (double) y, (double) z, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, (double) ((float) x), (double) y, (double) z, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, icon);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, icon);
        tessellator.draw();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
                                    Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        float px = 1.0F / 16.0F;
        float halfpx = 1.0F / 32.0F;
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 1.0F, 0, 0);
        int meta = world.getBlockMetadata(x, y, z);
        //renderer.renderStandardBlock(block, x, y, z);
        setBoundsMeta(block, px, 0.0F, px, 1.0F - px, 0.5F - 2 * px, 1.0F - px, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.renderFaceYNeg(block, x, y, z, ((BlockSteamCharger) block).top);
        setBoundsMeta(block, 0.0F, 0.5F - 2 * px, 0.0F, 1.0F, 0.5F, 1.0F, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.setOverrideBlockTexture(((BlockSteamCharger) block).top);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.clearOverrideBlockTexture();
        // renderer.renderFaceYPos(block,x,y,z, ((BlockSteamCharger)block).top);
        //renderer.renderFaceYNeg(block,x,y,z, ((BlockSteamCharger)block).top);

        setBoundsMeta(block, 0 + halfpx, 0.5F, 6 * px + halfpx, 4 * px - halfpx, 0.5F + px, 10 * px - halfpx, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, px, 0.5F + px, 7 * px, 3 * px, 0.5F + 3 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 0 + halfpx, 0.5F + 3 * px, 6 * px + halfpx, 4 * px - halfpx, 0.5F + 4 * px, 10 * px - halfpx, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, px, 0.5F + 4 * px, 7 * px, 3 * px, 0.5F + 7 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 3 * px, 0.5F + 5 * px, 7 * px, 4 * px, 0.5F + 7 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 4 * px, 0.5F + 4 * px + halfpx, 7 * px - halfpx, 5 * px, 0.5F + 7 * px + halfpx, 9 * px + halfpx, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 5 * px, 0.5F + 5 * px, 7 * px, 6 * px, 0.5F + 7 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 6 * px, 0.5F + 2 * px, 7 * px, 8 * px, 0.5F + 7 * px, 9 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 5 * px + halfpx, 0.5F + px, 6 * px + halfpx, 9 * px - halfpx, 0.5F + 2 * px, 10 * px - halfpx, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 6 * px, 0.5F, 0, 10 * px, 0.5F + px, px, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        setBoundsMeta(block, 6 * px, 0.5F + px, 0, 10 * px, 0.5F + 2 * px, 3 * px, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        setBoundsMeta(block, 6 * px, 0.5F, 1.0F - px, 10 * px, 0.5F + px, 1.0F, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        setBoundsMeta(block, 6 * px, 0.5F + px, 1.0F - 3 * px, 10 * px, 0.5F + 2 * px, 1.0F, meta);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        GL11.glPopMatrix();

        return true;
    }

    private void setBoundsMeta(Block block, float x, float y, float z, float x2, float y2, float z2, int meta) {
        switch (meta) {
            case 0:
                block.setBlockBounds(z, y, x, z2, y2, x2);
                break;
            case 1:
                block.setBlockBounds(1 - x2, y, 1 - z2, 1 - x, y2, 1 - z);
                break;
            case 2:
                block.setBlockBounds(1 - z2, y, 1 - x2, 1 - z, y2, 1 - x);
                break;
            case 3:
                block.setBlockBounds(x, y, z, x2, y2, z2);
                break;

        }
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return Steamcraft.chargerRenderID;
    }

}
