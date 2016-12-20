package eiteam.esteemedinnovation.armor;

import eiteam.esteemedinnovation.api.util.UtilMisc;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemGenericArmor extends ItemArmor {
    protected String name;
    private Object repairMaterial;

    public ItemGenericArmor(ArmorMaterial armorMat, int renderIndex, EntityEquipmentSlot armorType, Object repair, String n) {
        super(armorMat, renderIndex, armorType);
        repairMaterial = repair;
        name = n;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (repairMaterial instanceof ItemStack) {
            return repair.isItemEqual((ItemStack) repairMaterial) || super.getIsRepairable(toRepair, repair);
        }
        if (repairMaterial instanceof String) {
            return UtilMisc.doesMatch(repair, (String) repairMaterial) || super.getIsRepairable(toRepair, repair);
        }
        return super.getIsRepairable(toRepair, repair);
    }
}
