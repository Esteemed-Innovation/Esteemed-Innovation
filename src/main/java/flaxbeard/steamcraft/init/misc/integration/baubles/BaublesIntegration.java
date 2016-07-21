package flaxbeard.steamcraft.init.misc.integration.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import flaxbeard.steamcraft.init.items.tools.GadgetItems;
import flaxbeard.steamcraft.misc.ItemStackUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;

public class BaublesIntegration {
    public static boolean checkForUpgrade(EntityPlayer player, Item item) {
        IInventory inventory = BaublesApi.getBaubles(player);
        return ItemStackUtility.inventoryHasItem(inventory, item);
    }

    public static boolean checkForSurvivalist(EntityPlayer player) {
        return checkForUpgrade(player, GadgetItems.Items.SURVIVALIST_TOOLKIT.getItem());
    }

    public static boolean checkForSteamCellFiller(EntityPlayer player) {
        return checkForUpgrade(player, GadgetItems.Items.STEAM_CELL_FILLER.getItem());
    }

    public static Item getSurvivalist() {
        return new ItemBauble(BaubleType.BELT).setMaxStackSize(1);
    }

    public static Item getSteamCellFiller() {
        return new ItemBauble(BaubleType.AMULET).setMaxStackSize(1);
    }
}
