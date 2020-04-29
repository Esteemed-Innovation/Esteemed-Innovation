package eiteam.esteemedinnovation.armor.exosuit;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * It is likely that you'll want to inherit ModelBiped with this as well.
 */
public interface ModelExosuit {
    /**
     * Called each client tick when the player is wearing the armor.
     * @param entityLivingBase The player wearing the piece.
     * @param itemStack The piece being worn.
     * @param slot The slot that this piece is in.
     */
    void updateModel(EntityLivingBase entityLivingBase, ItemStack itemStack, EntityEquipmentSlot slot);
}
