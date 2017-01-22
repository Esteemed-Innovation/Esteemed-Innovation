package eiteam.esteemedinnovation.commons.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtility {
    /**
     * Player-specific overload for {@link #consumeInventoryItem(IInventory, Item)}
     */
    public static void consumeInventoryItem(EntityPlayer player, Item item) {
        consumeInventoryItem(player.inventory, item);
    }

    /**
     * Removes 1 of the provided item from the provided inventory. It will try to deplete the stack size, but if it is
     * already 1, it will remove the stack completely.
     * @param inventory The inventory to remove the item from
     * @param item The item to deplete
     */
    public static void consumeInventoryItem(IInventory inventory, Item item) {
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stackInSlot = inventory.getStackInSlot(slot);
            if (stackInSlot != null && stackInSlot.getItem() == item) {
                if (stackInSlot.stackSize > 1) {
                    stackInSlot.stackSize--;
                    inventory.setInventorySlotContents(slot, stackInSlot);
                } else {
                    inventory.setInventorySlotContents(slot, null);
                }
            }
        }
    }
}
