package eiteam.esteemedinnovation.api.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ItemStackUtility {
    /**
     * A performance-friendly cache of all the equipment slots.
     */
    public static final EntityEquipmentSlot[] EQUIPMENT_SLOTS = EntityEquipmentSlot.values();

    /**
     * A performance-friendly cache of all armor slots (excludes hand slots).
     */
    public static final EntityEquipmentSlot[] ARMOR_SLOTS = new ArrayList<>(Arrays.asList(EQUIPMENT_SLOTS))
      .stream()
      .filter(slot -> slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
      .collect(Collectors.toList())
      .toArray(new EntityEquipmentSlot[] {});

    /**
     * Public version of {@link net.minecraft.item.crafting.FurnaceRecipes#compareItemStacks(ItemStack, ItemStack)}
     */
    public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        return stack2.getItem() == stack1.getItem() &&
                (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    /**
     * Gets the EntityEquipmentSlot by enum index.
     * @param index The index
     * @return The slot or null.
     */
    @Nullable
    public static EntityEquipmentSlot getSlotFromSlotIndex(int index) {
        for (EntityEquipmentSlot slot : EQUIPMENT_SLOTS) {
            if (slot.getSlotIndex() == index) {
                return slot;
            }
        }
        return null;
    }

    @Nullable
    public static EntityEquipmentSlot getSlotFromIndex(int index) {
        for (EntityEquipmentSlot slot : EQUIPMENT_SLOTS) {
            if (slot.getIndex() == index) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Gets the player's held item by priority (main, off, null)
     * @param player The player
     * @return The main hand itemstack, offhand itemstack, or null.
     */
    @Nullable
    public static ItemStack getHeldItemStack(EntityPlayer player) {
        ItemStack mainHand = player.getHeldItemMainhand();
        ItemStack offHand = player.getHeldItemOffhand();

        if (mainHand == null) {
            if (offHand == null) {
                return null;
            } else {
                return offHand;
            }
        } else {
            return mainHand;
        }
    }

    /**
     * Checks if the item is in the inventory. Does not handle metadata.
     * @param inventory The inventory
     * @param check The item
     * @return boolean
     */
    public static boolean inventoryHasItem(IInventory inventory, Item check) {
        return findItemStackFromInventory(inventory, check) != null;
    }

    /**
     * Searches for an item in the inventory and returns its stack.
     * @param haystack The inventory to search in
     * @param needle The item to search for
     * @return The itemstack, or null
     */
    public static ItemStack findItemStackFromInventory(IInventory haystack, Item needle) {
        for (int slot = 0; slot < haystack.getSizeInventory(); slot++) {
            ItemStack inSlot = haystack.getStackInSlot(slot);
            if (inSlot == null) {
                continue;
            }
            if (inSlot.getItem() == needle) {
                return inSlot;
            }
        }
        return null;
    }

    /**
     * A very specific method for consuming an ItemStack in the player's inventory.
     * This is basically the old consumeInventoryItem method.
     * @param inventory The player's inventory
     * @param item The item to consume.
     */
    public static void consumePlayerInventoryItem(InventoryPlayer inventory, Item item) {
        ItemStack stack = null;
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack inSlot = inventory.getStackInSlot(slot);
            if (inSlot == null) {
                continue;
            }
            if (inSlot.getItem() == item) {
                stack = inSlot;
            }
        }
        if (stack == null) {
            return;
        }
        stack.shrink(1);

        if (stack.isEmpty()) {
            inventory.deleteStack(stack);
        }
    }

    /**
     * @param item The ItemStack to check
     * @param oreDict The OreDictionary value to check
     * @return Whether the provided Item (metadata specific) is in the OreDictionary under the provided oreDict value.
     */
    public static boolean isItemOreDictedAs(ItemStack item, String oreDict) {
        for (ItemStack i : OreDictionary.getOres(oreDict)) {
            if (i.isItemEqual(item)) {
                return true;
            }
        }
        return false;
    }
}
