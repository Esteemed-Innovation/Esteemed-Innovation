package eiteam.esteemedinnovation.commons.util;

import baubles.api.BaublesApi;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;

public class BaublesUtility {
    /**
     * @param player The player whose inventory to look in
     * @param item The item to look for
     * @return Whether the player is currently wearing the provided bauble.
     */
    public static boolean checkForUpgrade(EntityPlayer player, Item item) {
        IInventory inventory = BaublesApi.getBaubles(player);
        return ItemStackUtility.inventoryHasItem(inventory, item);
    }
}
