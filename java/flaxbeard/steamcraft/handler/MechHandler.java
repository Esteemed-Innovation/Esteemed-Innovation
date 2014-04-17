package flaxbeard.steamcraft.handler;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.steamcraft.api.ISteamTransporter;

public class MechHandler {
	@SubscribeEvent
	public void clickLeft(PlayerInteractEvent event) {
		if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) != null && !event.entityPlayer.worldObj.isRemote) { 
			if (event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) instanceof ISteamTransporter) {
				System.out.println(((ISteamTransporter)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getSteam() + " " + ((ISteamTransporter)event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getPressure());
			}
		}
	}
}
