package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.client.render.model.ModelPointer;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class TileEntitySteamGaugeRenderer extends TileEntitySpecialRenderer {
    private static final ModelPointer model = new ModelPointer();
    private static final ResourceLocation texture = new ResourceLocation("steamcraft:textures/models/pointer.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        int meta = tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        ForgeDirection dir = ForgeDirection.getOrientation(meta);
        GL11.glTranslatef(-7F * dir.offsetX / 16.0F, -2.0F / 16.0F, -7F * dir.offsetZ / 16.0F);
        if (tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - dir.offsetX, tileEntity.yCoord - dir.offsetY, tileEntity.zCoord - dir.offsetZ) != null) {
            ISteamTransporter trans = (ISteamTransporter) tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord - dir.offsetX, tileEntity.yCoord - dir.offsetY, tileEntity.zCoord - dir.offsetZ);
            float pressure = 0F;
            if (trans instanceof TileEntitySteamCharger) {
                pressure = ((TileEntitySteamCharger) trans).getSteamInItem();
            } else {
                pressure = trans.getPressure();
            }
            if (trans instanceof TileEntitySteamPipe) {
                GL11.glTranslatef(-5.0F * dir.offsetX / 16.0F, 0.0F, -5.0F * dir.offsetZ / 16.0F);
            }
            if (meta == 2) {
                GL11.glRotated(-90.0F, 0, 1, 0);
            }
            if (meta == 3) {
                GL11.glRotated(90.0F, 0, 1, 0);
            }
            if (meta == 5) {
                GL11.glRotated(180.0F, 0, 1, 0);
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

            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            model.render();
            GL11.glPopMatrix();
        }

    }

}
