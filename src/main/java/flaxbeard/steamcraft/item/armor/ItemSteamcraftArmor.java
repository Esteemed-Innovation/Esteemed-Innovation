package flaxbeard.steamcraft.item.armor;

import flaxbeard.steamcraft.api.util.UtilMisc;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemSteamcraftArmor extends ItemArmor {
    protected String name;
    private Object repairMaterial;

    public ItemSteamcraftArmor(ArmorMaterial armorMat, int renderIndex, EntityEquipmentSlot armorType, Object repair, String n) {
        super(armorMat, renderIndex, armorType);
        this.repairMaterial = repair;
        this.name = n;
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
