package eiteam.esteemedinnovation.commons;

import eiteam.esteemedinnovation.api.APIConfig;
import eiteam.esteemedinnovation.boiler.BoilerModule;
import eiteam.esteemedinnovation.commons.init.ContentModuleHandler;
import eiteam.esteemedinnovation.steamsafety.SafetyModule;
import eiteam.esteemedinnovation.storage.StorageModule;
import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.List;

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

    public static final int STEAM_TOOL_CONSUMPTION_DEFAULT = 800;
    private static final int BATTLE_DRILL_CONSUMPTION_DEFAULT = 20;

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

    public static int mortarRadius;
    public static int duplicateLogs;
    public static int steamToolConsumptionDrill;
    public static int steamToolConsumptionAxe;
    public static int steamToolConsumptionShovel;


    public static int villagerId;

    public static boolean enableWrench;

    public static boolean enableSteamTools;
    public static boolean enableSurvivalist;

    // steam tool upgrades
    // core
    public static boolean enableTheVoid;
    public static boolean enableAutosmelting;
    public static boolean enableOverclocker;
    public static boolean enableFortune;

    // drill
    public static boolean enableBigDrill;
    public static boolean enableBattleDrill;
    public static boolean enableStoneGrinder;
    public static boolean enablePreciseCuttingHead;
    public static boolean enableInternalProcessingUnit;
    public static boolean enableThermalDrill;
    public static boolean enableChargePlacer;
    public static int battleDrillConsumption;

    // saw
    public static boolean enableLeafBlower;
    public static boolean enableTreeFeller;
    public static boolean enableChainsaw;
    public static boolean enableForestFire;

    // shovel
    public static boolean enableCultivator;
    public static boolean enableRotaryBlades;
    public static boolean enableSifter;
    public static boolean enableBackhoe;
    public static int backhoeRange;

    public static List<String> blacklistedStoneGrinderNuggets;

    public static boolean hasAllCrucial;

    public static boolean singleButtonTrackpad;
    public static boolean removeVanillaMetalToolRecipes;

    public static void load() {
        Configuration config = new Configuration(APIConfig.getConfigFile("EsteemedInnovation.cfg"));
        config.load();
        ContentModuleHandler.loadConfigs(config);

        // WORLD GEN
        villagerId = config.get(CATEGORY_WORLD_GENERATION, "Villager ID", 694).getInt();
        genPoorZincOre = config.get(CATEGORY_INTEGRATION, "Railcraft Poor Zinc Ore", true).getBoolean();

        // MACHINES
        mortarRadius = config.get(CATEGORY_MACHINES, "Item Mortar accuracy (radius in blocks)", 2).getInt();
        duplicateLogs = config.get(CATEGORY_MACHINES, "Chance of duplicate drops from Buzzsaw (1 in X)", 6).getInt();

        // STEAM SYSTEM
        config.addCustomCategoryComment(CATEGORY_STEAM_SYSTEM, "Disabling any piece marked crucial disables pretty much the whole mod.");
        //enableBloodBoiler = config.get(CATEGORY_STEAM_SYSTEM, "Enable Blood Boiler", true).getBoolean();


        // BLOCKS
        //enableGenocide = config.get(CATEGORY_BLOCKS, "Enable Aquatic Genocide Machine", true).getBoolean();

        // ITEMS
        enableSteamTools = config.get(CATEGORY_ITEMS, "Enable steam tools", true).getBoolean();
        enableSurvivalist = config.get(CATEGORY_ITEMS, "Enable Survivalist's Toolkit", true).getBoolean();
        enableWrench = config.get(CATEGORY_ITEMS, "Enable Pipe Wrench", true).getBoolean();
        steamToolConsumptionAxe = config.get(CATEGORY_ITEMS, "The consumption rate of the Steam Axe", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();
        steamToolConsumptionDrill = config.get(CATEGORY_ITEMS, "The consumption rate of the Steam Drill", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();
        steamToolConsumptionShovel = config.get(CATEGORY_ITEMS, "The consumption rate of the Steam Shovel", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();
        removeVanillaMetalToolRecipes = config.get(CATEGORY_ITEMS, "Remove Vanilla-style tool recipes for castable tools", true).getBoolean();

        // STEAM TOOL UPGRADES
        enableBigDrill = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Hammer Head upgrade", true).getBoolean();
        enableLeafBlower = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Axe's Leaf Blower upgrade", true).getBoolean();
        enableCultivator = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Shovel's Cultivator upgrade", true).getBoolean();
        enableRotaryBlades = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Shovel's Rotary Blades upgrade", true).getBoolean();
        enableBattleDrill = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable BattleDrill", true).getBoolean();
        enableSifter = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Shovel's Sifter upgrade", true).getBoolean();
        enableStoneGrinder = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Stone Grinder upgrade", true).getBoolean();
        enableBackhoe = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Shovel's Backhoe upgrade", true).getBoolean();
        enableTheVoid = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Tool core upgrade the Void", true).getBoolean();
        enableAutosmelting = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Tool core upgrade autosmelting", true).getBoolean();
        enableOverclocker = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Tool core upgrade overclocker", true).getBoolean();
        enablePreciseCuttingHead = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Precise Cutting Head", true).getBoolean();
        enableInternalProcessingUnit = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Internal Processing Unit", true).getBoolean();
        enableTreeFeller = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Axe's Tree Felling upgrade", true).getBoolean();
        enableChainsaw = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Axe's Chainsaw upgrade", true).getBoolean();
        enableFortune = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's fortune upgrade", true).getBoolean();
        enableForestFire = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Axe's Forest Fire upgrade", true).getBoolean();
        enableThermalDrill = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Thermal Drill upgrade", true).getBoolean();
        enableChargePlacer = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Enable Steam Drill's Charge Placer upgrade", true).getBoolean();

        backhoeRange = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "The range (in each direction) that the Backhoe upgrade effects", 16).getInt();
        battleDrillConsumption = config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Steam consumption for the " +
          "BattleDrill. This is not the actual amount of steam, but the relative item damage.",
          BATTLE_DRILL_CONSUMPTION_DEFAULT).getInt();
        blacklistedStoneGrinderNuggets = Arrays.asList(config.get(CATEGORY_STEAM_TOOL_UPGRADES, "Nuggets that the Stone Grinder cannot produce. These are OreDict entries", new String[] {}).getStringList());

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
