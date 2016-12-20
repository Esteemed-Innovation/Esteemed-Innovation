package eiteam.esteemedinnovation.armor.exosuit.upgrades.reloading;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.ItemExosuitUpgrade;

public class ItemExosuitReloadingHolster extends ItemExosuitUpgrade {
    public ItemExosuitReloadingHolster() {
        super(ExosuitSlot.LEGS_HIPS, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelReloadingHolster.class;
    }
}
