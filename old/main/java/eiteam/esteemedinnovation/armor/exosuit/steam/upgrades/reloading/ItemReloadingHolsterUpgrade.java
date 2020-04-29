package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.reloading;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.ItemSteamExosuitUpgrade;

public class ItemReloadingHolsterUpgrade extends ItemSteamExosuitUpgrade {
    public ItemReloadingHolsterUpgrade() {
        super(ExosuitSlot.LEGS_HIPS, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelReloadingHolster.class;
    }
}
