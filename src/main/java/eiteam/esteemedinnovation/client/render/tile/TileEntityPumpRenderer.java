package eiteam.esteemedinnovation.client.render.tile;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.client.render.RenderUtility;
import eiteam.esteemedinnovation.client.render.model.ModelPump;
import eiteam.esteemedinnovation.misc.FluidHelper;
import eiteam.esteemedinnovation.tile.TileEntityPump;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.annotation.Nonnull;

public class TileEntityPumpRenderer extends TileEntitySpecialRenderer implements IInventoryTESR {
    private static final ModelPump MODEL = new ModelPump();
    private static final ResourceLocation TEXTURE = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/test.png");
    private static final float PX = (1.0F / 16.0F);

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityPump pump = (TileEntityPump) tile;
        int meta = tile.getBlockMetadata();
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F * (meta + (meta % 2 * 2)), 0F, 1F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        MODEL.renderNoRotate();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(pump.rotateTicks * 10.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        MODEL.render();
        if (pump.progress > 0) {
            FluidStack fluidStack = pump.myTank.getFluid();
            if (fluidStack != null) {
                Fluid fluid = fluidStack.getFluid();
                if (fluid != null) {
                    for (int i = 0; i < 4; i++) {
                        GL11.glPushMatrix();
                        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                        GL11.glRotatef(90.0F * i, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                        GL11.glTranslatef(0.0F, 5 * PX + 0.01F, -1 * PX);
                        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                        renderLiquid(pump.progress, fluid);
                        GL11.glPopMatrix();
                    }
                    GL11.glTranslatef(0.0F, 0.0F, 18 * PX + 0.001F);
                    renderEndcap(pump.progress, fluid);
                }
            }
        }

        GL11.glPopMatrix();
    }

    private void renderLiquid(int progress, @Nonnull Fluid fluid) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        ResourceLocation stillResource = fluid.getStill();
        TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(stillResource.toString());
        Tessellator tessellator = Tessellator.getInstance();
        float scaledProgress = Math.min(80, progress) / 80.0F;

        float f1 = (float) (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * 0.6);
        float f2 = icon.getMinV();
        float f3 = icon.getMinU();
        float f4 = icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * scaledProgress;
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.putNormal(0F, 0F, 1F);
        double y = 0.1875D + (1 - scaledProgress) * 1D;
        RenderUtility.addVertexWithUV(buffer, 0.3125D, y, 0D, f1, f4);
        RenderUtility.addVertexWithUV(buffer, 0.6875D, 6, 0D, f3, f4);
        RenderUtility.addVertexWithUV(buffer, 0.6875D, 1.1875D, 0D, f3, f2);
        RenderUtility.addVertexWithUV(buffer, 0.3125D, 1.1875D, 0D, f1, f2);
        tessellator.draw();

        if (progress > 80) {
            float scaledProgress2 = (progress - 80) / 20.0F;
            f1 = (float) (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * 0.6);
            f2 = icon.getMinV();
            f3 = icon.getMinU();
            f4 = (float) (icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * scaledProgress2 * 0.1875);
            buffer.putNormal(0F, 0F, 1F);
            y = (1 - scaledProgress2) * 0.1875D;
            RenderUtility.addVertexWithUV(buffer, 0.3125D, y, 0.0D, f1, f4);
            RenderUtility.addVertexWithUV(buffer, 0.6875D, y, 0.0D, f3, f4);
            RenderUtility.addVertexWithUV(buffer, 0.6875D, 0.1875D, 0.0D, f3, f2);
            RenderUtility.addVertexWithUV(buffer, 0.3125D, 0.1875D, 0.0D, f1, f2);
            tessellator.draw();
        }
    }

    private void renderEndcap(int progress, @Nonnull Fluid fluid) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite icon = FluidHelper.getStillTexture(Minecraft.getMinecraft(), fluid);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        float f1 = (float) (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * 0.6);
        float f2 = icon.getMinV();
        float f3 = icon.getMinU();
        float f4 = (float) (icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * 0.6);
        buffer.putNormal(0F, 0F, 1F);
        RenderUtility.addVertexWithUV(buffer, 0.3125D, 0.3125D, 0.0D, f1, f4);
        RenderUtility.addVertexWithUV(buffer, 0.6875D, 0.3125D, 0.0D, f3, f4);
        RenderUtility.addVertexWithUV(buffer, 0.6875D, 0.6875D, 0.0D, f3, f2);
        RenderUtility.addVertexWithUV(buffer, 0.3125D, 0.6875D, 0.0D, f1, f2);
        tessellator.draw();

        GL11.glTranslatef(0.0F, 0.0F, -20 * PX * (progress / 100.0F) - 0.002F);
        buffer.putNormal(0F, 0F, 1F);
        RenderUtility.addVertexWithUV(buffer, 0.3125D, 0.6875D, 0.0D, f1, f4);
        RenderUtility.addVertexWithUV(buffer, 0.6875D, 0.6875D, 0.0D, f3, f4);
        RenderUtility.addVertexWithUV(buffer, 0.6875D, 0.3125D, 0.0D, f3, f2);
        RenderUtility.addVertexWithUV(buffer, 0.3125D, 0.3125D, 0.0D, f1, f2);
        tessellator.draw();
    }

    @Override
    public void renderInventoryTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslated(x, y, z);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(0F, 0F, 1F, 0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        MODEL.renderNoRotate();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(Minecraft.getMinecraft().thePlayer.ticksExisted * 10.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        MODEL.render();
        GL11.glPopMatrix();
    }
}
