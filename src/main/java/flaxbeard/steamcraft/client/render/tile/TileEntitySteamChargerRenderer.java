package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.common.tile.TileEntitySteamCharger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TileEntitySteamChargerRenderer extends TileEntitySpecialRenderer {


    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4,
                                   double var6, float var8) {
        TileEntitySteamCharger charger = (TileEntitySteamCharger) var1;
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        int meta = charger.getWorldObj().getBlockMetadata(charger.xCoord, charger.yCoord, charger.zCoord);
        if (charger.getStackInSlot(0) != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) var2 + 0.5F, (float) var4 + 0.5F + (1.0F / 16.0F) - 0.02F, (float) var6 + 0.5F);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            GL11.glRotatef(0.0F + 90.0F * meta + (meta % 2 == 0 ? 180.0F : 0.0F), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -(7.0F / 32.0F), 0.0F);
            ItemStack is = charger.getStackInSlot(0).copy();
            is.stackSize = 1;
            EntityItem item = new EntityItem(var1.getWorldObj(), 0.0F, 0.0F, 0.0F, is);
            item.hoverStart = 0.0F;
            boolean fancy = settings.fancyGraphics;
            settings.fancyGraphics = true;
            RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            settings.fancyGraphics = fancy;
            GL11.glPopMatrix();
        }
    }

}
