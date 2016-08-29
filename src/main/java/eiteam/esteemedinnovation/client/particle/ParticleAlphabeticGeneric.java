package eiteam.esteemedinnovation.client.particle;

import eiteam.esteemedinnovation.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleAlphabeticGeneric extends Particle {
    protected ParticleAlphabeticGeneric(World world, double posX, double posY, double posZ) {
        super(world, posX, posY, posZ, 0, 0, 0);
    }

    // Gross duplication of Particle#renderParticle in order to bypass the particleTexture null check.
    @Override
    public void renderParticle(VertexBuffer buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = (float) particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float) particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * particleScale;

        float f5 = (float) (prevPosX + (posX - prevPosX) * (double) partialTicks - interpPosX);
        float f6 = (float) (prevPosY + (posY - prevPosY) * (double) partialTicks - interpPosY);
        float f7 = (float) (prevPosZ + (posZ - prevPosZ) * (double) partialTicks - interpPosZ);
        int i = getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[] {
          new Vec3d((double) (-rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (-rotationYZ * f4 - rotationXZ * f4)),
          new Vec3d((double) (-rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (-rotationYZ * f4 + rotationXZ * f4)),
          new Vec3d((double) (rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (rotationYZ * f4 + rotationXZ * f4)),
          new Vec3d((double) (rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (rotationYZ * f4 - rotationXZ * f4))
        };

        if (particleAngle != 0.0F) {
            float f8 = particleAngle + (particleAngle - prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float) field_190016_K.xCoord;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float) field_190016_K.yCoord;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float) field_190016_K.zCoord;
            Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);

            for (int l = 0; l < 4; ++l) {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
            }
        }


        buffer.pos((double) f5 + avec3d[0].xCoord, (double) f6 + avec3d[0].yCoord, (double) f7 + avec3d[0].zCoord).tex((double) f1, (double) f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double) f5 + avec3d[1].xCoord, (double) f6 + avec3d[1].yCoord, (double) f7 + avec3d[1].zCoord).tex((double) f1, (double) f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double) f5 + avec3d[2].xCoord, (double) f6 + avec3d[2].yCoord, (double) f7 + avec3d[2].zCoord).tex((double) f, (double) f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double) f5 + avec3d[3].xCoord, (double) f6 + avec3d[3].yCoord, (double) f7 + avec3d[3].zCoord).tex((double) f, (double) f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();

        Minecraft.getMinecraft().getTextureManager().bindTexture(ClientProxy.FONT_ASCII);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
