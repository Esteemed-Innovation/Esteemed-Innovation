package eiteam.esteemedinnovation.api.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * A simple utility class for interacting with Minecraft's inventories, containers, and slots.
 */
public final class InventoryUtility {
    /**
     * A Field for use with reflection which retrieves the {@link InventoryCrafting#eventHandler} field.
     */
    private static final Field INVENTORYCRAFTING_EVENT_HANDLER_FIELD = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c");

    /**
     * A Field for use with reflection which retrieves the {@link ContainerPlayer#player} field.
     */
    private static final Field CONTAINERPLAYER_PLAYER_FIELD = ReflectionHelper.findField(ContainerPlayer.class, "player", "field_82862_h");

    /**
     * A Field for use with reflection which retrieves the {@link SlotCrafting#player} field.
     */
    private static final Field SLOTCRAFTING_PLAYER_FIELD = ReflectionHelper.findField(SlotCrafting.class, "player", "field_75238_b");

    /**
     * Gets the player currently using this {@link InventoryCrafting}. This has support for the crafting inventory
     * in {@link ContainerPlayer} (player's inventory) and implementers of {@link ContainerWorkbench}.
     * @param inv The current crafting inventory.
     * @return The player, or null if there either is no player, or they are using a weird crafting table we don't know about.
     */
    @Nullable
    public static EntityPlayer getCurrentPlayerFromInventoryCrafting(InventoryCrafting inv) {
        try {
            Container container = (Container) INVENTORYCRAFTING_EVENT_HANDLER_FIELD.get(inv);
            if (container instanceof ContainerPlayer) {
                return (EntityPlayer) CONTAINERPLAYER_PLAYER_FIELD.get(container);
            } else if (container instanceof ContainerWorkbench) {
                return (EntityPlayer) SLOTCRAFTING_PLAYER_FIELD.get(container.getSlot(0));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Player-specific overload for {@link #consumeInventoryItem(IInventory, Item)}
     */
    public static void consumeInventoryItem(EntityPlayer player, Item item) {
        consumeInventoryItem(player.inventory, item);
    }

    /**
     * Removes 1 of the provided item from the provided inventory. It will deplete the stack size as handled by vanilla.
     * @param inventory The inventory to remove the item from
     * @param item The item to deplete
     */
    public static void consumeInventoryItem(IInventory inventory, Item item) {
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stackInSlot = inventory.getStackInSlot(slot);
            if (!stackInSlot.isEmpty() && stackInSlot.getItem() == item) {
                stackInSlot.shrink(1);
                inventory.setInventorySlotContents(slot, stackInSlot);
            }
        }
    }

    /**
     * @param player The player to check the hotbar of
     * @param item The item to search for
     * @return Whether the provided item is in the player's hotbar.
     */
    public static boolean hasItemInHotbar(EntityPlayer player, Item item) {
        for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
            ItemStack stackInSlot = player.inventory.getStackInSlot(i);
            if (stackInSlot.getItem() == item) {
                return true;
            }
        }
        return false;
    }
}
