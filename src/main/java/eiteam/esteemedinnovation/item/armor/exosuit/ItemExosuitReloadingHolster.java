package eiteam.esteemedinnovation.item.armor.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelReloadingHolster;

public class ItemExosuitReloadingHolster extends ItemExosuitUpgrade {
    public ItemExosuitReloadingHolster() {
        super(ExosuitSlot.LEGS_HIPS, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelReloadingHolster.class;
    }
}
