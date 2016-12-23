package eiteam.esteemedinnovation.converter;

import eiteam.esteemedinnovation.commons.util.RenderUtility;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityFluidSteamRenderer extends TileEntitySpecialRenderer<TileEntityFluidSteamConverter> {
    private static final ResourceLocation SQUISH_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/converter_squisher");
    private static final ResourceLocation RING_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/converter_squisher_ring");

    @Override
    public void renderTileEntityAt(TileEntityFluidSteamConverter converter, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        EnumFacing dir = converter.getWorld().getBlockState(converter.getPos()).getValue(BlockFluidSteamConverter.FACING);
        switch (dir) {
            case NORTH: {
                break;
            }
            case SOUTH: {
                GlStateManager.rotate(180F, 0, 1, 0);
                break;
            }
            case WEST: {
                GlStateManager.rotate(90F, 0, 1, 0);
                break;
            }
            case EAST: {
                GlStateManager.rotate(270F, 0, 1, 0);
                break;
            }
            case UP: {
                GlStateManager.rotate(90F, 1, 0, 0);
                break;
            }
            case DOWN: {
                GlStateManager.rotate(270F, 1, 0, 0);
                break;
            }
        }
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        if (converter.pushing) {
            double val = (0.2F + (StrictMath.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100 - converter.runTicks)))) * 0.07F) / 0.2F;
            GlStateManager.scale(1D, 1.0D, val);
        }

        RenderUtility.renderModel(Tessellator.getInstance().getBuffer(), SQUISH_RL);
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (converter.pushing) {
            double val = (StrictMath.cos(Math.toRadians(-90.0F + (360.0F / 100.0F) * (100 - converter.runTicks)))) * 0.2F;
            GlStateManager.translate(0, 0, val);
        }
        RenderUtility.renderModel(Tessellator.getInstance().getBuffer(), RING_RL);
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
}
