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

    public ParticleAlphabeticGeneric(World world, double posX, double posY, double posZ, float red, float green, float blue, float alpha, int textureX, int textureY) {
        this(world, posX, posY, posZ);

        particleRed = red;
        particleGreen = green;
        particleBlue = blue;
        particleAlpha = alpha;

        particleGravity = 0;
        motionX *= (Math.random() * 2.0D - 1.0D) + 0.009999999776482582D;
        motionY *= (Math.random() * 2.0D - 1.0D) + 0.009999999776482582D;
        motionZ *= (Math.random() * 2.0D - 1.0D) + 0.009999999776482582D;
        motionY += 0.1D;
        particleScale *= 2F;
        particleMaxAge = 32;

        this.posX -= rand.nextInt(10);
        this.posY -= rand.nextInt(10);
        this.posZ -= rand.nextInt(10);

        prevPosX = this.posX;
        prevPosY = this.posY;
        prevPosZ = this.posZ;

        particleTextureIndexX = textureX;
        particleTextureIndexY = textureY;
    }

    // Gross duplication of Particle#renderParticle in order to bypass the particleTexture null check.
    @Override
    public void renderParticle(VertexBuffer buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * particleScale;

        float f5 = (float) (prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float f6 = (float) (prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float f7 = (float) (prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);
        int i = getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[] {
          new Vec3d((-rotationX * f4 - rotationXY * f4), (-rotationZ * f4), (-rotationYZ * f4 - rotationXZ * f4)),
          new Vec3d((-rotationX * f4 + rotationXY * f4), (rotationZ * f4), (-rotationYZ * f4 + rotationXZ * f4)),
          new Vec3d((rotationX * f4 + rotationXY * f4), (rotationZ * f4), (rotationYZ * f4 + rotationXZ * f4)),
          new Vec3d((rotationX * f4 - rotationXY * f4), (-rotationZ * f4), (rotationYZ * f4 - rotationXZ * f4))
        };

        if (particleAngle != 0.0F) {
            float f8 = particleAngle + (particleAngle - prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float) field_190016_K.xCoord;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float) field_190016_K.yCoord;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float) field_190016_K.zCoord;
            Vec3d vec3d = new Vec3d(f10, f11, f12);

            for (int l = 0; l < 4; ++l) {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((2.0F * f9)));
            }
        }


        buffer.pos(f5 + avec3d[0].xCoord, f6 + avec3d[0].yCoord, f7 + avec3d[0].zCoord).tex(f1, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(f5 + avec3d[1].xCoord, f6 + avec3d[1].yCoord, f7 + avec3d[1].zCoord).tex(f1, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(f5 + avec3d[2].xCoord, f6 + avec3d[2].yCoord, f7 + avec3d[2].zCoord).tex(f, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(f5 + avec3d[3].xCoord, f6 + avec3d[3].yCoord, f7 + avec3d[3].zCoord).tex(f, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();

        Minecraft.getMinecraft().getTextureManager().bindTexture(ClientProxy.FONT_ASCII);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
