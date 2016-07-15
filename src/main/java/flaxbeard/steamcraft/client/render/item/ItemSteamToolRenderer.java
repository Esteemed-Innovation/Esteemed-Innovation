package flaxbeard.steamcraft.client.render.item;

import codechicken.lib.render.IItemRenderer;
import codechicken.lib.render.TextureUtils;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import flaxbeard.steamcraft.api.IRenderItem;
import flaxbeard.steamcraft.client.render.RenderUtility;
import flaxbeard.steamcraft.item.tool.steam.SteamToolHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

public class ItemSteamToolRenderer implements IItemRenderer {
    @Override
    public void renderItem(ItemStack stack) {
        GL11.glPushMatrix();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Item item = stack.getItem();
        if (item instanceof IRenderItem) {
            IRenderItem renderItem = (IRenderItem) item;
            ResourceLocation icon = renderItem.getIcon(stack);
            if (icon != null) {
                for (int pass = 0; pass < renderItem.renderPasses(stack); pass++) {
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    int color = Minecraft.getMinecraft().getItemColors().getColorFromItemstack(stack, pass);
                    if (color != RenderUtility.WHITE) {
                        float r = (float) (color >> 16 & 255) / 255.0F;
                        float g = (float) (color >> 8 & 255) / 255.0F;
                        float b = (float) (color & 255) / 255.0F;
                        GL11.glColor4f(r, g, b, 1.0F);
                    }
                    TextureUtils.changeTexture(icon);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                }
            }
        }

        int use = SteamToolHelper.checkNBT(stack).getInteger("Speed");

        double health = (1000.0D - use) / 1000.0D;
        if (use > 0) {
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
        }
        GL11.glPopMatrix();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return null;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return null;
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
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
