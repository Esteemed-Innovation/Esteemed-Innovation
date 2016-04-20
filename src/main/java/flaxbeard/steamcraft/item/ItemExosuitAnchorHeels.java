package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelAnchors;

public class ItemExosuitAnchorHeels extends ItemExosuitUpgrade {
    public ItemExosuitAnchorHeels() {
        super(ExosuitSlot.bootsFeet, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelAnchors.class;
    }
}
