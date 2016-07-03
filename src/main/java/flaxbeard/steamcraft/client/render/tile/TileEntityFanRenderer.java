package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.block.BlockFan;
import flaxbeard.steamcraft.client.render.model.ModelFan;
import flaxbeard.steamcraft.tile.TileEntityFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityFanRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelFan MODEL = new ModelFan();
    private static final ResourceLocation TEXTURE = new ResourceLocation("steamcraft:textures/models/fan.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityFan fan = (TileEntityFan) tile;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        EnumFacing dir = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockFan.FACING);
        if (dir == EnumFacing.DOWN) {
            GL11.glRotatef(-90.0F, 0F, 0F, 1F);
        }
        if (dir == EnumFacing.UP) {
            GL11.glRotatef(90.0F, 0F, 0F, 1F);
        }
        if (dir == EnumFacing.SOUTH) {
            GL11.glRotatef(-90.0F, 0F, 1F, 0F);
        }
        if (dir == EnumFacing.NORTH) {
            GL11.glRotatef(90.0F, 0F, 1F, 0F);
        }
        if (dir == EnumFacing.WEST) {
            GL11.glRotatef(180.0F, 0F, 1F, 0F);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        MODEL.renderBase();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(fan.rotateTicks * 25.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        MODEL.render();
        renderBlades();

        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity tile, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(-0.2F, -0.5F, -0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        MODEL.renderBase();
        MODEL.render();
        renderBlades();
        GL11.glPopMatrix();
    }

    private void renderBlades() {
        for (int i = 0; i < 4; i++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);

            GL11.glRotatef(90F * i, 1F, 0F, 0F);
            GL11.glRotatef(10F, 0F, 1F, 0F);

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            MODEL.renderBlade();

            GL11.glPopMatrix();
        }
    }
}
