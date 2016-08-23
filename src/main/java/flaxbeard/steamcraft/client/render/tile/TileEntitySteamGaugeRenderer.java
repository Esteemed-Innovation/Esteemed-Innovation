package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.block.BlockSteamGauge;
import flaxbeard.steamcraft.client.render.RenderUtility;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
import flaxbeard.steamcraft.tile.TileEntitySteamGauge;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySteamGaugeRenderer extends TileEntitySpecialRenderer<TileEntitySteamGauge> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("steamcraft:textures/blocks/gaugePointer.png");
    private static final ResourceLocation POINTER_RL = new ResourceLocation(Steamcraft.MOD_ID, "block/steam_gauge_pointer");

    @Override
    public void renderTileEntityAt(TileEntitySteamGauge gauge, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y, z + 0.5);
        World world = gauge.getWorld();
        BlockPos gaugePos = gauge.getPos();
        EnumFacing dir = world.getBlockState(gaugePos).getValue(BlockSteamGauge.FACING).getOpposite();
        int offsetX = dir.getFrontOffsetX();
        int offsetZ = dir.getFrontOffsetZ();
        TileEntity tile = world.getTileEntity(gaugePos.offset(dir));
        if (tile != null) {
            ISteamTransporter trans = (ISteamTransporter) tile;
            float pressure = trans instanceof TileEntitySteamCharger ? ((TileEntitySteamCharger) trans).getSteamInItem() : trans.getPressure();
            if (trans instanceof TileEntitySteamPipe) {
                GlStateManager.translate(4F * offsetX / 16.0F, 0.0F, 4F * offsetZ / 16.0F);
            }
            switch (dir.getOpposite()) {
                case WEST: {
                    GlStateManager.rotate(-90F, 0, 1, 0);
                    break;
                }
                case EAST: {
                    GlStateManager.rotate(90F, 0, 1, 0);
                    break;
                }
                case NORTH: {
                    GlStateManager.rotate(180F, 0, 1, 0);
                    break;
                }
			default:
				break;
            }
            GlStateManager.rotate(-95.0F, 1, 0, 0);
            GlStateManager.translate(-7.5F / 16F, 0, -10F / 16F);

            float rand = 0.0F;
            if (pressure > 0.0F) {
                rand = (float) ((Math.random() - 0.5F));
                if (pressure >= 1.0F) {
                    rand = (float) ((Math.random() * 20.0F - 10.0F));
                }
            }
            GlStateManager.translate(7F / 16F, 0, 17F / 16F);
            GlStateManager.rotate((Math.min(190.0F * pressure, 190.0F) + rand), 0, 1, 0);
            GlStateManager.translate(-7F / 16F, 0, -17F / 16F);

            Tessellator tess = Tessellator.getInstance();
            VertexBuffer buffer = tess.getBuffer();
            RenderUtility.renderModel(buffer, POINTER_RL);
            bindTexture(TEXTURE);
            tess.draw();
        }
        GlStateManager.popMatrix();
    }
}
