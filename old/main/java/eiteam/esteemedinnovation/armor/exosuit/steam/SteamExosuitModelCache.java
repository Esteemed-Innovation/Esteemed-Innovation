package eiteam.esteemedinnovation.armor.exosuit.steam;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.armor.exosuit.ExosuitModelCache;
import eiteam.esteemedinnovation.armor.exosuit.ModelExosuit;
import net.minecraft.inventory.EntityEquipmentSlot;

public class SteamExosuitModelCache extends ExosuitModelCache {
    public static final SteamExosuitModelCache INSTANCE = new SteamExosuitModelCache();

    private SteamExosuitModelCache() {
        super(ItemSteamExosuitArmor.class);
    }

    @Override
    protected ModelExosuit[] generateNewArray() {
        ModelSteamExosuit[] array = new ModelSteamExosuit[4];
        for (EntityEquipmentSlot slot : ItemStackUtility.ARMOR_SLOTS) {
            array[slot.getIndex()] = new ModelSteamExosuit(slot);
        }
        return array;
    }
}
