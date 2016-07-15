package flaxbeard.steamcraft.client.render.item;

import flaxbeard.steamcraft.api.IRenderItem;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.api.enhancement.IEnhancementRocketLauncher;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.client.render.RenderUtility;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.item.firearm.ItemFirearm;
import flaxbeard.steamcraft.item.firearm.ItemRocketLauncher;
import codechicken.lib.render.IItemRenderer;
import codechicken.lib.render.TextureUtils;
import codechicken.lib.render.TransformUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

public class ItemFirearmRenderer implements IItemRenderer {
    @Override
    public void renderItem(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (!(item instanceof IRenderItem)) {
            return;
        }
        ResourceLocation icon = ((IRenderItem) item).getIcon(itemStack);
        if (icon != null) {
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            TextureUtils.changeTexture(icon);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        }

        ItemFirearm.initializeNBT(itemStack);
        NBTTagCompound nbt = itemStack.getTagCompound();
        int use = SteamcraftEventHandler.use;
        int loaded = nbt.getInteger("loaded");

        double health = (double) (use) / (double) itemStack.getMaxItemUseDuration();
        if (Minecraft.getMinecraft().thePlayer.getHeldItemMainhand() == itemStack && use > -1 && loaded == 0 && itemStack.getMaxItemUseDuration() != 72000) {
            GL11.glPushMatrix();
            int j1 = (int) Math.round(13.0D - health * 13.0D);
            int k = (int) Math.round(255.0D - health * 255.0D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            Tessellator tessellator = Tessellator.getInstance();
            int l = 255 - k << 16 | k << 8;
            int i1 = (255 - k) / 4 << 16 | 16128;
            RenderUtility.renderQuad(tessellator, 2, 10, 13, 2, 0);
            RenderUtility.renderQuad(tessellator, 2, 10, 12, 1, i1);
            RenderUtility.renderQuad(tessellator, 2, 10, j1, 1, l);
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
            currentAmmo = itemStack.getTagCompound().getInteger("loaded");
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
            currentAmmo = itemStack.getTagCompound().getInteger("loaded");
        }
        health = (double) (maxAmmo - currentAmmo) / (double) maxAmmo;
        if (Minecraft.getMinecraft().thePlayer.getHeldItemMainhand() == itemStack && currentAmmo != 0) {
            GL11.glPushMatrix();
            int j1 = (int) Math.round(13.0D - health * 13.0D);
            int k = (int) Math.round(255.0D - health * 255.0D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            Tessellator tessellator = Tessellator.getInstance();
            int l = 255 - k << 16 | k << 8;
            int i1 = (255 - k) / 4 << 16 | 16128;
            RenderUtility.renderQuad(tessellator, 2, 1, 13, 2, 0);
            RenderUtility.renderQuad(tessellator, 2, 1, 12, 1, i1);
            RenderUtility.renderQuad(tessellator, 2, 1, j1, 1, l);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return new ArrayList<>();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return MapWrapper.handlePerspective(this, TransformUtils.DEFAULT_TOOL.getTransforms(), cameraTransformType);
    }
}
