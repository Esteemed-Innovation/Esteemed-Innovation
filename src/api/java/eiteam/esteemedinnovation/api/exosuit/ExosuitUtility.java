package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ExosuitUtility {
    /**
     * Helper method for draining the steam from an exosuit without having to explicitly null check and type check
     * in your own code every time.
     * @param stack The ItemStack that contains the armor piece
     * @param amount The amount of steam to drain.
     */
    public static void drainSteam(ItemStack stack, int amount) {
        if (stack != null && stack.getItem() instanceof ExosuitArmor) {
            ((ExosuitArmor) stack.getItem()).drainSteam(stack, amount);
        }
    }

    /**
     * Helper method for checking if the provided entity has steam in their exosuit, without having to explicitly
     * find the proper armor piece, null check, type check, and check for the power every time.
     * @param entityLiving The entity to check for power. It will check every equipment slot.
     * @param i The amount of power required.
     * @return Whether the player has any ExosuitArmor piece in their inventory with the required amount of power.
     *         It will return as soon as it finds a single piece (so you could have 1 piece with no power, and 1 with `i`
     *         power, and it will return true as soon as it finds the one with `i` power).
     */
    public static boolean hasPower(EntityLivingBase entityLiving, int i) {
        boolean hasPower = false;
        for (EntityEquipmentSlot slot : ItemStackUtility.EQUIPMENT_SLOTS) {
            ItemStack equipment = entityLiving.getItemStackFromSlot(slot);
            if (equipment != null) {
                Item item = equipment.getItem();
                if (item instanceof ExosuitArmor) {
                    hasPower = ((ExosuitArmor) item).hasPower(equipment, i);
                }
            }
            if (hasPower) {
                break;
            }
        }
        return hasPower;
    }

    /**
     * @param entityLiving The entity being checked against.
     * @return The number of ExosuitArmor pieces the Entity is wearing in their equipment slots.
     */
    public static int getExoArmor(EntityLivingBase entityLiving) {
        int num = 0;

        for (EntityEquipmentSlot armor : ItemStackUtility.EQUIPMENT_SLOTS) {
            ItemStack stack = entityLiving.getItemStackFromSlot(armor);
            if (stack != null && stack.getItem() instanceof ExosuitArmor) {
                num++;
            }
        }
        return num;
    }
}
