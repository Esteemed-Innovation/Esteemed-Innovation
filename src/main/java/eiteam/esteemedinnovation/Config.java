package eiteam.esteemedinnovation;

import eiteam.esteemedinnovation.api.Constants;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Config {
    //Don't change this string. - @xbony2
    public static final String VERSION = "@VERSION@";

    public static final int JUMP_BOOST_CONSUMPTION_DEFAULT = 10;
    public static final int JETPACK_CONSUMPTION_DEFAULT = 10;
    public static final int JUMP_BOOST_CONSUMPTION_SHIFT_BOOST_DEFAULT = 10;
    public static final int POWER_FIST_CONSUMPTION_DEFAULT = 5;
    public static final int THRUSTER_CONSUMPTION_DEFAULT = 5;
    public static final int RUN_ASSIST_CONSUMPTION_DEFAULT = 5;
    public static final int EXO_CONSUMPTION_DEFAULT = 5;
    public static final int STEAM_TOOL_CONSUMPTION_DEFAULT = 800;
    public static final int BASIC_TANK_CAPACITY_DEFAULT = 36000;
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
    public static final int STEAMCELL_CAPACITY_DEFAULT = 100;

    public static final float extendedRange = 2.0F; //Range extension in blocks
    public static final float fallAssistDivisor = 2;

    public static boolean genCopperOverworld;
    public static boolean genZincOverworld;
    public static boolean genCopperEnd;
    public static boolean genZincEnd;
    public static boolean genCopperNether;
    public static boolean genZincNether;
    public static int[] zincExtraDimensionIDs;
    public static int[] copperExtraDimensionIDs;
    public static boolean genZincExtras;
    public static boolean genCopperExtras;
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
    public static boolean enableBaublesIntegration;
    public static boolean enableRailcraftIntegration;
    public static boolean enableNEIIntegration;

    public static boolean enableAnchorAnvilRecipe;
    public static int mortarRadius;
    public static boolean expensiveMusketRecipes;
    public static int smasherDoubleChance;
    public static boolean dropItem;
    public static int duplicateLogs;
    public static int exoConsumption;
    public static int basicTankCapacity;
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
    public static int hammerConsumption;
    public static int fanConsumption;
    public static int screwConsumption;
    public static int heaterConsumption;
    public static int vacuumConsumption;
    public static int zincPlateConsumption;
    public static int rebreatherConsumption;
    public static int hydrophobicConsumption;
    public static int pyrophobicConsumption;
    public static int pistonPushConsumption;
    public static int reloadingConsumption;
    public static int dragonRoarConsumption;
    public static int steamCellCapacity;
    public static float musketDamage;
    public static float pistolDamage;
    public static float blunderbussDamage;


    public static int villagerId;

    public static boolean enableRedstoneValvePipe;

    // blocks
    public static boolean enableFunnel;
    public static boolean enablePlonker;
    public static boolean enableBoiler;
    public static boolean enableCharger;
    public static boolean enableCrucible;
    public static boolean enableHellCrucible;
    public static boolean enableEngineering;
    public static boolean enableFan;
    public static boolean enableFluidSteamConverter;
    public static boolean enableMortar;
    public static boolean enableGauge;
    public static boolean enableHammer;
    public static boolean enableHeater;
    public static boolean enableHorn;
    public static boolean enableMold;
    public static boolean enablePipe;
    public static boolean enablePump;
    public static boolean enableRuptureDisc;
    public static boolean enableSmasher;
    public static boolean enableTank;
    public static boolean enableThumper;
    public static boolean enableVacuum;
    public static boolean enableValvePipe;
    public static boolean enableChargingPad;
    public static boolean enableSaw;
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
    public static boolean enableEnhancementAblaze;
    public static boolean enableEnhancementRevolver;
    public static boolean enableEnhancementSpeedloader;
    public static boolean enableEnhancementSilencer;
    public static boolean enableEnhancementRecoil;
    public static boolean enableEnhancementSpeedy;
    public static boolean enableEnhancementFastRockets;
    public static boolean enableEnhancementAmmo;
    public static boolean enableEnhancementAirStrike;
    public static boolean enableExosuit;
    public static boolean enableFallAssist;
    public static boolean enableJetpack;
    public static boolean enableFirearms;
    public static boolean enableRL;
    public static boolean enableRocket;
    public static boolean enableRocketConcussive;
    public static boolean enableRocketMining;
    public static boolean enablePowerFist;
    public static boolean enableSpyglass;
    public static boolean enableSteamTools;
    public static boolean enableSurvivalist;
    public static boolean enableThrusters;
    public static boolean enableCanningMachine;
    public static boolean enableExtendoFist;
    public static boolean enablePitonDeployer;
    public static boolean disableMainBarrelRecipe;
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
    public static boolean enableSteamCell;
    public static boolean enableSteamCellBauble;

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

    public static boolean enableCanister;

    public static boolean singleButtonTrackpad;


    public static void load(FMLPreInitializationEvent event) {
        File configurationDir = event.getModConfigurationDirectory();
        Configuration config = new Configuration(new File(configurationDir, "EsteemedInnovation.cfg"));
        config.load();

        // WORLD GEN
        genCopperOverworld = config.get("World Generation", "Generate Overworld Copper", true).getBoolean();
        genZincOverworld = config.get("World Generation", "Generate Overworld Zinc", true).getBoolean();
        genCopperEnd = config.get("World Generation", "Generate End Copper", false).getBoolean();
        genZincEnd = config.get("World Generation", "Generate End Zinc", false).getBoolean();
        genCopperNether = config.get("World Generation", "Generate Nether Copper", false).getBoolean();
        genZincNether = config.get("World Generation", "Generate Nether Zinc", false).getBoolean();
        String zincDims = config.get("World Generation", "Extra dimensions to generate Zinc in, separate by ;", "").getString();
        String copperDims = config.get("World Generation", "Extra dimensions to generate Copper in, separate by ;", "").getString();
        genZincExtras = config.get("World Generation", "Generate Zinc in the above extra dimensions", true).getBoolean();
        genCopperExtras = config.get("World Generation", "Generate Copper in the above extra dimensions", true).getBoolean();
        villagerId = config.get("World Generation", "Villager ID", 694).getInt();
        genPoorZincOre = config.get("Integration", "Railcraft Poor Zinc Ore", true).getBoolean();
        workshopLimit = config.get("World Generation", "Maximum number of Workshops allowed to generate per village", 1).getInt();
        workshopWeight = config.get("World Generation", "Workshop spawn weight", 7).getInt(7);

        copperExtraDimensionIDs = getDimensionIDsFromString(copperDims);
        zincExtraDimensionIDs = getDimensionIDsFromString(zincDims);

        // WEAPONS
        expensiveMusketRecipes = config.get("Weapons", "Hardcore Musket Cartridge recipe (1 gunpowder per cartridge)", false).getBoolean();
        disableMainBarrelRecipe = config.get("Weapons", "Remove ingot barrel recipe in case of conflicts (keeps plate recipe)", false).getBoolean();
        enableFirearms = config.get("Weapons", "Enable firearms", true).getBoolean();
        enableRL = config.get("Weapons", "Enable Rocket Launcher", true).getBoolean();
        enableRocket = config.get("Weapons", "Enable Normal Rocket", true).getBoolean();
        enableRocketConcussive = config.get("Weapons", "Enable Concussive Rocket", true).getBoolean();
        enableRocketMining = config.get("Weapons", "Enable Mining Charge", true).getBoolean();
        enableEnhancementAblaze = config.get("Weapons", "Enable Blaze Barrel enhancement", true).getBoolean();
        enableEnhancementRevolver = config.get("Weapons", "Enable Revolver enhancement", true).getBoolean();
        enableEnhancementSpeedloader = config.get("Weapons", "Enable Bolt Action enhancement", true).getBoolean();
        enableEnhancementSilencer = config.get("Weapons", "Enable Makeshift Suppressor enhancement", true).getBoolean();
        enableEnhancementRecoil = config.get("Weapons", "Enable Recoil Pad enhancement", true).getBoolean();
        enableEnhancementSpeedy = config.get("Weapons", "Enable Breech Loader enhancement", true).getBoolean();
        enableEnhancementFastRockets = config.get("Weapons", "Enable Streamlined Barrel enhancement", true).getBoolean();
        enableEnhancementAmmo = config.get("Weapons", "Enable extended Magazine enhancement", true).getBoolean();
        enableEnhancementAirStrike = config.get("Weapons", "Enable Air Strike enhancement", true).getBoolean();
        musketDamage = Float.valueOf(config.get("Weapons", "Musket damage", "20.0F").getString());
        pistolDamage = Float.valueOf(config.get("Weapons", "Pistol damage", "15.0F").getString());
        blunderbussDamage = Float.valueOf(config.get("Weapons", "Blunderbuss damage", "25.0F").getString());

        // MACHINES
        mortarRadius = config.get("Machines", "Item Mortar accuracy (radius in blocks)", 2).getInt();
        smasherDoubleChance = config.get("Machines", "Chance of double drops from Rock Smasher (%)", 75).getInt();
        duplicateLogs = config.get("Machines", "Chance of duplicate drops from Buzzsaw (1 in X)", 6).getInt();
        dropItem = config.get("Machines", "Thumper drops items (may lag servers)", true).getBoolean();

        // STEAM SYSTEM
        config.addCustomCategoryComment("SteamSystem", "Disabling any piece marked crucial disables pretty much the whole mod.");
        enableBoiler = config.get("SteamSystem", "Enable Boiler (Crucial)", true).getBoolean();
        enableHorn = config.get("SteamSystem", "Enable Horn", true).getBoolean();
        enableGauge = config.get("SteamSystem", "Enable Pressure Gauge (Crucial)", true).getBoolean();
        enablePipe = config.get("SteamSystem", "Enable Steam Pipe (Crucial)", true).getBoolean();
        enableRuptureDisc = config.get("SteamSystem", "Enable Rupture Disc", true).getBoolean();
        enableTank = config.get("SteamSystem", "Enable Steam Tank (Crucial)", true).getBoolean();
        enableValvePipe = config.get("SteamSystem", "Enable Valve Pipe", true).getBoolean();
        enableFluidSteamConverter = config.get("Blocks", "Enable Steam Converter", true).getBoolean();
        //enableBloodBoiler = config.get("SteamSystem", "Enable Blood Boiler", true).getBoolean();


        // BLOCKS
        enableSaw = config.get("Blocks", "Enable the Buzzsaw", true).getBoolean();
        enableCharger = config.get("Blocks", "Enable Steam Filler", true).getBoolean();
        enableChargingPad = config.get("Blocks", "Enable Filling Pad", true).getBoolean();
        enableCrucible = config.get("Blocks", "Enable Crucible", true).getBoolean();
        enableHellCrucible = config.get("Blocks", "Enable Nether Crucible", true).getBoolean();
        enableEngineering = config.get("Blocks", "Enable Engineering Table", true).getBoolean();
        enableFan = config.get("Blocks", "Enable Fan (disabling this disables Vacuum)", true).getBoolean();
        //enableGenocide = config.get("Blocks", "Enable Aquatic Genocide Machine", true).getBoolean();
        enableMortar = config.get("Blocks", "Enable Item Mortar", true).getBoolean();
        enableHammer = config.get("Blocks", "Enable Steam Hammer", true).getBoolean();
        enableHeater = config.get("Blocks", "Enable Steam Heater", true).getBoolean();
        enableMold = config.get("Blocks", "Enable Mold block", true).getBoolean();
        enablePump = config.get("Blocks", "Enable Archimedes Screw", true).getBoolean();
        enableSmasher = config.get("Blocks", "Enable Rock Smasher", true).getBoolean();
        enableThumper = config.get("Blocks", "Enable Thumper", true).getBoolean();
        enableVacuum = config.get("Blocks", "Enable Vacuum", true).getBoolean();
        enablePlonker = config.get("Blocks", "Enable Plonker", true).getBoolean();
        enableFunnel = config.get("Block", "Enable Funnel", true).getBoolean();

        // BLOCK CONSUMPTION RATES
        hammerConsumption = config.get("Consumption", "Steam Hammer consumption", 4000).getInt();
        fanConsumption = config.get("Consumption", "Fan consumption", 1).getInt();
        screwConsumption = config.get("Consumption", "Archimedes Screw consumption", 100).getInt();
        heaterConsumption = config.get("Consumption", "Steam Heater consumption", 20).getInt();
        vacuumConsumption = config.get("Consumption", "Vacuum consumption", 3).getInt();

        // EXOSUIT
        passiveDrain = config.get("Exosuit", "Passively drain steam while in use", true).getBoolean();
        enableExosuit = config.get("Exosuit", "Enable Exosuit (disabling also disables all upgrades)", true).getBoolean();
        exoConsumption = config.get("Exosuit", "The amount of steam the Exosuit consumes", EXO_CONSUMPTION_DEFAULT).getInt();
        jumpBoostConsumption = config.get("Exosuit", "The amount of steam jump boost consumes", JUMP_BOOST_CONSUMPTION_DEFAULT).getInt();
        jetpackConsumption = config.get("Exosuit", "The amount of steam the Jetpack consumes", JETPACK_CONSUMPTION_DEFAULT).getInt();
        jumpBoostConsumptionShiftJump = config.get("Exosuit", "The amount of steam the jump boost shift jump consumes", JUMP_BOOST_CONSUMPTION_SHIFT_BOOST_DEFAULT).getInt();
        thrusterConsumption = config.get("Exosuit", "The amount of steam the Exosuit Thrusters consumes", THRUSTER_CONSUMPTION_DEFAULT).getInt();
        runAssistConsumption = config.get("Exosuit", "The amount of steam the Exosuit Run Assist consumes", RUN_ASSIST_CONSUMPTION_DEFAULT).getInt();
        powerFistConsumption = config.get("Exosuit", "The amount of steam the Exosuit Power Fist consumes", POWER_FIST_CONSUMPTION_DEFAULT).getInt();
        zincPlateConsumption = config.get("Exosuit", "The amount of steam the Exosuit Zinc Plate consumes", ZINC_PLATE_CONSUMPTION_DEFAULT).getInt();
        rebreatherConsumption = config.get("Exosuit", "The amount of steam the Rebreather consumes", REBREATHER_CONSUMPTION_DEFAULT).getInt();
        hydrophobicConsumption = config.get("Exosuit", "The amount of steam the Hydrophobic Coatings consume", HYDROPHOBIC_CONSUMPTION_DEFAULT).getInt();
        pyrophobicConsumption = config.get("Exosuit", "The amount of steam the Pyrophobic Coatings consume", PYROPHOBIC_CONSUMPTION_DEFAULT).getInt();
        enableAnchorAnvilRecipe = config.get("Exosuit", "Use the leadless Anchor Heels recipe. This will always be true if there is no lead available.", false).getBoolean();
        pistonPushConsumption = config.get("Exosuit", "The amount of steam the Piston Push consumes", PISTON_PUSH_CONSUMPTION_DEFAULT).getInt();
        reloadingConsumption = config.get("Exosuit", "The amount of steam the Reloading Holsters consume", RELOADING_CONSUMPTION_DEFAULT).getInt();
        dragonRoarConsumption = config.get("Exosuit", "The amount of steam the Dragon Roar consumes", DRAGON_ROAR_CONSUMPTION_DEFAULT).getInt();

        // EXOSUIT UPGRADES
        enableFallAssist = config.get("Exosuit Upgrades", "Enable Fall Assist", true).getBoolean();
        enableJumpAssist = config.get("Exosuit Upgrades", "Enable Leap Actuator", true).getBoolean();
        enableDoubleJump = config.get("Exosuit Upgrades", "Enable Pulse Nozzle", true).getBoolean();
        enableRunAssist = config.get("Exosuit Upgrades", "Enable Modular Accelerator", true).getBoolean();
        enableStealthUpgrade = config.get("Exosuit Upgrades", "Enable Acoustic Dampener", true).getBoolean();
        enableJetpack = config.get("Exosuit Upgrades", "Enable Steam Jetpack", true).getBoolean();
        enableThrusters = config.get("Exosuit Upgrades", "Enable Thrusters", true).getBoolean();
        enableWings = config.get("Exosuit Upgrades", "Enable Wings", true).getBoolean();
        enablePowerFist = config.get("Exosuit Upgrades", "Enable Power Fist", true).getBoolean();
        enableCanningMachine = config.get("Exosuit Upgrades", "Enable Canner", true).getBoolean();
        enableExtendoFist = config.get("Exosuit Upgrades", "Enable Extendo Fist", true).getBoolean();
        enablePitonDeployer = config.get("Exosuit Upgrades", "Enable Piton Deployer", true).getBoolean();
        enableReinforcedTank = config.get("Exosuit Upgrades", "Enable Reinforced Tank", true).getBoolean();
        enableUberReinforcedTank = config.get("Exosuit Upgrades", "Enable Heavily Reinforced Tank", true).getBoolean();
        enableEnderShroud = config.get("Exosuit Upgrades", "Enable Ender Shroud", true).getBoolean();
        enableRebreather = config.get("Exosuit Upgrades", "Enable Rebreather", true).getBoolean();
        enableHydrophobic = config.get("Exosuit Upgrades", "Enable Hydrophobic Coatings", true).getBoolean();
        enablePyrophobic = config.get("Exosuit Upgrades", "Enable Pyrophobic Coatings", true).getBoolean();
        enableAnchorHeels = config.get("Exosuit Upgrades", "Enable Anchor Heels", true).getBoolean();
        enablePistonPush = config.get("Exosuit Upgrades", "Enable Piston Push", true).getBoolean();
        enableReloadingHolsters = config.get("Exosuit Upgrades", "Enable Reloading Holsters", true).getBoolean();
        enableFrequencyShifter = config.get("Exosuit Upgrades", "Enable Frequency Shifter", true).getBoolean();
        enableDragonRoar = config.get("Exosuit Upgrades", "Enable Dragon Roar", true).getBoolean();

        enableCopperPlate = config.get("Exosuit Plates", "Enable copper plate", true).getBoolean();
        enableZincPlate = config.get("Exosuit Plates", "Enable zinc plate", true).getBoolean();
        enableIronPlate = config.get("Exosuit Plates", "Enable iron plate", true).getBoolean();
        enableGoldPlate = config.get("Exosuit Plates", "Enable gold plate", true).getBoolean();
        enableBrassPlate = config.get("Exosuit Plates", "Enable brass plate", true).getBoolean();
        enableLeadPlate = config.get("Exosuit Plates", "Enable lead plate", true).getBoolean();
        enableThaumiumPlate = config.get("Exosuit Plates", "Enable thaumium plate", true).getBoolean();
        enableElementiumPlate = config.get("Exosuit Plates", "Enable elementium plate", true).getBoolean();
        enableTerrasteelPlate = config.get("Exosuit Plates", "Enable terrasteel plate", true).getBoolean();
        enableYetiPlate = config.get("Exosuit Plates", "Enable yeti plate", true).getBoolean();
        enableFieryPlate = config.get("Exosuit Plates", "Enable fiery plate", true).getBoolean();
        enableSadistPlate = config.get("Exosuit Plates", "Enable sadist plate", true).getBoolean();
        enableVibrantPlate = config.get("Exosuit Plates", "Enable vibrant plate", true).getBoolean();
        enableEnderiumPlate = config.get("Exosuit Plates", "Enable enderium plate", true).getBoolean();
        enableGildedIronPlate = config.get("Exosuit Plates", "Enable gilded iron plate", true).getBoolean();

        basicTankCapacity = config.get("Exosuit Upgrades", "The amount of steam the basic tank can hold", BASIC_TANK_CAPACITY_DEFAULT).getInt();
        reinforcedTankCapacity = config.get("Exosuit Upgrades", "The amount of steam the reinforced tank can hold", REINFORCED_TANK_CAPACITY_DEFAULT).getInt();
        uberReinforcedTankCapacity = config.get("Exosuit Upgrades", "The amount of steam the heavily reinforced tank can hold", UBER_REINFORCED_TANK_CAPACITY_DEFAULT).getInt();
        //enableDoubleJump = config.get("Exosuit Upgrades", "Enable double jump", true).getBoolean();

        // ITEMS
        enableAstrolabe = config.get("Items", "Enable Astrolabe", true).getBoolean();
        enableSpyglass = config.get("Items", "Enable Spyglass", true).getBoolean();
        enableSteamTools = config.get("Items", "Enable steam tools", true).getBoolean();
        enableSurvivalist = config.get("Items", "Enable Survivalist's Toolkit", true).getBoolean();
        enableWrench = config.get("Items", "Enable Pipe Wrench", true).getBoolean();
        enableCanister = config.get("Items", "Enable Canisters", true).getBoolean();
        enableTopHat = config.get("Items", "Enable Top Hat", true).getBoolean();
        enableEmeraldHat = config.get("Items", "Enable Emerald Top Hat", true).getBoolean();
        enableGoggles = config.get("Items", "Enable Goggles/Monocle", true).getBoolean();
        steamToolConsumptionAxe = config.get("Items", "The consumption rate of the Steam Axe", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();
        steamToolConsumptionDrill = config.get("Items", "The consumption rate of the Steam Drill", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();
        steamToolConsumptionShovel = config.get("Items", "The consumption rate of the Steam Shovel", STEAM_TOOL_CONSUMPTION_DEFAULT).getInt();
        enableSteamCell = config.get("Items", "Enable Steam Cell", true).getBoolean();
        steamCellCapacity = config.get("Items", "Steam Cell capacity", STEAMCELL_CAPACITY_DEFAULT).getInt();
        enableSteamCellBauble = config.get("Items", "Enable Steam Cell Bauble", true).getBoolean();

        // STEAM TOOL UPGRADES
        enableBigDrill = config.get("Steam Tool Upgrades", "Enable Steam Drill's Hammer Head upgrade", true).getBoolean();
        enableLeafBlower = config.get("Steam Tool Upgrades", "Enable Steam Axe's Leaf Blower upgrade", true).getBoolean();
        enableCultivator = config.get("Steam Tool Upgrades", "Enable Steam Shovel's Cultivator upgrade", true).getBoolean();
        enableRotaryBlades = config.get("Steam Tool Upgrades", "Enable Steam Shovel's Rotary Blades upgrade", true).getBoolean();
        enableBattleDrill = config.get("Exosuit Upgrades", "Enable BattleDrill", true).getBoolean();
        enableSifter = config.get("Steam Tool Upgrades", "Enable Steam Shovel's Sifter upgrade", true).getBoolean();
        enableStoneGrinder = config.get("Steam Tool Upgrades", "Enable Steam Drill's Stone Grinder upgrade", true).getBoolean();
        enableBackhoe = config.get("Steam Tool Upgrades", "Enable Steam Shovel's Backhoe upgrade", true).getBoolean();
        enableTheVoid = config.get("Steam Tool Upgrades", "Enable Steam Tool core upgrade the Void", true).getBoolean();
        enableAutosmelting = config.get("Steam Tool Upgrades", "Enable Steam Tool core upgrade autosmelting", true).getBoolean();
        enableOverclocker = config.get("Steam Tool Upgrades", "Enable Steam Tool core upgrade overclocker", true).getBoolean();
        enablePreciseCuttingHead = config.get("Steam Tool Upgrades", "Enable Steam Drill's Precise Cutting Head", true).getBoolean();
        enableInternalProcessingUnit = config.get("Steam Tool Upgrades", "Enable Steam Drill's Internal Processing Unit", true).getBoolean();
        enableTreeFeller = config.get("Steam Tool Upgrades", "Enable Steam Axe's Tree Felling upgrade", true).getBoolean();
        enableChainsaw = config.get("Steam Tool Upgrades", "Enable Steam Axe's Chainsaw upgrade", true).getBoolean();
        enableFortune = config.get("Steam Tool Upgrades", "Enable Steam Drill's fortune upgrade", true).getBoolean();
        enableForestFire = config.get("Steam Tool Upgrades", "Enable Steam Axe's Forest Fire upgrade", true).getBoolean();
        enableThermalDrill = config.get("Steam Tool Upgrades", "Enable Steam Drill's Thermal Drill upgrade", true).getBoolean();
        enableChargePlacer = config.get("Steam Tool Upgrades", "Enable Steam Drill's Charge Placer upgrade", true).getBoolean();

        backhoeRange = config.get("Steam Tool Upgrades", "The range (in each direction) that the Backhoe upgrade effects", 16).getInt();
        battleDrillConsumption = config.get("Steam Tool Upgrades", "Steam consumption for the " +
          "BattleDrill. This is not the actual amount of steam, but the relative item damage.",
          BATTLE_DRILL_CONSUMPTION_DEFAULT).getInt();
        blacklistedStoneGrinderNuggets = Arrays.asList(config.get("Steam Tool Upgrades", "Nuggets that the Stone Grinder cannot produce. These are OreDict entries", new String[] {}).getStringList());

        // OTHER
        easterEggs = config.get("Other", "Enable Easter Eggs", true).getBoolean();
        Constants.wimpMode = config.get("Other", "Enable wimp mode (no explosions)", false).getBoolean();
        enableRedstoneValvePipe = config.get("Other", "Enable redstone support for Valve Pipes", true).getBoolean();
        disableParticles = config.get("Other", "Disable block break particles (May solve crashes with guns, thumper)", false).getBoolean();
        singleButtonTrackpad = config.get("Other", "Check both mouse buttons for the journal ctrl-click feature for single-button trackpad users. If you have trouble getting the ctrl-click feature to work on a trackpad, enable this.", false).getBoolean();

        //INTEGRATION
        enableThaumcraftIntegration = config.get("Integration", "Enable Thaumcraft", true).getBoolean();
        enableNitorPoweredCrucible = config.get("Integration", "Allow the Thaumcraft Nitor to power the Crucible", true).getBoolean();
        enableBotaniaIntegration = config.get("Integration", "Enable Botania", true).getBoolean();
        enableEnchiridionIntegration = config.get("Integration", "Enable Enchiridion", true).getBoolean();
        enableTwilightForestIntegration = config.get("Integration", "Enable Twilight Forest", true).getBoolean();
        enableBloodMagicIntegration = config.get("Integration", "Enable Blood Magic", true).getBoolean();
        enableEnderIOIntegration = config.get("Integration", "Enable Ender IO", true).getBoolean();
        enableThermalFoundationIntegration = config.get("Integration", "Enable Thermal Foundation", true).getBoolean();
        enableIC2Integration = config.get("Integration", "Enable IC2", true).getBoolean();
        enableNaturaIntegration = config.get("Integration", "Enable Natura", true).getBoolean();
        enableTinkersConstruct = config.get("Integration", "Enable Tinker's Construct", true).getBoolean();
        enableBaublesIntegration = config.get("Integration", "Enable Baubles", true).getBoolean();
        enableRailcraftIntegration = config.get("Integration", "Enable Railcraft", true).getBoolean();
        enableNEIIntegration = config.get("Integration", "Enable NEI", true).getBoolean();

        hasAllCrucial = enableBoiler && enableGauge && enableTank && enablePipe;

        config.save();
    }

    private static int[] getDimensionIDsFromString(String dims) {
        if (!dims.isEmpty()) {
            try {
                String replacedDims = dims.replaceAll("[a-zA-Z\\s]", "");
                String[] array = replacedDims.split(";");
                final int[] ids = new int[array.length];
                for (int i = 0; i < array.length; i++) {
                    int id = Integer.parseInt(array[i]);
                    // Do not store vanilla dimensions.
                    if (id == 0 || Math.abs(id) == 1) {
                        continue;
                    }
                    ids[i] = id;
                }
                return ids;
                //This exception needs to be ignored for when the value is null.
            } catch (NumberFormatException ignore) {}
        }
        return new int[] {};
    }
}
