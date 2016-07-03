package flaxbeard.steamcraft.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.tile.TileEntitySteamHeater;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class BlockSteamHeaterRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float x = 0;
        float y = 0;
        float z = 0;
        IIcon icon = block.getBlockTextureFromSide(1);
        IIcon icon2 = block.getBlockTextureFromSide(0);
        float baseMin = 5.0F / 16.0F;
        float baseMax = 11.0F / 16.0F;
        float ringMin = 4.0F / 16.0F;
        float ringMax = 12.0F / 16.0F;
        float px = 1.0F / 16.0F;
        float minY = 0.0F + 2 * px;
        float maxY = baseMax;
        float minX = baseMin;
        float maxX = baseMax;
        float minZ = baseMin;
        float maxZ = baseMax;

        block.setBlockBounds(baseMin, minY, baseMin, baseMax, maxY, baseMax);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        block.setBlockBounds(ringMin, 0.0F, ringMin, ringMax, minY, ringMax);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).sideIcon, block, renderer);

        renderer.clearOverrideBlockTexture();
        block.setBlockBounds(0.0F, maxY, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSidesMultiple(new IIcon[]{icon, icon, icon, icon, icon, icon2}, block, renderer);
    }

    private void drawSidesMultiple(IIcon[] icon, Block block, RenderBlocks renderer) {
        int x = 0;
        int y = 0;
        int z = 0;
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, (double) ((float) x), (double) y, (double) z, icon[0]);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, (double) ((float) x), (double) y, (double) z, icon[1]);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, (double) x, (double) y, (double) z, icon[2]);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, (double) x, (double) y, (double) z, icon[3]);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, (double) x, (double) y, (double) z, icon[4]);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, (double) x, (double) y, (double) z, icon[5]);
        tessellator.draw();
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
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntitySteamHeater heater = (TileEntitySteamHeater) world.getTileEntity(x, y, z);
        float baseMin = 5.0F / 16.0F;
        float baseMax = 11.0F / 16.0F;
        float ringMin = 4.0F / 16.0F;
        float ringMax = 12.0F / 16.0F;
        float px = 1.0F / 16.0F;
        float minX = baseMin;
        float maxX = baseMax;
        float minY = baseMin;
        float maxY = baseMax;
        float minZ = baseMin;
        float maxZ = baseMax;
        ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection direction = ForgeDirection.getOrientation(meta).getOpposite();
        for (ForgeDirection dir : ForgeDirection.values()) {
            if (dir != direction.getOpposite()) {
                if (world.getTileEntity(heater.xCoord + dir.offsetX, heater.yCoord + dir.offsetY, heater.zCoord + dir.offsetZ) != null) {
                    TileEntity tile = world.getTileEntity(heater.xCoord + dir.offsetX, heater.yCoord + dir.offsetY, heater.zCoord + dir.offsetZ);
                    if (tile instanceof ISteamTransporter) {
                        ISteamTransporter target = (ISteamTransporter) tile;
                        if (target.doesConnect(dir.getOpposite())) {
                            myDirections.add(dir);
                            if (dir.offsetX == 1) {
                                maxX = 1.0F - 2 * px;
                            }
                            if (dir.offsetY == 1) {
                                maxY = 1.0F - 2 * px;
                            }
                            if (dir.offsetZ == 1) {
                                maxZ = 1.0F - 2 * px;
                            }
                            if (dir.offsetX == -1) {
                                minX = 0.0F + 2 * px;
                            }
                            if (dir.offsetY == -1) {
                                minY = 0.0F + 2 * px;
                            }
                            if (dir.offsetZ == -1) {
                                minZ = 0.0F + 2 * px;
                            }
                        }
                    }
                }
            }
        }

        if (direction == ForgeDirection.WEST) {
            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(maxX, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (direction == ForgeDirection.EAST) {
            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(0.0F, 0.0F, 0.0F, minX, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (direction == ForgeDirection.NORTH) {
            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(0.0F, 0.0F, maxZ, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (direction == ForgeDirection.SOUTH) {
            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, minZ);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (direction == ForgeDirection.DOWN) {
            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(0.0F, maxY, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (direction == ForgeDirection.UP) {
            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, minY, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }

        renderer.overrideBlockTexture = ((BlockPipe) SteamcraftBlocks.pipe).sideIcon;
        if (minX == 2 * px) {
            block.setBlockBounds(0.0F, ringMin, ringMin, minX, ringMax, ringMax);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (maxX == 1.0F - 2 * px) {
            block.setBlockBounds(maxX, ringMin, ringMin, 1.0F, ringMax, ringMax);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (minY == 2 * px) {
            block.setBlockBounds(ringMin, 0.0F, ringMin, ringMax, minY, ringMax);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);

        }
        if (maxY == 1.0F - 2 * px) {
            block.setBlockBounds(ringMin, maxY, ringMin, ringMax, 1.0F, ringMax);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (minZ == 2 * px) {
            block.setBlockBounds(ringMin, ringMin, 0.0F, ringMax, ringMax, minZ);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (maxZ == 1.0F - 2 * px) {
            block.setBlockBounds(ringMin, ringMin, maxZ, ringMax, ringMax, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
        }
        renderer.clearOverrideBlockTexture();

        int type = -1;
        if (myDirections.size() == 1) {
            renderer.overrideBlockTexture = ((BlockPipe) SteamcraftBlocks.pipe).sideIcon;
            minX = minX - px;
            maxX = maxX + px;
            minY = minY - px;
            maxY = maxY + px;
            minZ = minZ - px;
            maxZ = maxZ + px;
        }
        block.setBlockBounds(minX, baseMin, baseMin, maxX, baseMax, baseMax);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        block.setBlockBounds(baseMin, baseMin, minZ, baseMax, baseMax, maxZ);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        block.setBlockBounds(baseMin, minY, baseMin, baseMax, maxY, baseMax);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.clearOverrideBlockTexture();
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return Steamcraft.heaterRenderID;
    }

}
