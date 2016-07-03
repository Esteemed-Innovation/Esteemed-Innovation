package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TileEntitySteamChargerRenderer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntitySteamCharger charger = (TileEntitySteamCharger) tile;
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings settings = mc.gameSettings;
        int meta = charger.getBlockMetadata();
        ItemStack stackInSlotZero = charger.getStackInSlot(0);
        if (stackInSlotZero != null) {
            GL11.glDepthMask(false);
            RenderItem ri = mc.getRenderItem();
            IBakedModel model = ri.getItemModelWithOverrides(stackInSlotZero, tile.getWorld(), null);
            ri.renderItem(stackInSlotZero, model);
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F + (1.0F / 16.0F) - 0.02F, (float) z + 0.5F);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            GL11.glRotatef(0.0F + 90.0F * meta + (meta % 2 == 0 ? 180.0F : 0.0F), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -(7.0F / 32.0F), 0.0F);
            ItemStack is = stackInSlotZero.copy();
            is.stackSize = 1;
            EntityItem item = new EntityItem(tile.getWorld(), 0.0F, 0.0F, 0.0F, is);
            item.hoverStart = 0.0F;
            boolean fancy = settings.fancyGraphics;
            settings.fancyGraphics = true;
            mc.getRenderManager().doRenderEntity(item, 0D, 0D, 0D, 0F, 0F, false);
            settings.fancyGraphics = fancy;
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }
    }

}
