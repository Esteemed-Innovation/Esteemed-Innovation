package eiteam.esteemedinnovation.item.armor.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelDragonsRoar;

public class ItemExosuitDragonRoar extends ItemExosuitUpgrade {
    public ItemExosuitDragonRoar() {
        super(ExosuitSlot.HEAD_GOGGLES, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelDragonsRoar.class;
    }
}
