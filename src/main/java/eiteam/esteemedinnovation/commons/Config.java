package eiteam.esteemedinnovation.commons;

import eiteam.esteemedinnovation.api.APIConfig;
import eiteam.esteemedinnovation.boiler.BoilerModule;
import eiteam.esteemedinnovation.commons.init.ContentModuleHandler;
import eiteam.esteemedinnovation.steamsafety.SafetyModule;
import eiteam.esteemedinnovation.storage.StorageModule;
import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraftforge.common.config.Configuration;

public class Config {
    //Don't change this string. - @xbony2
    public static final String VERSION = "@VERSION@";
    public static final String CATEGORY_WORLD_GENERATION = "World Generation";
    public static final String CATEGORY_WEAPONS = "Weapons";
    public static final String CATEGORY_STEAM_SYSTEM = "Steam System";
    public static final String CATEGORY_MACHINES = "Machines";
    public static final String CATEGORY_CONSUMPTION = "Consumption";
    public static final String CATEGORY_BLOCKS = "Blocks";
    public static final String CATEGORY_ITEMS = "Items";
    public static final String CATEGORY_EXOSUIT = "Exosuit";
    public static final String CATEGORY_EXOSUIT_UPGRADES = "Exosuit Upgrades";
    public static final String CATEGORY_EXOSUIT_PLATES = "Exosuit Plates";
    public static final String CATEGORY_STEAM_TOOL_UPGRADES = "Steam Tool Upgrades";
    public static final String CATEGORY_OTHER = "Other";
    public static final String CATEGORY_INTEGRATION = "Integration";

    public static boolean disableParticles;
    public static boolean genPoorZincOre;

    public static boolean easterEggs;

    public static boolean enableNitorPoweredCrucible;
    public static boolean enableThaumcraftIntegration;
    public static boolean enableBotaniaIntegration;
    public static boolean enableEnchiridionIntegration;
    public static boolean enableTwilightForestIntegration;
    public static boolean enableBloodMagicIntegration;
    public static boolean enableEnderIOIntegration;
    public static boolean enableThermalFoundationIntegration;
    public static boolean enableIC2Integration;
    public static boolean enableNaturaIntegration;
    public static boolean enableTinkersConstruct;
    public static boolean enableRailcraftIntegration;
    public static boolean enableNEIIntegration;

    public static boolean hasAllCrucial;

    public static boolean singleButtonTrackpad;

    public static void load() {
        Configuration config = new Configuration(APIConfig.getConfigFile("EsteemedInnovation.cfg"));
        config.load();
        config.addCustomCategoryComment(CATEGORY_STEAM_SYSTEM, "Disabling any piece marked crucial disables pretty much the whole mod.");
        ContentModuleHandler.loadConfigs(config);

        // WORLD GEN
        genPoorZincOre = config.get(CATEGORY_INTEGRATION, "Railcraft Poor Zinc Ore", true).getBoolean();

        // STEAM SYSTEM
        //enableBloodBoiler = config.get(CATEGORY_STEAM_SYSTEM, "Enable Blood Boiler", true).getBoolean();


        // BLOCKS
        //enableGenocide = config.get(CATEGORY_BLOCKS, "Enable Aquatic Genocide Machine", true).getBoolean();

        // OTHER
        easterEggs = config.get(CATEGORY_OTHER, "Enable Easter Eggs", true).getBoolean();
        disableParticles = config.get(CATEGORY_OTHER, "Disable block break particles (May solve crashes with guns, thumper)", false).getBoolean();
        singleButtonTrackpad = config.get(CATEGORY_OTHER, "Check both mouse buttons for the journal ctrl-click feature for single-button trackpad users. If you have trouble getting the ctrl-click feature to work on a trackpad, enable this.", false).getBoolean();

        //INTEGRATION
        enableThaumcraftIntegration = config.get(CATEGORY_INTEGRATION, "Enable Thaumcraft", true).getBoolean();
        enableNitorPoweredCrucible = config.get(CATEGORY_INTEGRATION, "Allow the Thaumcraft Nitor to power the Crucible", true).getBoolean();
        enableBotaniaIntegration = config.get(CATEGORY_INTEGRATION, "Enable Botania", true).getBoolean();
        enableEnchiridionIntegration = config.get(CATEGORY_INTEGRATION, "Enable Enchiridion", true).getBoolean();
        enableTwilightForestIntegration = config.get(CATEGORY_INTEGRATION, "Enable Twilight Forest", true).getBoolean();
        enableBloodMagicIntegration = config.get(CATEGORY_INTEGRATION, "Enable Blood Magic", true).getBoolean();
        enableEnderIOIntegration = config.get(CATEGORY_INTEGRATION, "Enable Ender IO", true).getBoolean();
        enableThermalFoundationIntegration = config.get(CATEGORY_INTEGRATION, "Enable Thermal Foundation", true).getBoolean();
        enableIC2Integration = config.get(CATEGORY_INTEGRATION, "Enable IC2", true).getBoolean();
        enableNaturaIntegration = config.get(CATEGORY_INTEGRATION, "Enable Natura", true).getBoolean();
        enableTinkersConstruct = config.get(CATEGORY_INTEGRATION, "Enable Tinker's Construct", true).getBoolean();
        enableRailcraftIntegration = config.get(CATEGORY_INTEGRATION, "Enable Railcraft", true).getBoolean();
        enableNEIIntegration = config.get(CATEGORY_INTEGRATION, "Enable NEI", true).getBoolean();

        // TODO: Abstract this somehow
        hasAllCrucial = BoilerModule.enableBoiler && SafetyModule.enableGauge && StorageModule.enableTank && TransportationModule.enablePipe;

        config.save();
    }
}
