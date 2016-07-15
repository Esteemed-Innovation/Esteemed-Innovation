package flaxbeard.steamcraft.init.misc;

import flaxbeard.steamcraft.init.misc.integration.CrossMod;
import flaxbeard.steamcraft.init.misc.integration.crafttweaker.CraftTweakerIntegration;
import flaxbeard.steamcraft.init.misc.integration.tinkers.TinkersIntegration;

public enum MiscellaneousCategories {
    DEFAULT_CRUCIBLE_LIQUIDS(new DefaultCrucibleLiquids()),
    TINKERS_INTEGRATION(new TinkersIntegration()),
    CRAFTTWEAKER_INTEGRATION(new CraftTweakerIntegration());

    private MiscellaneousCategory category;

    MiscellaneousCategories(MiscellaneousCategory category) {
        this.category = category;
    }

    public MiscellaneousCategory getCategory() {
        return category;
    }

    public boolean isEnabled() {
        switch (this) {
            case DEFAULT_CRUCIBLE_LIQUIDS: {
                return true;
            }
            case TINKERS_INTEGRATION: {
                return CrossMod.CRAFTTWEAKER;
            }
            case CRAFTTWEAKER_INTEGRATION: {
                return CrossMod.TINKERS_CONSTRUCT;
            }
        }
        return false;
    }
}
