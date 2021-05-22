package eiteam.esteemedinnovation.armor.exosuit.leather;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitModelCache;
import eiteam.esteemedinnovation.armor.exosuit.ModelExosuit;
import net.minecraft.inventory.EntityEquipmentSlot;

public class LeatherExosuitModelCache extends ExosuitModelCache {
    public static final LeatherExosuitModelCache INSTANCE = new LeatherExosuitModelCache();

    private LeatherExosuitModelCache() {
        super(ItemLeatherExosuitArmor.class);
    }

    @Override
    protected ModelExosuit[] generateNewArray() {
        ModelLeatherExosuit[] array = new ModelLeatherExosuit[4];
        for (EntityEquipmentSlot slot : ItemStackUtility.ARMOR_SLOTS) {
            array[slot.getIndex()] = new ModelLeatherExosuit(slot);
        }
        return array;
    }
}
