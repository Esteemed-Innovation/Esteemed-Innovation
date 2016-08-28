package eiteam.esteemedinnovation.item.armor.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelAnchors;

public class ItemExosuitAnchorHeels extends ItemExosuitUpgrade {
    public ItemExosuitAnchorHeels() {
        super(ExosuitSlot.BOOTS_FEET, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelAnchors.class;
    }
}
