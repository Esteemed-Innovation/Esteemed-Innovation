package flaxbeard.steamcraft.init.misc;

public enum MiscellaneousCategories {
    DEFAULT_CRUCIBLE_LIQUIDS(new DefaultCrucibleLiquids());

    private MiscellaneousCategory category;

    MiscellaneousCategories(MiscellaneousCategory category) {
        this.category = category;
    }

    public MiscellaneousCategory getCategory() {
        return category;
    }
}
