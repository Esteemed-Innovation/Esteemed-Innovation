package flaxbeard.steamcraft.client.render;

import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.api.enhancement.IEnhancementRocketLauncher;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.item.firearm.ItemFirearm;
import flaxbeard.steamcraft.item.firearm.ItemRocketLauncher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

public class ItemFirearmRenderer implements IItemRenderer {

    private static RenderItem renderItem = new RenderItem();

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        IIcon icon = itemStack.getIconIndex();
        renderItem.renderIcon(0, 0, icon, 16, 16);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = itemStack.getTagCompound();
            nbt.setInteger("loaded", 0);
            nbt.setInteger("numloaded", 0);
        }
        NBTTagCompound nbt = itemStack.getTagCompound();
        int use = SteamcraftEventHandler.use;
        int loaded = nbt.getInteger("loaded");

        double health = (double) (use) / (double) itemStack.getMaxItemUseDuration();
        if (Minecraft.getMinecraft().thePlayer.getHeldItem() == itemStack && use > -1 && loaded == 0 && itemStack.getMaxItemUseDuration() != 72000) {
            GL11.glPushMatrix();
            int j1 = (int) Math.round(13.0D - health * 13.0D);
            int k = (int) Math.round(255.0D - health * 255.0D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            Tessellator tessellator = Tessellator.instance;
            int l = 255 - k << 16 | k << 8;
            int i1 = (255 - k) / 4 << 16 | 16128;
            this.renderQuad(tessellator, 2, 10, 13, 2, 0);
            this.renderQuad(tessellator, 2, 10, 12, 1, i1);
            this.renderQuad(tessellator, 2, 10, j1, 1, l);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
        int maxAmmo = 1;
        int currentAmmo = 1;
        if (itemStack.getItem() instanceof ItemFirearm) {
            int enhancementShells = 0;
            if (UtilEnhancements.hasEnhancement(itemStack)) {
                if (UtilEnhancements.getEnhancementFromItem(itemStack) instanceof IEnhancementFirearm) {
                    enhancementShells = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(itemStack)).getClipSizeChange(
                      itemStack.getItem());
                }
            }
            maxAmmo = ((ItemFirearm) itemStack.getItem()).shellCount + enhancementShells;
            currentAmmo = itemStack.stackTagCompound.getInteger("loaded");
        }
        if (itemStack.getItem() instanceof ItemRocketLauncher) {
            int enhancementShells = 0;
            if (UtilEnhancements.hasEnhancement(itemStack)) {
                if (UtilEnhancements.getEnhancementFromItem(itemStack) instanceof IEnhancementRocketLauncher) {
                    enhancementShells = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(itemStack)).getClipSizeChange(
                      itemStack.getItem());
                }
            }
            maxAmmo = ((ItemRocketLauncher) itemStack.getItem()).shellCount + enhancementShells;
            currentAmmo = itemStack.stackTagCompound.getInteger("loaded");
        }
        health = (double) (maxAmmo - currentAmmo) / (double) maxAmmo;
        if (Minecraft.getMinecraft().thePlayer.getHeldItem() == itemStack && currentAmmo != 0) {
            GL11.glPushMatrix();
            int j1 = (int) Math.round(13.0D - health * 13.0D);
            int k = (int) Math.round(255.0D - health * 255.0D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            Tessellator tessellator = Tessellator.instance;
            int l = 255 - k << 16 | k << 8;
            int i1 = (255 - k) / 4 << 16 | 16128;
            this.renderQuad(tessellator, 2, 1, 13, 2, 0);
            this.renderQuad(tessellator, 2, 1, 12, 1, i1);
            this.renderQuad(tessellator, 2, 1, j1, 1, l);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setColorOpaque_I(par6);
        par1Tessellator.addVertex((double) (par2 + 0), (double) (par3 + 0), 0.0D);
        par1Tessellator.addVertex((double) (par2 + 0), (double) (par3 + par5), 0.0D);
        par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + par5), 0.0D);
        par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + 0), 0.0D);
        par1Tessellator.draw();
    }

}
