package flaxbeard.steamcraft.item.armor.exosuit;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelJetpack;

public class ItemExosuitJetpack extends ItemExosuitUpgrade {

    public ItemExosuitJetpack() {
        super(ExosuitSlot.BODY_FRONT, "", "", 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelJetpack.class;
    }
}
