package eiteam.esteemedinnovation.armor.exosuit.plates;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;

public class ExosuitPlateBrass extends ExosuitPlate {
    public ExosuitPlateBrass() {
        super("Brass", null, "brass", "brass", EsteemedInnovation.MOD_ID + ".plate.brass");
    }

    @Override
    public int getDamageReductionAmount(EntityEquipmentSlot slot, DamageSource source) {
        if (source.isFireDamage()) {
            return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot);
        }
        return super.getDamageReductionAmount(slot, source);
    }
}
