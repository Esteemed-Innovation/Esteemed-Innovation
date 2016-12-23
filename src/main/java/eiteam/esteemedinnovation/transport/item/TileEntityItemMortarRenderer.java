package eiteam.esteemedinnovation.transport.item;

import eiteam.esteemedinnovation.commons.util.RenderUtility;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TileEntityItemMortarRenderer extends TileEntitySpecialRenderer<TileEntityItemMortar> {
    private static final ResourceLocation CANNON1_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/item_mortar_cannon1");
    private static final ResourceLocation CANNON2_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/item_mortar_cannon2");
    private static final ResourceLocation ELSE_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/item_mortar_else");

    @Override
    public void renderTileEntityAt(TileEntityItemMortar mortar, double x, double y, double z, float partialTicks, int destroyStage) {
        BlockPos pos = mortar.getPos();
        int zTile = pos.getZ();
        int xTile = pos.getX();
        int zTarget = mortar.zTarget;
        int xTarget = mortar.xTarget;
        int fireTicks = mortar.fireTicks;

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(x, y, z);

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.translate(0.5, 0.5, 0.5);
        if (zTarget == zTile) {
            GlStateManager.rotate(270F, 0, 1, 0);
        } else {
            GlStateManager.rotate((float) Math.toDegrees(StrictMath.atan2(xTarget - xTile, zTarget - zTile)), 0, 1, 0);
        }
        GlStateManager.translate(-0.5, -0.5, -0.5);
        RenderUtility.renderModel(Tessellator.getInstance().getBuffer(), ELSE_RL);
        Tessellator.getInstance().draw();
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.rotate(2F, 1, 0, 0);
        GlStateManager.translate(-0.5, -0.5, -0.5);
        if (fireTicks > 60 && fireTicks <= 65) {
            GlStateManager.translate(0, -0.05F * StrictMath.sin(Math.PI * ((fireTicks - 60) / 10.0F)), 0);
        } else if (fireTicks > 65 && fireTicks < 80) {
            GlStateManager.translate(0, -0.05F * (1 - StrictMath.sin(Math.PI * ((fireTicks - 65) / 30.0F))), 0);
        }
        RenderUtility.renderModel(Tessellator.getInstance().getBuffer(), CANNON1_RL);
        Tessellator.getInstance().draw();
        GlStateManager.translate(0, 1, 0);
        if (fireTicks > 60 && fireTicks <= 65) {
            GlStateManager.translate(0, -0.15F * StrictMath.sin(Math.PI * ((fireTicks - 60) / 10.0F)), 0);
        } else if (fireTicks > 65 && fireTicks < 80) {
            GlStateManager.translate(0, -0.15F * (1 - StrictMath.sin(Math.PI * ((fireTicks - 65) / 30.0F))), 0);
        }
        RenderUtility.renderModel(Tessellator.getInstance().getBuffer(), CANNON2_RL);
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
        GlStateManager.rotate(0, 180, 0, 0);
    }
}
