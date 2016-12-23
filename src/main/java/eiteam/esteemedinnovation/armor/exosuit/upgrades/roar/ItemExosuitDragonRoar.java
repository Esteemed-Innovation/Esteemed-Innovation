package eiteam.esteemedinnovation.armor.exosuit.upgrades.roar;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.ItemExosuitUpgrade;

public class ItemExosuitDragonRoar extends ItemExosuitUpgrade {
    public ItemExosuitDragonRoar() {
        super(ExosuitSlot.HEAD_GOGGLES, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelDragonsRoar.class;
    }
}
