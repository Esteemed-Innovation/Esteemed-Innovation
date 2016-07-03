package flaxbeard.steamcraft.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemStackUtility {
    /**
     * A performance-friendly cache of all the equipment slots.
     */
    public static final EntityEquipmentSlot[] EQUIPMENT_SLOTS = EntityEquipmentSlot.values();

    /**
     * "Mostly" equal; returns true if the item and meta are the same (don't care about stacksize).
     * 
     * @param stack1
     * @param stack2
     * @return
     */
    public static boolean areItemStacksMostlyEqual(ItemStack stack1, ItemStack stack2){
        return stack1.getItem().equals(stack2.getItem()) && stack1.getItemDamage() == stack2.getItemDamage();
    }

    /**
     * Gets the EntityEquipmentSlot by enum index.
     * @param index The index
     * @return The slot or null.
     */
    @Nullable
    public static EntityEquipmentSlot getSlotFromIndex(int index) {
        for (EntityEquipmentSlot slot : EQUIPMENT_SLOTS) {
            if (slot.getSlotIndex() == index) {
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
}
