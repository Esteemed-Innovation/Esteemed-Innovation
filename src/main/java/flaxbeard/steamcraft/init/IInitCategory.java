package flaxbeard.steamcraft.init;

public interface IInitCategory {
    /**
     * Initialize all the OreDict entries in the item category.
     */
    void oreDict();

    /**
     * Initialize all the recipes in the item category.
     */
    void recipes();
}
