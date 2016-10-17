package eiteam.esteemedinnovation.item.armor.exosuit.plates;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;

public class ExosuitPlateGildedIron extends ExosuitPlate {
    public ExosuitPlateGildedIron() {
        super("Gilded Iron", null, "GildedIron", "GildedIron", EsteemedInnovation.MOD_ID + ".plate.gilded");
    }

    @Override
    public int getDamageReductionAmount(EntityEquipmentSlot slot, DamageSource source) {
        if (source.isProjectile()) {
            return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot) - 1;
        }
        return super.getDamageReductionAmount(slot, source);
    }
}
