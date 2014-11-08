package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.client.render.model.ModelFan;
import flaxbeard.steamcraft.tile.TileEntityFan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityFanRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelFan model = new ModelFan();

    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/fan.png");

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        TileEntityFan fan = (TileEntityFan) var1;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2 + 0.5F, (float) var4 + 0.5F, (float) var6 + 0.5F);
        int meta = var1.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
        if (meta == 0) {
            GL11.glRotatef(-90.0F, 0F, 0F, 1F);
        }
        if (meta == 1) {
            GL11.glRotatef(90.0F, 0F, 0F, 1F);
        }
        if (meta == 3) {
            GL11.glRotatef(-90.0F, 0F, 1F, 0F);
        }
        if (meta == 2) {
            GL11.glRotatef(90.0F, 0F, 1F, 0F);
        }
        if (meta == 4) {
            GL11.glRotatef(180.0F, 0F, 1F, 0F);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        model.renderBase();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(fan.rotateTicks * 25.0F, 1F, 0F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        model.render();
        for (int i = 0; i < 4; i++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);

            GL11.glRotatef(90.0F * i, 1F, 0F, 0F);
            GL11.glRotatef(10.0F, 0F, 1F, 0F);

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            model.renderBlade();

            GL11.glPopMatrix();

        }

        GL11.glPopMatrix();

    }


    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double x, double y, double z, float var8) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(-0.2F, -0.5F, -0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        model.renderBase();
        model.render();
        for (int i = 0; i < 4; i++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);

            GL11.glRotatef(90.0F * i, 1F, 0F, 0F);
            GL11.glRotatef(10.0F, 0F, 1F, 0F);

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            model.renderBlade();

            GL11.glPopMatrix();

        }
        GL11.glPopMatrix();

    }

}
