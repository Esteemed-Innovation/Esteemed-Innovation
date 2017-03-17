package eiteam.esteemedinnovation.api.exosuit;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eiteam.esteemedinnovation.api.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class ExosuitPlate implements ExosuitEventHandler {
    private String identifier;
    private String invMod;
    private String armorMod;
    private Object plate;
    private String effect;

    public ExosuitPlate(String id, Object item, String invLocMod, String armorLocMod, String effectLoc) {
        identifier = id;
        invMod = invLocMod;
        armorMod = armorLocMod;
        plate = item;
        effect = effectLoc;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Object getItem() {
        return plate;
    }

    public void setItem(Object item) {
        plate = item;
    }

    public ResourceLocation getIcon(ExosuitArmor item) {
        return new ResourceLocation(item.getItemIconResource() + "_" + invMod);
    }

    public String getArmorLocation(ExosuitArmor item, EntityEquipmentSlot slot) {
        // TODO: Abstract out of API
        if (slot != EntityEquipmentSlot.LEGS) {
            return Constants.EI_MODID + ":textures/models/armor/exo_plate_" + armorMod + "_1.png";
        } else {
            return Constants.EI_MODID + ":textures/models/armor/exo_plate_" + armorMod + "_2.png";
        }
    }

    public String getArmorMod() {
        return armorMod;
    }

    /**
     * @param slot The armor slot that this plate is installed in
     * @param source The damage source
     * @return The damage reduction amount for the slot and the source. Default implementation returns the IRON
     *         damage reduction amount.
     */
    public int getDamageReductionAmount(EntityEquipmentSlot slot, DamageSource source) {
        return ItemArmor.ArmorMaterial.IRON.getDamageReductionAmount(slot);
    }

    public String effect() {
        return I18n.format(effect);
    }

    /**
     * @see ExosuitUpgrade#getAttributeModifiersForExosuit(EntityEquipmentSlot, ItemStack)
     */
    public Multimap<String, AttributeModifier> getAttributeModifiersForExosuit(EntityEquipmentSlot armorSlot, ItemStack armorPieceStack) {
        return HashMultimap.create();
    }
}
