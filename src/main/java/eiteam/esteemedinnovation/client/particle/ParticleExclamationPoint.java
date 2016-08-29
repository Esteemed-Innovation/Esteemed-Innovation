package eiteam.esteemedinnovation.client.particle;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleExclamationPoint extends ParticleAlphabeticGeneric {
    public ParticleExclamationPoint(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);

        // #FFD700
        particleRed = 1;
        particleGreen = 0.843F;
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

        particleTextureIndexX = 1;
        particleTextureIndexY = 2;
    }
}
