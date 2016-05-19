package flaxbeard.steamcraft.integration.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;

public class BaublesIntegration {
    
	private BaublesIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static boolean checkForUpgrade(EntityPlayer player, Item item) {
        IInventory inventory = BaublesApi.getBaubles(player);
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i) != null) {
                if (inventory.getStackInSlot(i).getItem() == item) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkForSurvivalist(EntityPlayer player) {
        return checkForUpgrade(player, SteamcraftItems.survivalist);
    }

    public static boolean checkForSteamCellFiller(EntityPlayer player) {
        return checkForUpgrade(player, SteamcraftItems.steamcellBauble);
    }

    public static Item getSurvivalist() {
        return new ItemBauble(BaubleType.BELT).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:survivalist").setTextureName("steamcraft:toolkit").setMaxStackSize(1);
    }

    public static Item getSteamCellFiller() {
        return new ItemBauble(BaubleType.AMULET/*subject to change*/)
          .setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:steamcellFiller")
          .setTextureName("steamcraft:steamcellFiller").setMaxStackSize(1);
    }
}
