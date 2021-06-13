package eiteam.esteemedinnovation.api.exosuit;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * The interface which allows an item to be upgraded into an Exosuit armor piece.
 * <p>
 * You must inherit Item, or provide a new implementation for isInstalled, as the default implementation
 * checks if we are an instance of Item.
 */
public interface ExosuitUpgrade extends ExosuitEventHandler {
    /**
     * The priority of the upgrade's rendering
     * <p>
     * Use 1 if it should overwrite the exosuit's rendering
     * <p>
     * Use 0 if it should not overwrite the exosuit's rendering
     */
    int renderPriority();

    /**
     * The slot that the upgrade can be used on
     * @see ExosuitSlot for the list of slots.
     */
    ExosuitSlot getSlot();

    /**
     * The texture overlay *for the armor model*. This is not for the item texture/model.
     * @return ResourceLocation or null if it does not add a texture overlay for the armor.
     */
    ResourceLocation getOverlay();

    Class<? extends ModelExosuitUpgrade> getModel();

    void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, @Nonnull ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade);

    void writeInfo(List<String> list);

    /**
     * Called to add attribute modifiers from the upgrade (or plate) to the exosuit armor piece.
     * @param armorSlot The slot that the armor is in
     * @param armorPieceStack The ItemStack holding the armor piece
     * @return A Multimap that will have all of its entries added to the armor's attribute modifiers. Default implementation
     *         returns an empty multimap.
     */
    default Multimap<String, AttributeModifier> getAttributeModifiersForExosuit(EntityEquipmentSlot armorSlot, @Nonnull ItemStack armorPieceStack) {
        return HashMultimap.create();
    }

    /**
     * @param entity The entity to check
     * @return Whether this upgrade is installed in its according ExosuitArmor piece worn by the provided entity.
     */
    default boolean isInstalled(EntityLivingBase entity) {
        ItemStack armor = entity.getItemStackFromSlot(getSlot().getArmorPiece());
        return this instanceof Item && armor.getItem() instanceof ExosuitArmor &&
          ((ExosuitArmor) armor.getItem()).hasUpgrade(armor, (Item) this);
    }
}
