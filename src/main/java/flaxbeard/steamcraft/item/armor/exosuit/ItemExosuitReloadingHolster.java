package flaxbeard.steamcraft.item.armor.exosuit;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelReloadingHolster;

public class ItemExosuitReloadingHolster extends ItemExosuitUpgrade {
    public ItemExosuitReloadingHolster() {
        super(ExosuitSlot.LEGS_HIPS, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelReloadingHolster.class;
    }
}
