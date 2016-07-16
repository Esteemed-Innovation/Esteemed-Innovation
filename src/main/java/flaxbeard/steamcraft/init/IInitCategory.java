package flaxbeard.steamcraft.init;

public interface IInitCategory {
    /**
     * Initialize all the OreDict entries in the item category. This is called during preInit.
     * It might be a good idea to call values() on your enum to ensure that they are initialized at this time.
     * Unless of course they don't initialize them at PreInit, or you already reference values in the enum.
     *
     * TODO: Figure out a way to do this automatically instead of relying on the implementer to do it.
     * The problem is that Enums are really complicated to use as return values from methods.
     * Alternatively, we could not have normal classes for categories at all, and just use enums. If the enums extended
     * an abstract class, we could further unify these category things, and handle some basic registration without
     * redundant boiler code.
     */
    void oreDict();

    /**
     * Initialize all the recipes in the item category. This is called during init.
     */
    void recipes();
}
