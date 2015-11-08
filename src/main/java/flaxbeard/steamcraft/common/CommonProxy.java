package flaxbeard.steamcraft.common;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class CommonProxy {
    public void registerRenderers() {}

    public void serverStarting(FMLServerStartingEvent event) {
        //event.registerServerCommand(new CommandUpdateSteamcraft());
    }

    public void spawnBreakParticles(World world, float x, float y, float z, Block blokc, float xv, float yv, float zv) {}

    public void registerHotkeys() {}

    public void extendRange(Entity entity, float amount) {
        if (entity instanceof EntityPlayerMP)
            ((EntityPlayerMP) entity).theItemInWorldManager.setBlockReachDistance(((EntityPlayerMP) entity).theItemInWorldManager.getBlockReachDistance() + amount);
    }

    public void checkRange(EntityLivingBase entity) {
        // TODO Auto-generated method stub
    }

    public static void logInfo(String string){
        FMLLog.info("[FSP]: " + string);
    }
}