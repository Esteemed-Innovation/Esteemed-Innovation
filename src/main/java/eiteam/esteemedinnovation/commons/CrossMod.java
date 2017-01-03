package eiteam.esteemedinnovation.commons;

import net.minecraftforge.fml.common.Loader;

public class CrossMod {
    public static final boolean BAUBLES = Loader.isModLoaded("Baubles") && Config.enableBaublesIntegration;
    public static final boolean ENCHIRIDION = Loader.isModLoaded("Enchridion") && Config.enableEnchiridionIntegration;
    public static final boolean TINKERS_CONSTRUCT = Loader.isModLoaded("TConstruct") && Config.enableTinkersConstruct;
    public static final boolean CRAFTTWEAKER = Loader.isModLoaded("MineTweaker3");
    public static final boolean IC2 = Loader.isModLoaded("IC2") && Config.enableIC2Integration;
}
