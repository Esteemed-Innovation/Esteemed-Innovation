package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.client.render.model.ModelPump;
import flaxbeard.steamcraft.tile.TileEntityPump;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityPumpRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelPump model = new ModelPump();
    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/test.png");
    private static float px = (1.0F / 16.0F);

    @Override
    public void renderTileEntityAt(TileEntity var1, double x, double y, double z, float var8) {
        TileEntityPump pump = (TileEntityPump) var1;
        int meta = pump.getWorldObj().getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);


        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F * (meta + (meta % 2 * 2)), 0F, 1F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        model.renderNoRotate();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);


        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(pump.rotateTicks * 10.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);


        model.render();
        if (pump.progress > 0) {
            for (int i = 0; i < 4; i++) {
                GL11.glPushMatrix();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                GL11.glRotatef(90.0F * i, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                GL11.glTranslatef(0.0F, 5 * px + 0.01F, -1 * px);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                renderLiquid(pump.progress, pump.myTank.getFluid().getFluid());
                GL11.glPopMatrix();
            }
            GL11.glTranslatef(0.0F, 0.0F, 18 * px + 0.001F);
            renderEndcap(pump.progress, pump.myTank.getFluid().getFluid());
        }


        GL11.glPopMatrix();
    }

    private void renderLiquid(int progress, Fluid fluid) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        IIcon icon = fluid.getStillIcon();
        Tessellator tessellator = Tessellator.instance;
        float scaledProgress = Math.min(80, progress) / 80.0F;

        float f1 = (float) (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * 0.6);
        float f2 = icon.getMinV();
        float f3 = icon.getMinU();
        float f4 = icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * scaledProgress;
        float pix2 = 2.0F / 16.0F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0.3125D, 0.1875D + (1 - scaledProgress) * 1.0D, 0.0D, f1, f4);
        tessellator.addVertexWithUV(0.6875D, 0.1875D + (1 - scaledProgress) * 1.0D, 0.0D, f3, f4);
        tessellator.addVertexWithUV(0.6875D, 1.1875D, 0.0D, f3, f2);
        tessellator.addVertexWithUV(0.3125D, 1.1875D, 0.0D, f1, f2);
        tessellator.draw();


        if (progress > 80) {
            float scaledProgress2 = (progress - 80) / 20.0F;
            f1 = (float) (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * 0.6);
            f2 = icon.getMinV();
            f3 = icon.getMinU();
            f4 = (float) (icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * scaledProgress2 * 0.1875);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addVertexWithUV(0.3125D, 0.0D + (1 - scaledProgress2) * 0.1875D, 0.0D, f1, f4);
            tessellator.addVertexWithUV(0.6875D, 0.0D + (1 - scaledProgress2) * 0.1875D, 0.0D, f3, f4);
            tessellator.addVertexWithUV(0.6875D, 0.1875D, 0.0D, f3, f2);
            tessellator.addVertexWithUV(0.3125D, 0.1875D, 0.0D, f1, f2);
            tessellator.draw();
        }
    }

    private void renderEndcap(int progress, Fluid fluid) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        IIcon icon = fluid.getStillIcon();
        Tessellator tessellator = Tessellator.instance;

        float f1 = (float) (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * 0.6);
        float f2 = icon.getMinV();
        float f3 = icon.getMinU();
        float f4 = (float) (icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * 0.6);
        float pix2 = 2.0F / 16.0F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0.3125D, 0.3125D, 0.0D, f1, f4);
        tessellator.addVertexWithUV(0.6875D, 0.3125D, 0.0D, f3, f4);
        tessellator.addVertexWithUV(0.6875D, 0.6875D, 0.0D, f3, f2);
        tessellator.addVertexWithUV(0.3125D, 0.6875D, 0.0D, f1, f2);
        tessellator.draw();

        GL11.glTranslatef(0.0F, 0.0F, -20 * px * (progress / 100.0F) - 0.002F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0.3125D, 0.6875D, 0.0D, f1, f4);
        tessellator.addVertexWithUV(0.6875D, 0.6875D, 0.0D, f3, f4);
        tessellator.addVertexWithUV(0.6875D, 0.3125D, 0.0D, f3, f2);
        tessellator.addVertexWithUV(0.3125D, 0.3125D, 0.0D, f1, f2);
        tessellator.draw();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity var1, double x,
                                            double y, double z, float var8) {
        TileEntityPump pump = (TileEntityPump) var1;
        int meta = 0;
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);


        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F * (meta + (meta % 2 * 2)), 0F, 1F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        model.renderNoRotate();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);


        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(Minecraft.getMinecraft().thePlayer.ticksExisted * 10.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);


        model.render();
        GL11.glPopMatrix();

    }

}
