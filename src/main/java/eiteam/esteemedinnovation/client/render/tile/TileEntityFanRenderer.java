package eiteam.esteemedinnovation.client.render.tile;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.block.BlockFan;
import eiteam.esteemedinnovation.client.render.RenderUtility;
import eiteam.esteemedinnovation.tile.TileEntityFan;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityFanRenderer extends TileEntitySpecialRenderer<TileEntityFan> {
    static final ResourceLocation BLADES_TEXTURE = new ResourceLocation(EsteemedInnovation.MOD_ID, "textures/blocks/fan_blades_noise.png");
    static final ResourceLocation BLADES_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/fan_blades");

    @Override
    public void renderTileEntityAt(TileEntityFan tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        IBlockState state = tile.getWorldObj().getBlockState(tile.getPos());
        EnumFacing dir = state.getValue(BlockFan.FACING);
        if (dir == EnumFacing.DOWN) {
            GlStateManager.rotate(-90F, 0, 0, 1);
        }
        if (dir == EnumFacing.UP) {
            GlStateManager.rotate(90F, 0, 0, 1);
        }
        if (dir == EnumFacing.SOUTH) {
            GlStateManager.rotate(-90F, 0, 1, 0);
        }
        if (dir == EnumFacing.NORTH) {
            GlStateManager.rotate(90F, 0, 1, 0);
        }
        if (dir == EnumFacing.WEST) {
            GlStateManager.rotate(180F, 0, 1, 0);
        }
        GlStateManager.rotate(tile.rotateTicks * 25F, 1, 0, 0);
        GlStateManager.translate(-0.5, -0.5, -0.5);

        Tessellator tess = Tessellator.getInstance();
        RenderUtility.renderModel(tess.getBuffer(), BLADES_RL);
        bindTexture(BLADES_TEXTURE);
        tess.draw();

        GlStateManager.popMatrix();
    }
}
