package eiteam.esteemedinnovation.item.armor.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelJetpack;

public class ItemExosuitJetpack extends ItemExosuitUpgrade {

    public ItemExosuitJetpack() {
        super(ExosuitSlot.BODY_FRONT, "", "", 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelJetpack.class;
    }
}
