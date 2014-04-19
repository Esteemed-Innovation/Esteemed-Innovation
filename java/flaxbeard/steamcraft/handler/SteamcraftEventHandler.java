package flaxbeard.steamcraft.handler;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilEnhancements;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;

public class SteamcraftEventHandler {
	@SubscribeEvent
	public void clickLeft(PlayerInteractEvent event) {
		if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) != null && !event.entityPlayer.worldObj.isRemote) { 
			if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) instanceof ISteamTransporter) {
				System.out.println(((ISteamTransporter)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getSteam() + " " + ((ISteamTransporter)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getPressure());
			}
		}
	}
	
	@SubscribeEvent
	public void handleEnhancement(AnvilUpdateEvent event) {
		if (event.right.getItem() instanceof IEnhancement) {
			IEnhancement enhancement = (IEnhancement) event.right.getItem();
			if (enhancement.canApplyTo(event.left) && UtilEnhancements.canEnhance(event.left)) {
				event.cost = enhancement.cost(event.left);
				event.output = UtilEnhancements.getEnhancedItem(event.left, event.right);
			}
		}
	}
}
