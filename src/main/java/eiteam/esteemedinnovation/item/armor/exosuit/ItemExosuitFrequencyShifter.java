package eiteam.esteemedinnovation.item.armor.exosuit;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelFrequencyShifter;

public class ItemExosuitFrequencyShifter extends ItemExosuitUpgrade {
    public ItemExosuitFrequencyShifter() {
        super(ExosuitSlot.HEAD_HELM, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelFrequencyShifter.class;
    }
}
