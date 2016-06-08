package flaxbeard.steamcraft.integration.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.init.items.tools.GadgetItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;

public class BaublesIntegration {
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
        return checkForUpgrade(player, GadgetItems.Items.SURVIVALIST_TOOLKIT.getItem());
    }

    public static boolean checkForSteamCellFiller(EntityPlayer player) {
        return checkForUpgrade(player, GadgetItems.Items.STEAM_CELL_FILLER.getItem());
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
