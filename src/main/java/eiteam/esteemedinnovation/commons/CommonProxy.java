package eiteam.esteemedinnovation.commons;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.World;

public class CommonProxy {
    public void registerTexturesToStitch() {}

    public void spawnBreakParticles(World world, float x, float y, float z, Block block, float xv, float yv, float zv) {}

    public void spawnAsteriskParticles(World world, float x, float y, float z) {}
    public void spawnExclamationParticles(World world, float x, float y, float z) {}

    public void extendRange(Entity entity, double amount) {
        if (entity instanceof EntityPlayerMP) {
            PlayerInteractionManager mgr = ((EntityPlayerMP) entity).interactionManager;
            double reach = mgr.getBlockReachDistance();
            mgr.setBlockReachDistance(reach + amount);
        }
    }

    public void checkRange(EntityLivingBase entity) {}
}