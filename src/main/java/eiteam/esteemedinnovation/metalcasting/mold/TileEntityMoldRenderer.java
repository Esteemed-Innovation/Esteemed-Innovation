package eiteam.esteemedinnovation.metalcasting.mold;

import eiteam.esteemedinnovation.api.mold.CrucibleMold;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.util.RenderUtility;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityMoldRenderer extends TileEntitySpecialRenderer<TileEntityMold> {
    private static final ResourceLocation MODEL_RL = new ResourceLocation(EsteemedInnovation.MOD_ID, "block/mold_top");
    private static final float PX = (1F / 16F);

    @Override
    public void render(TileEntityMold mold, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.translate(x, y, z);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        EnumFacing dir = getWorld().getBlockState(mold.getPos()).getValue(BlockMold.FACING);
        if (dir == EnumFacing.SOUTH) {
            GlStateManager.rotate(180F, 0, 1, 0);
        } else if (dir == EnumFacing.EAST) {
            GlStateManager.rotate(270F, 0, 1, 0);
        } else if (dir == EnumFacing.WEST) {
            GlStateManager.rotate(90F, 0, 1, 0);
        }

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180F, 1, 0, 0);
        // FIXME: Z Fighting when really high above the mold.
        GlStateManager.translate(-0.5, (3.999F / 16F), -0.5);

        renderMold(mold.mold, true);
        GlStateManager.translate(0, 0.001, 0);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180F, 0, 1, 0);
        GlStateManager.translate(0, -4 * PX, -6 * PX);
        float tick = (float) (Math.PI * (mold.changeTicks * 90.0F / 20.0F)) / 180.0F;
        float rotation = mold.isOpen ? 80F + MathHelper.sin(tick) * 100F : MathHelper.sin(tick) * -100F;
        if (mold.isOpen) {
            GlStateManager.rotate(180F, 1, 0, 0);
        }
        GlStateManager.rotate(rotation, 1, 0, 0);
        GlStateManager.translate(-0.5, -0.5, -0.5);
        GlStateManager.translate(0, 4 * PX, 6 * PX);
        RenderUtility.renderModel(Tessellator.getInstance().getBuffer(), MODEL_RL);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180F, 0, 1, 0);
        GlStateManager.translate(0, -4 * PX, -6 * PX);
        if (mold.isOpen) {
            GlStateManager.rotate(180F, 1, 0, 0);
        }
        GlStateManager.rotate(rotation, 1, 0, 0);
        GlStateManager.translate(-0.5, -0.5, -0.5);
        GlStateManager.translate(0, 4 * PX, 6 * PX);
        GlStateManager.translate(0, 3.999 * PX, 0);
        renderMold(mold.mold, true);

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
    }

    private void renderMold(ItemStack item, boolean bottom) {
        bindTexture(((CrucibleMold) item.getItem()).getBlockTexture(item));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
//        buffer.putNormal(0F, 0F, 1F);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        if (bottom) {
            buffer.pos(0D, 0D, 0D).tex(0F, 0F).endVertex();
            buffer.pos(1D, 0D, 0D).tex(1F, 0F).endVertex();
            buffer.pos(1D, 0D, 1D).tex(1F, 1F).endVertex();
            buffer.pos(0D, 0D, 1D).tex(0F, 1F).endVertex();
        } else {
            buffer.pos(1D, 0D, 1D).tex(0F, 0F).endVertex();
            buffer.pos(0D, 0D, 1D).tex(1F, 0F).endVertex();
            buffer.pos(0D, 0D, 0D).tex(1F, 1F).endVertex();
            buffer.pos(1D, 0D, 0D).tex(0F, 1F).endVertex();
        }
        tessellator.draw();
    }
}
