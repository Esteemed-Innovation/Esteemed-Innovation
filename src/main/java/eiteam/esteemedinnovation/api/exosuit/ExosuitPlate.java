package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class ExosuitPlate {
    private String identifier;
    private String invMod;
    private String armorMod;
    private Object plate;
    private String effect;
    private DamageSource[] bonusSources;

    public ExosuitPlate(String id, Object item, String invLocMod, String armorLocMod, String effectLoc, DamageSource... sources) {
        identifier = id;
        invMod = invLocMod;
        armorMod = armorLocMod;
        plate = item;
        bonusSources = sources;
        effect = effectLoc;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Object getItem() {
        return plate;
    }

    public ResourceLocation getIcon(ItemExosuitArmor item) {
        return new ResourceLocation(item.getString() + invMod);
    }

    public String getArmorLocation(ItemExosuitArmor item, EntityEquipmentSlot slot) {
        if (slot != EntityEquipmentSlot.LEGS) {
            return EsteemedInnovation.MOD_ID + ":textures/models/armor/exoPlate" + armorMod + "_1.png";
        } else {
            return EsteemedInnovation.MOD_ID + ":textures/models/armor/exoPlate" + armorMod + "_2.png";
        }
    }

    public int getDamageReductionAmount(EntityEquipmentSlot slot, DamageSource source) {
        // TODO: Remove implementation from the API.
        if (getIdentifier().equals("Copper")) {
            if (source.isExplosion()) {
                return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot);
            }
        }
        if (getIdentifier().equals("Iron")) {
            if (source.isProjectile()) {
                return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot);
            }
        }
        if (getIdentifier().equals("Gilded Iron")) {
            if (source.isProjectile()) {
                return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot) - 1;
            }
        }
        if (getIdentifier().equals("Brass")) {
            if (source.isFireDamage()) {
                return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot);
            }
        }
        return ItemArmor.ArmorMaterial.IRON.getDamageReductionAmount(slot);
    }

    public String effect() {
        return I18n.format(effect);
    }
}
