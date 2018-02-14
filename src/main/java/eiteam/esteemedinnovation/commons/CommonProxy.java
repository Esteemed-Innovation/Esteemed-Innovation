package eiteam.esteemedinnovation.commons;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.commons.init.ContentModuleHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Constants.EI_MODID)
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

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ContentModuleHandler.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ContentModuleHandler.registerItems(event);
    }
}