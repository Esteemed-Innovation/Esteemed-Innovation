package eiteam.esteemedinnovation.init.misc.integration;

import net.minecraftforge.fml.common.Loader;
import eiteam.esteemedinnovation.Config;

public class CrossMod {
    public static final boolean BAUBLES = Loader.isModLoaded("Baubles") && Config.enableBaublesIntegration;
    public static final boolean ENCHIRIDION = Loader.isModLoaded("Enchridion") && Config.enableEnchiridionIntegration;
    public static final boolean TINKERS_CONSTRUCT = Loader.isModLoaded("TConstruct") && Config.enableTinkersConstruct;
    public static final boolean CRAFTTWEAKER = Loader.isModLoaded("MineTweaker3");
}
