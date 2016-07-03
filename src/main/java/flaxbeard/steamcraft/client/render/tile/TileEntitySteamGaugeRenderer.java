package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.block.BlockSteamGauge;
import flaxbeard.steamcraft.client.render.model.ModelPointer;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class TileEntitySteamGaugeRenderer extends TileEntitySpecialRenderer {
    private static final ModelPointer MODEL = new ModelPointer();
    private static final ResourceLocation TEXTURE = new ResourceLocation("steamcraft:textures/models/pointer.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        World world = tileEntity.getWorld();
        BlockPos pos = tileEntity.getPos();
        EnumFacing dir = world.getBlockState(pos).getValue(BlockSteamGauge.FACING);
        GL11.glTranslatef(-7F * dir.getFrontOffsetX() / 16.0F, -2.0F / 16.0F, -7F * dir.getFrontOffsetZ() / 16.0F);

        BlockPos offsetPos = new BlockPos(pos.getX() - dir.getFrontOffsetX(), pos.getY() - dir.getFrontOffsetY(),
          pos.getZ() - dir.getFrontOffsetZ());
        TileEntity offsetTileEntity = world.getTileEntity(offsetPos);
        if (offsetTileEntity != null) {
            ISteamTransporter trans = (ISteamTransporter) offsetTileEntity;
            float pressure = trans instanceof TileEntitySteamCharger ? ((TileEntitySteamCharger) trans).getSteamInItem() : trans.getPressure();
            if (trans instanceof TileEntitySteamPipe) {
                GL11.glTranslatef(-5.0F * dir.getFrontOffsetX() / 16.0F, 0.0F, -5.0F * dir.getFrontOffsetZ() / 16.0F);
            }
            switch (dir) {
                case NORTH: {
                    GL11.glRotated(-90F, 0, 1, 0);
                    break;
                }
                case SOUTH: {
                    GL11.glRotated(90F, 0, 1, 0);
                    break;
                }
                case EAST: {
                    GL11.glRotated(180F, 0, 1, 0);
                    break;
                }
            }
            GL11.glRotatef(-95.0F, 1, 0, 0);
            float rand = 0.0F;
            ////Steamcraft.log.debug(trans.getPressure());
            if (pressure > 0.0F) {
                rand = (float) ((Math.random() - 0.5F) * 5.0F);
                if (pressure >= 1.0F) {
                    rand = (float) ((Math.random() * 50.0F - 25.0F));
                }
            }
            GL11.glRotated((Math.min(190.0F * pressure, 190.0F) + rand), 1, 0, 0);

            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
            MODEL.render();
            GL11.glPopMatrix();
        }
    }
}
