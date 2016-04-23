package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelFrequencyShifter;

public class ItemExosuitFrequencyShifter extends ItemExosuitUpgrade {
    public ItemExosuitFrequencyShifter() {
        super(ExosuitSlot.headHelm, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelFrequencyShifter.class;
    }
}
