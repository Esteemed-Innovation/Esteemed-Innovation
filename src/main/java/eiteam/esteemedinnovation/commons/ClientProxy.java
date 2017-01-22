package eiteam.esteemedinnovation.commons;

import codechicken.lib.render.CCIconRegister;
import eiteam.esteemedinnovation.boiler.GuiBoiler;
import eiteam.esteemedinnovation.commons.particle.ParticleAlphabeticGeneric;
import eiteam.esteemedinnovation.metalcasting.BlockCrucible;
import eiteam.esteemedinnovation.misc.PlayerController;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
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
        spawnParticles(new ParticleDigging.Factory().getEntityFX(0, world, x, y, z, xv, yv, zv, 2));
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

    private void setController(Minecraft minecraft) {
        PlayerControllerMP controller = minecraft.playerController;
        World world = minecraft.theWorld;
        if (!(controller instanceof PlayerController)) {
            GameType type = world.getWorldInfo().getGameType();
            NetHandlerPlayClient net = minecraft.getConnection();
            PlayerController ourController = new PlayerController(minecraft, net);
            ourController.setGameType(type);
            minecraft.playerController = ourController;
        }
    }

    @Override
    public void extendRange(Entity entity, double amount) {
        super.extendRange(entity, amount);
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (entity == player) {
            setController(mc);

            ((PlayerController) mc.playerController).setReachDistanceExtension(((PlayerController) mc.playerController).getReachDistanceExtension() + amount);
        }
    }

    @Override
    public void checkRange(EntityLivingBase entity) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (entity == player) {
            setController(mc);
            if (((PlayerController) mc.playerController).getReachDistanceExtension() <= 2.0F) {
                extendRange(entity, 2.0F - ((PlayerController) mc.playerController).getReachDistanceExtension());
            }
        }
    }

}