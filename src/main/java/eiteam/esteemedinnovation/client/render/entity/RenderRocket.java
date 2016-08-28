package eiteam.esteemedinnovation.client.render.entity;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.client.render.RenderUtility;
import eiteam.esteemedinnovation.entity.projectile.EntityRocket;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderRocket extends Render {
    private static final ResourceLocation ARROW_TEXTURES = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/rocket.png");

    public RenderRocket(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!(entity instanceof EntityRocket)) {
            return;
        }

        EntityRocket rocket = (EntityRocket) entity;
        bindEntityTexture(rocket);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        Vec3d vec = new Vec3d(rocket.motionX, rocket.motionY, rocket.motionZ);
        vec = vec.normalize();
        float pitch = (float) Math.asin(vec.yCoord);
        float yaw = (float) Math.atan2(vec.xCoord, vec.zCoord);
        GL11.glRotatef((float) Math.toDegrees(yaw) - 90, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef((float) Math.toDegrees(pitch), 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float) (b0 * 10) / 32.0F;
        float f5 = (float) (5 + b0 * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float) (5 + b0 * 10) / 32.0F;
        float f9 = (float) (10 + b0 * 10) / 32.0F;
        float f10 = 0.05625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f11 = 0 - partialTicks;

        if (f11 > 0.0F) {
            float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
            GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
        }

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);
        RenderUtility.addVertexWithUV(buffer, -7D, -2D, -2D, f6, f8);
        RenderUtility.addVertexWithUV(buffer, -7D, -2D, 2D, f7, f8);
        RenderUtility.addVertexWithUV(buffer, -7D, 2D, 2D, f7, f9);
        RenderUtility.addVertexWithUV(buffer, -7D, 2D, -2D, f6, f9);
        tessellator.draw();
        GL11.glNormal3f(-f10, 0.0F, 0.0F);
        RenderUtility.addVertexWithUV(buffer, -7D, 2D, -2D, f6, f8);
        RenderUtility.addVertexWithUV(buffer, -7D, 2D, 2D, f7, f8);
        RenderUtility.addVertexWithUV(buffer, -7D, -2D, 2D, f7, f9);
        RenderUtility.addVertexWithUV(buffer, -7D, -2D, -2D, f6, f9);
        tessellator.draw();

        for (int i = 0; i < 4; ++i) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            RenderUtility.addVertexWithUV(buffer, -8D, -2D, 0D, f2, f4);
            RenderUtility.addVertexWithUV(buffer, 8D, -2D, 0D, f3, f4);
            RenderUtility.addVertexWithUV(buffer, 8D, 2D, 0D, f3, f5);
            RenderUtility.addVertexWithUV(buffer, -8D, 2D, -0D, f2, f5);
            tessellator.draw();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return ARROW_TEXTURES;
    }
}