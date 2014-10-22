package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelJetpack;

public class ItemExosuitJetpack extends ItemExosuitUpgrade {

    public ItemExosuitJetpack() {
        super(ExosuitSlot.bodyFront, "", "", 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelJetpack.class;
    }
}
