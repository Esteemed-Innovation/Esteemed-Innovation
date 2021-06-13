package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;

public class ExosuitUtility {
    /**
     * @param entityLiving The entity being checked against.
     * @return The number of ExosuitArmor pieces the Entity is wearing in their equipment slots.
     */
    public static int getExoArmor(EntityLivingBase entityLiving) {
        return getExoArmorMatchesPredicate(entityLiving, (slot, stack) -> true);
    }

    /**
     * @param entityLiving The entity being checked against.
     * @param predicate A predicate that passes the slot and its according item stack.
     * @return The number of ExosuitArmor pieces the Entity is wearing that match the provided predicate.
     */
    public static int getExoArmorMatchesPredicate(EntityLivingBase entityLiving, BiPredicate<EntityEquipmentSlot, ItemStack> predicate) {
        int num = 0;

        for (EntityEquipmentSlot armor : ItemStackUtility.ARMOR_SLOTS) {
            ItemStack stack = entityLiving.getItemStackFromSlot(armor);
            if (stack.getItem() instanceof ExosuitArmor && predicate.test(armor, stack)) {
                num++;
            }
        }
        return num;
    }
}
