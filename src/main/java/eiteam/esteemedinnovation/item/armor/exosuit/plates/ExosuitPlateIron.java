package eiteam.esteemedinnovation.item.armor.exosuit.plates;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;

public class ExosuitPlateIron extends ExosuitPlate {
    public ExosuitPlateIron() {
        super("Iron", null, "Iron", "Iron", EsteemedInnovation.MOD_ID + ".plate.iron");
    }

    @Override
    public int getDamageReductionAmount(EntityEquipmentSlot slot, DamageSource source) {
        if (source.isProjectile()) {
            return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot);
        }
        return super.getDamageReductionAmount(slot, source);
    }
}
