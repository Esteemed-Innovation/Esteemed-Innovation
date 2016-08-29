package eiteam.esteemedinnovation.client.particle;

import eiteam.esteemedinnovation.EsteemedInnovation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleAsterisk extends ParticleAlphabeticGeneric {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EsteemedInnovation.MOD_ID, "particles/asterisk");

    public ParticleAsterisk(World world, double posX, double posY, double posZ) {
        super(world, posX, posY, posZ);

        //#00c300
        particleRed = 0;
        particleGreen = 0.765F;
        particleBlue = 0;

        particleAlpha = 0.5F;

        particleGravity = 0;
        motionX *= (Math.random() * 2.0D - 1.0D) + 0.009999999776482582D;
        motionY *= (Math.random() * 2.0D - 1.0D) + 0.009999999776482582D;
        motionZ *= (Math.random() * 2.0D - 1.0D) + 0.009999999776482582D;
        motionY += 0.1D;
        particleScale *= 2F;
        particleMaxAge = 16;

        this.posX -= rand.nextInt(10);
        this.posY -= rand.nextInt(10);
        this.posZ -= rand.nextInt(10);

        prevPosX = this.posX;
        prevPosY = this.posY;
        prevPosZ = this.posZ;

        particleTextureIndexX = 10;
        particleTextureIndexY = 2;
    }
}
