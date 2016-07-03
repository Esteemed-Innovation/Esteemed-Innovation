package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.block.BlockFluidSteamConverter;
import flaxbeard.steamcraft.client.render.model.ModelFluidSteam;
import flaxbeard.steamcraft.tile.TileEntityFluidSteamConverter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityFluidSteamRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelFluidSteam MODEL = new ModelFluidSteam();
    private static final ResourceLocation TEXTURE = new ResourceLocation("steamcraft:textures/models/fluidSteam.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityFluidSteamConverter converter = (TileEntityFluidSteamConverter) tile;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        EnumFacing dir = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockFluidSteamConverter.FACING);
        if (dir == EnumFacing.DOWN) {
            GL11.glRotatef(-90F, 0F, 0F, 1F);
        }
        if (dir == EnumFacing.UP) {
            GL11.glRotatef(90F, 0F, 0F, 1F);
        }
        if (dir == EnumFacing.NORTH) {
            GL11.glRotatef(90F, 0F, 1F, 0F);
        }
        if (dir == EnumFacing.SOUTH) {
            GL11.glRotatef(-90F, 0F, 1F, 0F);
        }
        if (dir == EnumFacing.WEST) {
            GL11.glRotatef(180F, 0F, 1F, 0F);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        MODEL.renderAnchored();
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glTranslatef(0.25F, 0.0F, 0.0F);
        if (converter.pushing) {
            GL11.glScaled(0.4f, 1.0D, 1.0D);
        } else {
            GL11.glScaled((0.2F + (Math.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100 - converter.runTicks)))) * 0.13F) / 0.2F, 1.0D, 1.0D);
        }
        //	GL11.glTranslatef(-1.00F, 0.0F, 0.0F);
        //GL11.glTranslatef(0.5F, 0.0F, 0.0F);

        MODEL.renderSquish();
        GL11.glPopMatrix();
        if(converter.pushing)
            GL11.glTranslated(-0.2F, 0.0F, 0.0F);
        else
            GL11.glTranslated(0.2F - (Math.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100 - converter.runTicks)))) * 0.2F, 0.0F, 0.0F);
        MODEL.renderMoving();
        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity tile, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GL11.glRotatef(180F, 0F, 1F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        MODEL.renderAnchored();
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glTranslatef(0.25F, 0.0F, 0.0F);
        GL11.glScaled((0.2F + (Math.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100)))) * 0.13F) / 0.2F, 1.0D, 1.0D);
        //	GL11.glTranslatef(-1.00F, 0.0F, 0.0F);
        //GL11.glTranslatef(0.5F, 0.0F, 0.0F);

        MODEL.renderSquish();
        GL11.glPopMatrix();
        GL11.glTranslated(0.2F - (Math.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100)))) * 0.2F, 0.0F, 0.0F);
        MODEL.renderMoving();
        GL11.glPopMatrix();
    }
}
