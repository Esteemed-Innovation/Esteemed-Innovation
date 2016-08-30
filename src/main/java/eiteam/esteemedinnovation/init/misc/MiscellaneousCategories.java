package eiteam.esteemedinnovation.init.misc;

import eiteam.esteemedinnovation.init.misc.integration.CrossMod;
import eiteam.esteemedinnovation.init.misc.integration.crafttweaker.CraftTweakerIntegration;
import eiteam.esteemedinnovation.init.misc.integration.tinkers.TinkersIntegration;

public enum MiscellaneousCategories {
    DEFAULT_CRUCIBLE_LIQUIDS(new DefaultCrucibleLiquids()),
    LOOT_TABLES(new LootTablesCategory()),
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
            case LOOT_TABLES: {
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
