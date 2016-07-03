package flaxbeard.steamcraft.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import flaxbeard.steamcraft.item.tool.steam.SteamToolHelper;

public class ItemSteamToolRenderer extends ItemRenderer {
    public ItemSteamToolRenderer(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public void renderItem(EntityLivingBase entityIn, ItemStack item, ItemCameraTransforms.TransformType transform) {
        GL11.glPushMatrix();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        for (int pass = 0; pass < item.getItem().getRenderPasses(item.getItemDamage()); pass++) {
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            IIcon icon = item.getItem().getIcon(item, pass);
            int color = item.getItem().getColorFromItemStack(item, pass);
            // 16,777,215 is the "white" default returned when the pass does not render a drill head.
            if (color != 16777215) {
                float r = (float)(color >> 16 & 255) / 255.0F;
                float g = (float)(color >> 8 & 255) / 255.0F;
                float b = (float)(color & 255) / 255.0F;
                GL11.glColor4f(r, g, b, 1.0F);
            }
            renderItem.renderIcon(0, 0, icon, 16, 16);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        }

        int use = SteamToolHelper.checkNBT(item).getInteger("Speed");

        double health = (1000.0D - use) / 1000.0D;
        if (use > 0) {
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
        }
        GL11.glPopMatrix();
    }

    private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setColorOpaque_I(par6);
        par1Tessellator.addVertex((double) (par2), (double) (par3), 0.0D);
        par1Tessellator.addVertex((double) (par2), (double) (par3 + par5), 0.0D);
        par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + par5), 0.0D);
        par1Tessellator.addVertex((double) (par2 + par4), (double) (par3), 0.0D);
        par1Tessellator.draw();
    }
}
