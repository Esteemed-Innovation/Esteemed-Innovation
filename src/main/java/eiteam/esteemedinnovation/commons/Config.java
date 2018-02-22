package eiteam.esteemedinnovation.commons;

import eiteam.esteemedinnovation.api.APIConfig;
import eiteam.esteemedinnovation.boiler.BoilerModule;
import eiteam.esteemedinnovation.commons.init.ContentModuleHandler;
import eiteam.esteemedinnovation.steamsafety.SafetyModule;
import eiteam.esteemedinnovation.storage.StorageModule;
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

    public static final int JUMP_BOOST_CONSUMPTION_DEFAULT = 10;
    public static final int JETPACK_CONSUMPTION_DEFAULT = 10;
    public static final int JUMP_BOOST_CONSUMPTION_SHIFT_BOOST_DEFAULT = 10;
    public static final int POWER_FIST_CONSUMPTION_DEFAULT = 5;
    public static final int THRUSTER_CONSUMPTION_DEFAULT = 5;
    public static final int RUN_ASSIST_CONSUMPTION_DEFAULT = 5;
    public static final int EXO_CONSUMPTION_DEFAULT = 5;
    public static final int STEAM_TOOL_CONSUMPTION_DEFAULT = 800;
    public static final int REINFORCED_TANK_CAPACITY_DEFAULT = 72000;
    public static final int UBER_REINFORCED_TANK_CAPACITY_DEFAULT = 144000;
    public static final int ZINC_PLATE_CONSUMPTION_DEFAULT = 30;
    public static final int REBREATHER_CONSUMPTION_DEFAULT = 5;
    public static final int PYROPHOBIC_CONSUMPTION_DEFAULT = 5;
    public static final int HYDROPHOBIC_CONSUMPTION_DEFAULT = 10;
    public static final int PISTON_PUSH_CONSUMPTION_DEFAULT = 5;
    public static final int RELOADING_CONSUMPTION_DEFAULT = 15;
    public static final int DRAGON_ROAR_CONSUMPTION_DEFAULT = 20000;
    public static final int BATTLE_DRILL_CONSUMPTION_DEFAULT = 20;

    public static final float extendedRange = 2.0F; //Range extension in blocks
    public static final float fallAssistDivisor = 2;

    public static boolean passiveDrain;
    public static boolean disableParticles;
    public static boolean genPoorZincOre;
    public static int workshopLimit;
    public static int workshopWeight;

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

    public static boolean enableAnchorAnvilRecipe;
    public static int mortarRadius;
    public static int duplicateLogs;
    public static int exoConsumption;
    public static int reinforcedTankCapacity;
    public static int uberReinforcedTankCapacity;
    public static int steamToolConsumptionDrill;
    public static int steamToolConsumptionAxe;
    public static int steamToolConsumptionShovel;
    public static int jumpBoostConsumption;
    public static int jetpackConsumption;
    public static int jumpBoostConsumptionShiftJump;
    public static int thrusterConsumption;
    public static int runAssistConsumption;
    public static int powerFistConsumption;
    public static int fanConsumption;
    public static int screwConsumption;
    public static int vacuumConsumption;
    public static int plonkerConsumption;
    public static int zincPlateConsumption;
    public static int rebreatherConsumption;
    public static int hydrophobicConsumption;
    public static int pyrophobicConsumption;
    public static int pistonPushConsumption;
    public static int reloadingConsumption;
    public static int dragonRoarConsumption;


    public static int villagerId;

    public static boolean enableRedstoneValvePipe;

    // blocks
    public static boolean enableFunnel;
    public static boolean enablePlonker;
    public static boolean enableFan;
    public static boolean enableMortar;
    public static boolean enablePipe;
    public static boolean enablePump;
    public static boolean enableVacuum;
    public static boolean enableValvePipe;
    public static boolean enableWrench;

    // items
    public static boolean enableAstrolabe;
    public static boolean enableTopHat;
    public static boolean enableEmeraldHat;
    public static boolean enableGoggles;
    public static boolean enableDoubleJump;
    public static boolean enableJumpAssist;
    public static boolean enableRunAssist;
    public static boolean enableStealthUpgrade;
    public static boolean enableExosuit;
    public static boolean enableSteamExosuit;
    public static boolean enableLeatherExosuit;
    public static boolean enableFallAssist;
    public static boolean enableJetpack;
    public static boolean enablePowerFist;
    public static boolean enableSteamTools;
    public static boolean enableSurvivalist;
    public static boolean enableThrusters;
    public static boolean enableCanningMachine;
    public static boolean enableExtendoFist;
    public static boolean enablePitonDeployer;
    public static boolean enableReinforcedTank;
    public static boolean enableUberReinforcedTank;
    public static boolean enableEnderShroud;
    public static boolean enableRebreather;
    public static boolean enableHydrophobic;
    public static boolean enablePyrophobic;
    public static boolean enableAnchorHeels;
    public static boolean enablePistonPush;
    public static boolean enableReloadingHolsters;
    public static boolean enableFrequencyShifter;
    public static boolean enableDragonRoar;

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

    //plates
    public static boolean enableCopperPlate;
    public static boolean enableIronPlate;
    public static boolean enableGoldPlate;
    public static boolean enableBrassPlate;
    public static boolean enableZincPlate;
    public static boolean enableLeadPlate;
    public static boolean enableThaumiumPlate;
    public static boolean enableElementiumPlate;
    public static boolean enableTerrasteelPlate;
    public static boolean enableYetiPlate;
    public static boolean enableFieryPlate;
    public static boolean enableSadistPlate;
    public static boolean enableVibrantPlate;
    public static boolean enableEnderiumPlate;
    public static boolean enableGildedIronPlate;

    public static boolean enableWings;
    public static boolean hasAllCrucial;

    public static boolean singleButtonTrackpad;
    public static boolean removeHopperRecipe;
    public static boolean removeVanillaMetalToolRecipes;

    public static void load() {
        Configuration config = new Configuration(APIConfig.getConfigFile("EsteemedInnovation.cfg"));
        config.load();
        ContentModuleHandler.loadConfigs(config);

        // WORLD GEN
        villagerId = config.get(CATEGORY_WORLD_GENERATION, "Villager ID", 694).getInt();
        genPoorZincOre = config.get(CATEGORY_INTEGRATION, "Railcraft Poor Zinc Ore", true).getBoolean();
        workshopLimit = config.get(CATEGORY_WORLD_GENERATION, "Maximum number of Workshops allowed to generate per village", 1).getInt();
        workshopWeight = config.get(CATEGORY_WORLD_GENERATION, "Workshop spawn weight", 7).getInt(7);

        // MACHINES
        mortarRadius = config.get(CATEGORY_MACHINES, "Item Mortar accuracy (radius in blocks)", 2).getInt();
        duplicateLogs = config.get(CATEGORY_MACHINES, "Chance of duplicate drops from Buzzsaw (1 in X)", 6).getInt();

        // STEAM SYSTEM
        config.addCustomCategoryComment(CATEGORY_STEAM_SYSTEM, "Disabling any piece marked crucial disables pretty much the whole mod.");
        enablePipe = config.get(CATEGORY_STEAM_SYSTEM, "Enable Steam Pipe (Crucial)", true).getBoolean();
        enableValvePipe = config.get(CATEGORY_STEAM_SYSTEM, "Enable Valve Pipe", true).getBoolean();
        //enableBloodBoiler = config.get(CATEGORY_STEAM_SYSTEM, "Enable Blood Boiler", true).getBoolean();


        // BLOCKS
        enableFan = config.get(CATEGORY_BLOCKS, "Enable Fan (disabling this disables Vacuum)", true).getBoolean();
        //enableGenocide = config.get(CATEGORY_BLOCKS, "Enable Aquatic Genocide Machine", true).getBoolean();
        enableMortar = config.get(CATEGORY_BLOCKS, "Enable Item Mortar", true).getBoolean();
        enablePump = config.get(CATEGORY_BLOCKS, "Enable Archimedes Screw", true).getBoolean();
        enableVacuum = config.get(CATEGORY_BLOCKS, "Enable Vacuum", true).getBoolean();
        enablePlonker = config.get(CATEGORY_BLOCKS, "Enable Plonker", true).getBoolean();
        enableFunnel = config.get(CATEGORY_BLOCKS, "Enable Funnel", true).getBoolean();

        // BLOCK CONSUMPTION RATES
        fanConsumption = config.get(CATEGORY_CONSUMPTION, "Fan consumption", 1).getInt();
        screwConsumption = config.get(CATEGORY_CONSUMPTION, "Archimedes Screw consumption", 100).getInt();
        vacuumConsumption = config.get(CATEGORY_CONSUMPTION, "Vacuum consumption", 3).getInt();
        plonkerConsumption = config.get(CATEGORY_CONSUMPTION, "Plonker consumption", 5).getInt();

        // EXOSUIT
        passiveDrain = config.get(CATEGORY_EXOSUIT, "Passively drain steam while in use", true).getBoolean();
        enableExosuit = config.get(CATEGORY_EXOSUIT, "Enable Exosuits in general (disabling disables both suits, all upgrades, and plates)", true).getBoolean();
        enableSteamExosuit = config.get(CATEGORY_EXOSUIT, "Enable Steam Exosuit (disabling disabled all its upgrades, as well)", true).getBoolean();
        enableLeatherExosuit = config.get(CATEGORY_EXOSUIT, "Enable Leather Exosuit (disabling only disables the suit)", true).getBoolean();
        exoConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit consumes", EXO_CONSUMPTION_DEFAULT).getInt();
        jumpBoostConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam jump boost consumes", JUMP_BOOST_CONSUMPTION_DEFAULT).getInt();
        jetpackConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Jetpack consumes", JETPACK_CONSUMPTION_DEFAULT).getInt();
        jumpBoostConsumptionShiftJump = config.get(CATEGORY_EXOSUIT, "The amount of steam the jump boost shift jump consumes", JUMP_BOOST_CONSUMPTION_SHIFT_BOOST_DEFAULT).getInt();
        thrusterConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit Thrusters consumes", THRUSTER_CONSUMPTION_DEFAULT).getInt();
        runAssistConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit Run Assist consumes", RUN_ASSIST_CONSUMPTION_DEFAULT).getInt();
        powerFistConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit Power Fist consumes", POWER_FIST_CONSUMPTION_DEFAULT).getInt();
        zincPlateConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Exosuit Zinc Plate consumes", ZINC_PLATE_CONSUMPTION_DEFAULT).getInt();
        rebreatherConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Rebreather consumes", REBREATHER_CONSUMPTION_DEFAULT).getInt();
        hydrophobicConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Hydrophobic Coatings consume", HYDROPHOBIC_CONSUMPTION_DEFAULT).getInt();
        pyrophobicConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Pyrophobic Coatings consume", PYROPHOBIC_CONSUMPTION_DEFAULT).getInt();
        enableAnchorAnvilRecipe = config.get(CATEGORY_EXOSUIT, "Use the leadless Anchor Heels recipe. This will always be true if there is no lead available.", false).getBoolean();
        pistonPushConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Piston Push consumes", PISTON_PUSH_CONSUMPTION_DEFAULT).getInt();
        reloadingConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Reloading Holsters consume", RELOADING_CONSUMPTION_DEFAULT).getInt();
        dragonRoarConsumption = config.get(CATEGORY_EXOSUIT, "The amount of steam the Dragon Roar consumes", DRAGON_ROAR_CONSUMPTION_DEFAULT).getInt();

        // EXOSUIT UPGRADES
        enableFallAssist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Fall Assist", true).getBoolean();
        enableJumpAssist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Leap Actuator", true).getBoolean();
        enableDoubleJump = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Pulse Nozzle", true).getBoolean();
        enableRunAssist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Modular Accelerator", true).getBoolean();
        enableStealthUpgrade = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Acoustic Dampener", true).getBoolean();
        enableJetpack = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Steam Jetpack", true).getBoolean();
        enableThrusters = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Thrusters", true).getBoolean();
        enableWings = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Wings", true).getBoolean();
        enablePowerFist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Power Fist", true).getBoolean();
        enableCanningMachine = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Canner", true).getBoolean();
        enableExtendoFist = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Extendo Fist", true).getBoolean();
        enablePitonDeployer = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Piton Deployer", true).getBoolean();
        enableReinforcedTank = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Reinforced Tank", true).getBoolean();
        enableUberReinforcedTank = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Heavily Reinforced Tank", true).getBoolean();
        enableEnderShroud = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Ender Shroud", true).getBoolean();
        enableRebreather = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Rebreather", true).getBoolean();
        enableHydrophobic = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Hydrophobic Coatings", true).getBoolean();
        enablePyrophobic = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Pyrophobic Coatings", true).getBoolean();
        enableAnchorHeels = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Anchor Heels", true).getBoolean();
        enablePistonPush = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Piston Push", true).getBoolean();
        enableReloadingHolsters = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Reloading Holsters", true).getBoolean();
        enableFrequencyShifter = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Frequency Shifter", true).getBoolean();
        enableDragonRoar = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable Dragon Roar", true).getBoolean();

        enableCopperPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable copper plate", true).getBoolean();
        enableZincPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable zinc plate", true).getBoolean();
        enableIronPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable iron plate", true).getBoolean();
        enableGoldPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable gold plate", true).getBoolean();
        enableBrassPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable brass plate", true).getBoolean();
        enableLeadPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable lead plate", true).getBoolean();
        enableThaumiumPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable thaumium plate", true).getBoolean();
        enableElementiumPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable elementium plate", true).getBoolean();
        enableTerrasteelPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable terrasteel plate", true).getBoolean();
        enableYetiPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable yeti plate", true).getBoolean();
        enableFieryPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable fiery plate", true).getBoolean();
        enableSadistPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable sadist plate", true).getBoolean();
        enableVibrantPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable vibrant plate", true).getBoolean();
        enableEnderiumPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable enderium plate", true).getBoolean();
        enableGildedIronPlate = config.get(CATEGORY_EXOSUIT_PLATES, "Enable gilded iron plate", true).getBoolean();

        reinforcedTankCapacity = config.get(CATEGORY_EXOSUIT_UPGRADES, "The amount of steam the reinforced tank can hold", REINFORCED_TANK_CAPACITY_DEFAULT).getInt();
        uberReinforcedTankCapacity = config.get(CATEGORY_EXOSUIT_UPGRADES, "The amount of steam the heavily reinforced tank can hold", UBER_REINFORCED_TANK_CAPACITY_DEFAULT).getInt();
        //enableDoubleJump = config.get(CATEGORY_EXOSUIT_UPGRADES, "Enable double jump", true).getBoolean();

        // ITEMS
        enableAstrolabe = config.get(CATEGORY_ITEMS, "Enable Astrolabe", true).getBoolean();
        enableSteamTools = config.get(CATEGORY_ITEMS, "Enable steam tools", true).getBoolean();
        enableSurvivalist = config.get(CATEGORY_ITEMS, "Enable Survivalist's Toolkit", true).getBoolean();
        enableWrench = config.get(CATEGORY_ITEMS, "Enable Pipe Wrench", true).getBoolean();
        enableTopHat = config.get(CATEGORY_ITEMS, "Enable Top Hat", true).getBoolean();
        enableEmeraldHat = config.get(CATEGORY_ITEMS, "Enable Emerald Top Hat", true).getBoolean();
        enableGoggles = config.get(CATEGORY_ITEMS, "Enable Goggles/Monocle", true).getBoolean();
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
        enableRedstoneValvePipe = config.get(CATEGORY_OTHER, "Enable redstone support for Valve Pipes", true).getBoolean();
        disableParticles = config.get(CATEGORY_OTHER, "Disable block break particles (May solve crashes with guns, thumper)", false).getBoolean();
        singleButtonTrackpad = config.get(CATEGORY_OTHER, "Check both mouse buttons for the journal ctrl-click feature for single-button trackpad users. If you have trouble getting the ctrl-click feature to work on a trackpad, enable this.", false).getBoolean();
        removeHopperRecipe = config.get(CATEGORY_OTHER, "Remove Hopper crafting recipes (can still be made with the crucible)", true).getBoolean();

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
        hasAllCrucial = BoilerModule.enableBoiler && SafetyModule.enableGauge && StorageModule.enableTank && enablePipe;

        config.save();
    }
}
