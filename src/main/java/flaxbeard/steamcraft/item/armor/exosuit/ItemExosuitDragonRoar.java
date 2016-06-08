package flaxbeard.steamcraft.item.armor.exosuit;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelDragonsRoar;

public class ItemExosuitDragonRoar extends ItemExosuitUpgrade {
    public ItemExosuitDragonRoar() {
        super(ExosuitSlot.HEAD_GOGGLES, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelDragonsRoar.class;
    }
}
