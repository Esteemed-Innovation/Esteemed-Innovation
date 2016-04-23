package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelDragonsRoar;

public class ItemExosuitDragonRoar extends ItemExosuitUpgrade {
    public ItemExosuitDragonRoar() {
        super(ExosuitSlot.headHelm, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelDragonsRoar.class;
    }
}
