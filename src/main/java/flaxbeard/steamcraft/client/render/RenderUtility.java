package flaxbeard.steamcraft.client.render;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderUtility {
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
}
