package flaxbeard.steamcraft.common;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.packet.SteamcraftServerPacketHandler;


public class CommonProxy
{
    public void registerRenderers()
    {
   	 	Steamcraft.channel.register(new SteamcraftServerPacketHandler());

    }
    
	public void serverStarting(FMLServerStartingEvent event) {
		//event.registerServerCommand(new CommandUpdateSteamcraft());
	}
    
    public void spawnBreakParticles(World world, float x, float y, float z, Block blokc, float xv, float yv, float zv) {}

	public void registerHotkeys() {		
	}

	public boolean isKeyPressed() {
		return false;
	}

	public void extendRange(Entity entity, float amount) {
		if(entity instanceof EntityPlayerMP)
			((EntityPlayerMP) entity).theItemInWorldManager.setBlockReachDistance(((EntityPlayerMP) entity).theItemInWorldManager.getBlockReachDistance() + amount);
	}
}