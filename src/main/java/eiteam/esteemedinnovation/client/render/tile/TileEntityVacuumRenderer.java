package eiteam.esteemedinnovation.client.render.tile;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.block.BlockVacuum;
import eiteam.esteemedinnovation.client.render.RenderUtility;
import eiteam.esteemedinnovation.tile.TileEntityVacuum;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import static eiteam.esteemedinnovation.client.render.tile.TileEntityFanRenderer.BLADES_RL;
import static eiteam.esteemedinnovation.client.render.tile.TileEntityFanRenderer.BLADES_TEXTURE;

public class TileEntityVacuumRenderer extends TileEntitySpecialRenderer<TileEntityVacuum> {
    private static final ResourceLocation SIDE_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/vacuum_front_side");

    @Override
    public void renderTileEntityAt(TileEntityVacuum vacuum, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        EnumFacing dir = vacuum.getWorld().getBlockState(vacuum.getPos()).getValue(BlockVacuum.FACING);
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
        GlStateManager.rotate(-vacuum.rotateTicks * 25F, 1, 0, 0);
        GlStateManager.translate(-0.5, -0.5, -0.5);

        Tessellator tess = Tessellator.getInstance();

        RenderUtility.renderModel(tess.getBuffer(), BLADES_RL);
        bindTexture(BLADES_TEXTURE);
        tess.draw();
        GlStateManager.popMatrix();

        // These must be done in different matrixes in order to prevent wrongly spinning the sides with the blades.

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        // The default orientation of the blades and the sides are completely different, so we have to rotate differently.
        if (dir == EnumFacing.UP) {
            GlStateManager.rotate(270F, 1, 0, 0);
        } else if (dir == EnumFacing.DOWN) {
            GlStateManager.rotate(90F, 1, 0, 0);
        } else if (dir == EnumFacing.NORTH) {
            GlStateManager.rotate(180F, 0, 1, 0);
        } else if (dir == EnumFacing.EAST) {
            GlStateManager.rotate(90F, 0, 1, 0);
        } else if (dir == EnumFacing.WEST) {
            GlStateManager.rotate(270F, 0, 1, 0);
        }
        GlStateManager.translate(-0.5, -0.5, -0.5);

        renderFour(tess, SIDE_RL);

        GlStateManager.popMatrix();
    }

    private void renderFour(Tessellator tessellator, ResourceLocation modelResource) {
        for (int i = 0; i < 4; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5, 0.5, 0.5);
            GlStateManager.rotate(90F * i, 0, 0, 1);
            GlStateManager.rotate(-10F, 0, 1, 0);
            GlStateManager.translate(-0.5, -0.5, -0.5);

            RenderUtility.renderModel(tessellator.getBuffer(), modelResource);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            tessellator.draw();

            GlStateManager.popMatrix();
        }
    }
}
