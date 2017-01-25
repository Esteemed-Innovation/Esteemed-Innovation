package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ExosuitUtility {
    /**
     * @param entityLiving The entity being checked against.
     * @return The number of ExosuitArmor pieces the Entity is wearing in their equipment slots.
     */
    public static int getExoArmor(EntityLivingBase entityLiving) {
        int num = 0;

        for (EntityEquipmentSlot armor : ItemStackUtility.ARMOR_SLOTS) {
            ItemStack stack = entityLiving.getItemStackFromSlot(armor);
            if (stack != null && stack.getItem() instanceof ExosuitArmor) {
                num++;
            }
        }
        return num;
    }
}
