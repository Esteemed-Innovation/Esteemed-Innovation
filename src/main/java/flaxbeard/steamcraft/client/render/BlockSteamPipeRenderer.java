package flaxbeard.steamcraft.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.IPipeWrench;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.item.ItemWrench;
import flaxbeard.steamcraft.misc.BlockContainer;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class BlockSteamPipeRenderer implements ISimpleBlockRenderingHandler {

    public static boolean updateWrenchStatus() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        Item equipped = player.getCurrentEquippedItem() != null ? player.getCurrentEquippedItem().getItem() : null;
        return (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IPipeWrench && ((IPipeWrench) equipped).canWrench(player, (int) player.posX, (int) player.posY, (int) player.posZ) && !player.isSneaking());
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float x = 0;
        float y = 0;
        float z = 0;
        IIcon icon = block.getBlockTextureFromSide(0);
        float baseMin = 5.0F / 16.0F;
        float baseMax = 11.0F / 16.0F;
        float ringMin = 4.0F / 16.0F;
        float ringMax = 12.0F / 16.0F;
        float px = 1.0F / 16.0F;
        float minX = 0.0F + 2 * px;
        float maxX = 1.0F - 2 * px;
        float minY = baseMin;
        float maxY = baseMax;
        float minZ = baseMin;
        float maxZ = baseMax;

        block.setBlockBounds(minX, baseMin, baseMin, maxX, baseMax, baseMax);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(icon, block, renderer);

        block.setBlockBounds(0.0F, ringMin, ringMin, minX, ringMax, ringMax);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).sideIcon, block, renderer);
        block.setBlockBounds(maxX, ringMin, ringMin, 1.0F, ringMax, ringMax);
        renderer.setRenderBoundsFromBlock(block);
        this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).sideIcon, block, renderer);

        if (block == SteamcraftBlocks.valvePipe) {
            block.setBlockBounds(4.5F * px, 1.0F - 5.5F * px, baseMax + 1 * px, 1.0F - 4.5F * px, 1.0F - 4.5F * px, baseMax + 2 * px);
            renderer.setRenderBoundsFromBlock(block);
            this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).copperIcon, block, renderer);

            block.setBlockBounds(4.5F * px, 4.5F * px, baseMax + 1 * px, 1.0F - 4.5F * px, 5.5F * px, baseMax + 2 * px);
            renderer.setRenderBoundsFromBlock(block);
            this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).copperIcon, block, renderer);

            block.setBlockBounds(4.5F * px, 5.5F * px, baseMax + 1 * px, 5.5F * px, 1.0F - 5.5F * px, baseMax + 2 * px);
            renderer.setRenderBoundsFromBlock(block);
            this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).copperIcon, block, renderer);

            block.setBlockBounds(1.0F - 5.5F * px, 5.5F * px, baseMax + 1 * px, 1.0F - 4.5F * px, 1.0F - 5.5F * px, baseMax + 2 * px);
            renderer.setRenderBoundsFromBlock(block);
            this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).copperIcon, block, renderer);

            block.setBlockBounds(5.5F * px, 7.5F * px, baseMax + 1 * px, 1.0F - 5.5F * px, 8.5F * px, baseMax + 2 * px);
            renderer.setRenderBoundsFromBlock(block);
            this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).copperIcon, block, renderer);

            block.setBlockBounds(7.5F * px, 5.5F * px, baseMax + 1 * px, 8.5F * px, 1.0F - 5.5F * px, baseMax + 2 * px);
            renderer.setRenderBoundsFromBlock(block);
            this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).copperIcon, block, renderer);

            block.setBlockBounds(6.5F * px, 6.5F * px, baseMax, 9.5F * px, 9.5F * px, baseMax + 2 * px);
            renderer.setRenderBoundsFromBlock(block);
            this.drawSides(((BlockPipe) SteamcraftBlocks.pipe).copperIcon, block, renderer);
            //	block.setBlockBounds(4.5F*px, 4.5F*px, baseMax+1*px, 1.0F-4.5F*px, 1.0F-4.5F*px, baseMax+2*px);
            //renderer.setRenderBoundsFromBlock(block);
            //this.drawSides(((BlockPipe)SteamcraftBlocks.pipe).copperIcon, block, renderer);
        }
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
        TileEntitySteamPipe pipe = (TileEntitySteamPipe) world.getTileEntity(x, y, z);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (updateWrenchStatus() || (pipe.disguiseBlock == null || pipe.disguiseBlock == Blocks.air || !pipe.disguiseBlock.renderAsNormalBlock())) {
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
            baseMin = 5.0F / 16.0F + 0.0001F;
            baseMax = 11.0F / 16.0F - 0.0001F;
            ArrayList<ForgeDirection> myDirections = new ArrayList<ForgeDirection>();

            for (ForgeDirection direction : ForgeDirection.values()) {
                if (!pipe.doesConnect(direction)) {
                }
                if (pipe.doesConnect(direction) && world.getTileEntity(pipe.xCoord + direction.offsetX, pipe.yCoord + direction.offsetY, pipe.zCoord + direction.offsetZ) != null) {
                    TileEntity tile = world.getTileEntity(pipe.xCoord + direction.offsetX, pipe.yCoord + direction.offsetY, pipe.zCoord + direction.offsetZ);
                    if (tile instanceof ISteamTransporter) {
                        ISteamTransporter target = (ISteamTransporter) tile;
                        if (target.doesConnect(direction.getOpposite())) {
                            myDirections.add(direction);
                            if (direction.offsetX == 1) {
                                maxX = 1.0F - 2 * px;
                            }
                            if (direction.offsetY == 1) {
                                maxY = 1.0F - 2 * px;
                            }
                            if (direction.offsetZ == 1) {
                                maxZ = 1.0F - 2 * px;
                            }
                            if (direction.offsetX == -1) {
                                minX = 0.0F + 2 * px;
                            }
                            if (direction.offsetY == -1) {
                                minY = 0.0F + 2 * px;
                            }
                            if (direction.offsetZ == -1) {
                                minZ = 0.0F + 2 * px;
                            }
                        }
                    }
                }
            }
            if (myDirections.size() == 1) {
                renderer.overrideBlockTexture = ((BlockPipe) SteamcraftBlocks.pipe).sideIcon;
                minX = minX - px;
                maxX = maxX + px;
                minY = minY - px;
                maxY = maxY + px;
                minZ = minZ - px;
                maxZ = maxZ + px;
            }
            if (myDirections.size() == 2) {
                ForgeDirection direction = myDirections.get(0).getOpposite();
                while (!pipe.doesConnect(direction) || direction == myDirections.get(0)) {
                    direction = ForgeDirection.getOrientation((direction.ordinal() + 1) % 5);
                }
                if (direction.offsetX == 1) {
                    maxX = 1.0F - 2 * px;
                }
                if (direction.offsetY == 1) {
                    maxY = 1.0F - 2 * px;
                }
                if (direction.offsetZ == 1) {
                    maxZ = 1.0F - 2 * px;
                }
                if (direction.offsetX == -1) {
                    minX = 0.0F + 2 * px;
                }
                if (direction.offsetY == -1) {
                    minY = 0.0F + 2 * px;
                }
                if (direction.offsetZ == -1) {
                    minZ = 0.0F + 2 * px;
                }
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
            if (pipe.disguiseBlock != null && pipe.disguiseBlock != Blocks.air && !pipe.disguiseBlock.renderAsNormalBlock() && !updateWrenchStatus()) {
                GL11.glPushMatrix();
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                renderer.setRenderBoundsFromBlock(block);
                if (pipe.disguiseMeta != 0) {
                    renderer.setOverrideBlockTexture(pipe.disguiseBlock.getIcon(0, pipe.disguiseMeta));
                }
                renderer.renderStandardBlock(pipe.disguiseBlock, x, y, z);
                renderer.clearOverrideBlockTexture();
                GL11.glPopMatrix();
            }
        } else {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.setRenderAllFaces(false);
            if (pipe.disguiseMeta != 0) {
                BlockContainer cont = null;
                for (int i = 0; i < 6; i++) {
                    renderer.setOverrideBlockTexture(pipe.disguiseBlock.getIcon(i, pipe.disguiseMeta));
                    cont = new BlockContainer(pipe.disguiseBlock, i);
                    renderer.renderStandardBlock(cont, x, y, z);
                }
                cont = null;
            } else {
                renderer.renderStandardBlock(pipe.disguiseBlock, x, y, z);
            }
            renderer.clearOverrideBlockTexture();

//		    renderer.renderFaceYPos(pipe.disguiseBlock, x, y, z, pipe.disguiseBlock.getIcon(1, pipe.disguiseMeta));
//		    renderer.renderFaceYNeg(pipe.disguiseBlock, x, y, z, pipe.disguiseBlock.getIcon(0, pipe.disguiseMeta));
//		    renderer.renderFaceXPos(pipe.disguiseBlock, x, y, z, pipe.disguiseBlock.getIcon(5, pipe.disguiseMeta));
//		    renderer.renderFaceXNeg(pipe.disguiseBlock, x, y, z, pipe.disguiseBlock.getIcon(2, pipe.disguiseMeta));
//		    renderer.renderFaceZPos(pipe.disguiseBlock, x, y, z, pipe.disguiseBlock.getIcon(3, pipe.disguiseMeta));
//		    renderer.renderFaceZNeg(pipe.disguiseBlock, x, y, z, pipe.disguiseBlock.getIcon(4, pipe.disguiseMeta));

        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return Steamcraft.tubeRenderID;
    }

}
