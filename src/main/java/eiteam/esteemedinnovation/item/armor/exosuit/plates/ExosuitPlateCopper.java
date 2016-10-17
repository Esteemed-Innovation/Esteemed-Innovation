package eiteam.esteemedinnovation.item.armor.exosuit.plates;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;

public class ExosuitPlateCopper extends ExosuitPlate {
    public ExosuitPlateCopper() {
        super("Copper", null, "Copper", "Copper", EsteemedInnovation.MOD_ID + ".plate.copper");
    }

    @Override
    public int getDamageReductionAmount(EntityEquipmentSlot slot, DamageSource source) {
        if (source.isExplosion()) {
            return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot);
        }
        return super.getDamageReductionAmount(slot, source);
    }
}
