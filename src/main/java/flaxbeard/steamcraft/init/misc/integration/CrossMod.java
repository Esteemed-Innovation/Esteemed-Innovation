package flaxbeard.steamcraft.init.misc.integration;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.init.misc.integration.crafttweaker.CraftTweakerIntegration;
import flaxbeard.steamcraft.init.misc.integration.tinkers.TinkersIntegration;

public class CrossMod {
    public static final boolean BAUBLES = Loader.isModLoaded("Baubles") && Config.enableBaublesIntegration;
    public static final boolean ENCHIRIDION = Loader.isModLoaded("Enchridion") && Config.enableEnchiridionIntegration;
    public static final boolean TINKERS_CONSTRUCT = Loader.isModLoaded("TConstruct") && Config.enableTinkersConstruct;
    public static final boolean CRAFTTWEAKER = Loader.isModLoaded("MineTweaker3");
}
