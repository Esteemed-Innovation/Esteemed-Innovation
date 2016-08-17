package flaxbeard.steamcraft.client.render.tile;

import flaxbeard.steamcraft.block.BlockSteamCharger;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class TileEntitySteamChargerRenderer extends TileEntitySpecialRenderer<TileEntitySteamCharger> {
    @Override
    public void renderTileEntityAt(TileEntitySteamCharger charger, double x, double y, double z, float partialTicks, int destroyStage) {
        ItemStack stackInSlotZero = charger.getStackInSlot(0);
        if (stackInSlotZero == null) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        EnumFacing facing = charger.getWorld().getBlockState(charger.getPos()).getValue(BlockSteamCharger.FACING);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5 + (1F / 16F) - 0.02F, z + 0.5);
        if (facing == EnumFacing.NORTH) {
            GlStateManager.rotate(180F, 0, 1, 0);
        } else if (facing == EnumFacing.EAST) {
            GlStateManager.rotate(270F, 0, 1, 0);
        } else if (facing == EnumFacing.WEST) {
            GlStateManager.rotate(90F, 0, 1, 0);
        }
        GlStateManager.rotate(90F, 1, 0, 0);
        mc.getRenderItem().renderItem(stackInSlotZero, ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
    }

}
