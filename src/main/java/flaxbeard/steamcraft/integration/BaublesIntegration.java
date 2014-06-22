package flaxbeard.steamcraft.integration;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import baubles.api.BaubleType;
import baubles.api.BaublesApi;

public class BaublesIntegration {

	public static boolean checkForSurvivalist(EntityPlayer player) {
		IInventory inventory = BaublesApi.getBaubles(player);
		for (int i = 0; i<inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null) {
				if (inventory.getStackInSlot(i).getItem() == SteamcraftItems.survivalist) {
					return true;
				}
			}
		}
		return false;
	}

	public static Item getSurvivalist() {
		return new ItemBauble(BaubleType.BELT).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:survivalist").setTextureName("steamcraft:toolkit").setMaxStackSize(1);
	}

}
