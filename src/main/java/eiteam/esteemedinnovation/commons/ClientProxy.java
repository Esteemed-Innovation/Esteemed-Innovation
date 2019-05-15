package eiteam.esteemedinnovation.commons;

import codechicken.lib.render.CCIconRegister;
import eiteam.esteemedinnovation.boiler.GuiBoiler;
import eiteam.esteemedinnovation.commons.particle.ParticleAlphabeticGeneric;
import eiteam.esteemedinnovation.metalcasting.crucible.BlockCrucible;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {
    public static final ResourceLocation FONT_ASCII = new ResourceLocation("minecraft", "textures/font/ascii.png");

    @Override
    public void registerTexturesToStitch() {
        CCIconRegister.registerTexture(BlockCrucible.LIQUID_ICON_RL);
        CCIconRegister.registerTexture(GuiBoiler.STEAM_RL);
    }

    @Override
    public void spawnBreakParticles(World world, float x, float y, float z, Block block, float xv, float yv, float zv) {
//      The first argument of getEntityFX is the particle ID, and it is not used in the method at all.
        spawnParticles(new ParticleDigging.Factory().createParticle(0, world, x, y, z, xv, yv, zv, 2));
    }

    @Override
    public void spawnAsteriskParticles(World world, float x, float y, float z) {
        // #00c300
        for (int i = 0; i < world.rand.nextInt(4) + 1; i++) {
            spawnParticles(new ParticleAlphabeticGeneric(world, x, y, z, 0, 0.765F, 0, 0.75F, 10, 2));
        }
    }

    @Override
    public void spawnExclamationParticles(World world, float x, float y, float z) {
        // #FFD700
        for (int i = 0; i < world.rand.nextInt(4) + 1; i++) {
            spawnParticles(new ParticleAlphabeticGeneric(world, x, y, z, 1, 0.843F, 0, 0.75F, 1, 2));
        }
    }

    private void spawnParticles(Particle particle) {
        if (!Config.disableParticles) {
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        }
    }

}