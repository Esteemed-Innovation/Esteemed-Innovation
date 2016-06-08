package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.block.BlockBoiler;
import flaxbeard.steamcraft.misc.BlockContainer;
import flaxbeard.steamcraft.misc.WorldContainer;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockBoilerRenderer implements ISimpleBlockRenderingHandler {
    private RenderBlocks renderBlocks = new RenderBlocks();

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
        renderer.renderFaceXPos(block, (double) ((float) x), (double) y, (double) z, icon);
        tessellator.draw();

        icon = block.getBlockTextureFromSide(0);
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
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntityBoiler boiler = (TileEntityBoiler) world.getTileEntity(x, y, z);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (BlockSteamPipeRenderer.updateWrenchStatus() || (boiler.disguiseBlock == null || boiler.disguiseBlock == Blocks.air)) {
            renderer.renderStandardBlock(block, x, y, z);
        } else {
            GL11.glPushMatrix();
            GL11.glScalef(0.8F, 0.8F, 0.8F);
            renderer.renderStandardBlock(block, x, y, z);
            GL11.glPopMatrix();

            WorldContainer con = new WorldContainer(world, boiler.disguiseMeta);
            RenderBlocks ren2 = new RenderBlocks(con);
            ren2.setRenderBoundsFromBlock(block);
            if (boiler.disguiseMeta != 0) {
                BlockContainer cont = null;
                for (int i = 0; i < 6; i++) {
                    ren2.setOverrideBlockTexture(boiler.disguiseBlock.getIcon(i, boiler.disguiseMeta));
                    cont = new BlockContainer(boiler.disguiseBlock, i);
                    ren2.renderStandardBlock(cont, x, y, z);
                }
                cont = null;
            } else {
                ren2.renderStandardBlock(boiler.disguiseBlock, x, y, z);
            }
            ren2.clearOverrideBlockTexture();
            ren2 = null;
            con = null;
            IIcon icon = boiler.isBurning() ? ((BlockBoiler) block).camoOnIcon : ((BlockBoiler) block).camoIcon;
            renderer.setRenderAllFaces(false);
            renderer.setOverrideBlockTexture(icon);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.clearOverrideBlockTexture();

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
