package flaxbeard.steamcraft.misc;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;

public class EnchantmentUtility {
    /**
     * Helper method to get the max fortune level for the entity, because apparently this method was removed from EnchantmentHelper.
     * @param entity The entity
     * @return The max enchantment level for the entity
     */
    public static int getFortuneModifier(EntityLivingBase entity) {
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FORTUNE, entity);
    }
}
