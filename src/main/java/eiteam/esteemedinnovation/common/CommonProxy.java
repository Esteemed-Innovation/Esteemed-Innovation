package eiteam.esteemedinnovation.common;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public abstract class CommonProxy {
    public abstract void registerModels();

    public abstract void registerRenderers();

    public abstract void registerTexturesToStitch();

    public abstract void spawnBreakParticles(World world, float x, float y, float z, Block block, float xv, float yv, float zv);

    public abstract void registerHotkeys();

    public void extendRange(Entity entity, double amount) {
        if (entity instanceof EntityPlayerMP) {
            PlayerInteractionManager mgr = ((EntityPlayerMP) entity).interactionManager;
            double reach = mgr.getBlockReachDistance();
            mgr.setBlockReachDistance(reach + amount);
        }
    }

    public abstract void checkRange(EntityLivingBase entity);

    public static void logInfo(String string){
        FMLLog.info("[EI]: " + string);
    }
}