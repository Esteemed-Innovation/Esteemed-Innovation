package eiteam.esteemedinnovation.commons.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;

public class EnchantmentUtility {
    /**
     * Helper method to get the max fortune level for the entity, because apparently this method was removed from EnchantmentHelper.
     * @param entity The entity
     * @return The max enchantment level for the entity
     */
    public static int getFortuneModifier(EntityLivingBase entity) {
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FORTUNE, entity);
    }

    /**
     * @param stack The item to check.
     * @return Whether the provided item has any enchantments.
     */
    public static boolean hasEnchantments(ItemStack stack) {
        return !EnchantmentHelper.getEnchantments(stack).isEmpty();
    }
}
