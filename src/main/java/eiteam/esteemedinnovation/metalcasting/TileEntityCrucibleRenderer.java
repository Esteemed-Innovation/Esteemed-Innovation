package eiteam.esteemedinnovation.metalcasting;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class TileEntityCrucibleRenderer extends TileEntitySpecialRenderer<TileEntityCrucible> {
    private int frame = 0;

    @Override
    public void renderTileEntityAt(TileEntityCrucible crucible, double x, double y, double z, float partialTicks, int destroyStage) {
        IBlockState state = crucible.getWorld().getBlockState(crucible.getPos());
        EnumFacing facing = state.getValue(BlockCrucible.FACING);

        // FIXME: If someone wants to give me a proper formula for this, that would be nice. I'm fine with it being
        // hardcoded though, since we only have 4 possible FACING values. Just be sure to test your stuff.
        float angle = 90F;
        switch (facing) {
            case WEST: {
                angle = 270F;
                break;
            }
            case NORTH: {
                angle = 180F;
                break;
            }
            case EAST: {
                angle = 90F;
                break;
            }
            case SOUTH: {
                angle = 0F;
                break;
            }
            default: {
                break;
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate(x, y, z);

        int ticks = crucible.tipTicks;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate(angle, 0F, 1F, 0F);
        GlStateManager.scale(1F, -1F, -1F);
        GlStateManager.translate(0, -0.5, 0);
        if (ticks > 135) {
            ticks = (int) ((ticks - 90) / 5.0F * 90);
            GlStateManager.rotate((MathHelper.sin((float) (Math.PI * (ticks / 90.0F)))) * 5F, 1F, 0F, 0F);
        } else if (ticks > 120) {
            ticks = (int) ((ticks - 90) / 15.0F * 90);
            GlStateManager.rotate((MathHelper.sin((float) (Math.PI * (ticks / 90.0F)))) * 15F, 1F, 0F, 0F);
        } else if (ticks > 90) {
            ticks = (int) ((ticks - 90) / 30.0F * 90);
            GlStateManager.rotate((MathHelper.sin((float) (Math.PI * (ticks / 90.0F)))) * 30F, -1F, 0F, 0F);
        } else {
            GlStateManager.rotate((MathHelper.sin((float) (Math.PI * (ticks / 90.0F)))) * 75F, 1F, 0F, 0F);
        }
        GlStateManager.translate(-0.5, 0.5, -0.5);
        GlStateManager.scale(1F, -1F, -1F);
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1F, false);

        int fill = crucible.getFill();
        if (fill > 0) {
            GlStateManager.disableBlend();
            float height = (2F + (fill / 90F) * 11F) / 16F;
            GlStateManager.rotate(180F, 1F, 0F, 0F);
            GlStateManager.translate(0F, -height, -1F);
            renderLiquid(crucible);
            GlStateManager.enableBlend();
        }
        GlStateManager.popMatrix();

        frame++;
        if (frame == 20) {
            frame = 0;
        }
    }

    private void renderLiquid(TileEntityCrucible crucible) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(BlockCrucible.LIQUID_ICON_RL.toString());
        // See crucible_liquid mcmeta
        if (frame % 2 == 0) {
            icon.updateAnimation();
        }
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        float f1 = icon.getMaxU();
        float f4 = icon.getMaxV();
        float f2 = icon.getMinV();
        float f3 = icon.getMinU();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        CrucibleLiquid liquid = crucible.contents.get(0);
        int r = liquid.getRed();
        int g = liquid.getGreen();
        int b = liquid.getBlue();
        int a = liquid.getAlpha();
        buffer.pos(0.125D, 0, 0.125D).tex(f1, f4).color(r, g, b, a).endVertex();
        buffer.pos(0.875D, 0, 0.125D).tex(f3, f4).color(r, g, b, a).endVertex();
        buffer.pos(0.875D, 0, 0.875D).tex(f3, f2).color(r, g, b, a).endVertex();
        buffer.pos(0.125D, 0, 0.875D).tex(f1, f2).color(r, g, b, a).endVertex();
        tessellator.draw();
    }
}