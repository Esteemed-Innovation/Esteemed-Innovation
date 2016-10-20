package eiteam.esteemedinnovation.client.render.tile;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.block.BlockChargingPad;
import eiteam.esteemedinnovation.client.render.RenderUtility;
import eiteam.esteemedinnovation.tile.TileEntityChargingPad;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityChargingPadRenderer extends TileEntitySpecialRenderer<TileEntityChargingPad> {
    private static final ResourceLocation POLES_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/charging_pad_poles");

    @Override
    public void renderTileEntityAt(TileEntityChargingPad pad, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        EnumFacing facing = pad.getWorld().getBlockState(pad.getPos()).getValue(BlockChargingPad.FACING);
        int rotation = 0;
        switch (facing) {
            case NORTH: {
                rotation = 90;
                break;
            }
            case SOUTH: {
                rotation = 270;
                break;
            }
            case WEST: {
                rotation = 180;
                break;
            }
            case EAST: {
                rotation = 0;
                break;
            }
            default: {
                break;
            }
        }

        GlStateManager.rotate(90.0F, 0F, 1F, 0F);

        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        GlStateManager.translate(0.0F, StrictMath.sin(Math.toRadians((90D / 40D) * pad.extendTicks)), 0.0F);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        RenderUtility.renderModel(Tessellator.getInstance().getBuffer(), POLES_RL);
        Tessellator.getInstance().draw();

        GlStateManager.popMatrix();
    }
}
