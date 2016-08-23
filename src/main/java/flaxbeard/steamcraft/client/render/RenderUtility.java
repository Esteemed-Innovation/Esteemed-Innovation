package flaxbeard.steamcraft.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class RenderUtility {
    /**
     * 16,777,215 is the "white" default returned when the pass does not render a drill head.
     */
    public static final int WHITE = 16_777_215;

    /**
     * Effectively the same thing as the old Tessellator#addVertexWithUV method.
     * @param buffer The vertex buffer
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param u
     * @param v
     */
    public static void addVertexWithUV(VertexBuffer buffer, double x, double y, double z, double u, double v) {
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y, z);
        buffer.tex(u, v);
        buffer.endVertex();
    }

    public static void renderQuad(Tessellator tessellator, int par2, int par3, int par4, int par5, int par6) {
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.putColor4(par6);
        buffer.pos(par2, par3, 0D);
        buffer.pos(par2, par3 + par5, 0D);
        buffer.pos(par2 + par4, par3 + par5, 0D);
        buffer.pos(par2 + par4, par3, 0D);
        buffer.endVertex();
        tessellator.draw();
    }

    private static HashMap<ResourceLocation, IBakedModel> bakedModels = new HashMap<>();

    public static IBakedModel bakeModel(ResourceLocation loc) {
        if (bakedModels.containsKey(loc)) {
            return bakedModels.get(loc);
        }

        try {
            IModel model = ModelLoaderRegistry.getModel(loc);
            IBakedModel bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
              ModelLoader.defaultTextureGetter());
            bakedModels.put(loc, bakedModel);
            return bakedModel;

            // Stupid vanilla, throwing generic exceptions
        } catch (Exception e) {
            throw new ReportedException(new CrashReport("Error loading custom model " + loc.toString(), e));
        }
    }

    /**
     * Renders a baked model from the resource location pointing to the model JSON.
     * <b>Always draw after calling this method, otherwise the vertex will never finish building!</b>
     * @param buffer The VertexBuffer
     * @param modelLocation The model, do not include .json
     */
    public static void renderModel(VertexBuffer buffer, ResourceLocation modelLocation) {
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        IBakedModel model = bakeModel(modelLocation);
        for (BakedQuad quad : model.getQuads(null, null, 0)) {
            buffer.addVertexData(quad.getVertexData());
        }
    }
}
