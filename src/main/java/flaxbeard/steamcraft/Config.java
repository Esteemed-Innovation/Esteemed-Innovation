package flaxbeard.steamcraft;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {
    public static final int jumpBoostConsumption = 1;
    public static final int jetpackConsumption = 1;
    public static final int jumpBoostConsumptionShiftJump = 3;
    public static final int powerFistConsumption = 5;
    public static final float extendedRange = 2.0F; //Range extension in blocks
    public static final float fallAssistDivisor = 2;

    public static final int basicTankCap = 36000;
    public static final int reinforcedTankCap = 72000;
    public static final int uberReinforcedTankCap = 144000;

    public static final String VERSION = "0.27.0";
    public static boolean genCopper;
    public static boolean genZinc;
    public static boolean passiveDrain;
    public static boolean disableParticles;

    public static boolean easterEggs;

    public static int mortarRadius;
    public static boolean expensiveMusketRecipes;
    public static int chance;
    public static boolean dropItem;
    public static boolean genPoorOre;
    public static int duplicateLogs;


    public static int villagerId;

    public static boolean wimpMode;

    // blocks
    public static boolean enableBlockPlacer;
    public static boolean enableBoiler;
    public static boolean enableCharger;
    public static boolean enableCrucible;
    public static boolean enableHellCrucible;
    public static boolean enableEngineering;
    public static boolean enableFan;
    public static boolean enableFlashBoiler;
    public static boolean enableFluidSteamConverter;
    public static boolean enableGenocide; //NYI
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
    public static boolean enableWrench;
    public static boolean enableSaw;

    // modular tool crap
    public static boolean enableModularTool;
    public static boolean enableDrillBit;
    public static boolean enableChainsawBit;
    public static boolean enableShovelBit;
    public static boolean enableSpeedUpgrade;
    public static boolean enableWaterUpgrade;
    public static boolean enableHeaterUpgrade;
    public static boolean enableDiamondUpgrade;
    public static boolean enableSilkUpgrade;
    public static boolean enableFortuneUpgrade;

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

    //plates
    public static boolean enableCopperPlate;
    public static boolean enableIronPlate;
    public static boolean enableGoldPlate;
    public static boolean enableBrassPlate;
    public static boolean enableThaumiumPlate;
    public static boolean enableElementiumPlate;
    public static boolean enableTerrasteelPlate;
    public static boolean enableYetiPlate;
    public static boolean enableFieryPlate;
    public static boolean enableSadistPlate;
    public static boolean enableVibrantPlate;
    public static boolean enableEnderiumPlate;


    public static boolean enableWings;
    public static boolean hasAllCrucial;

    public static boolean enableCanister;


    public static void load(FMLPreInitializationEvent event) {
        File configurationDir = ReflectionHelper.getPrivateValue(FMLPreInitializationEvent.class, event, 2);
        File oldConfigFile = new File(configurationDir, "Steamcraft.cfg");
        if (oldConfigFile.exists()) {
            try {
                FileUtils.copyFile(new File(configurationDir, "Steamcraft.cfg"), new File(configurationDir, "FlaxbeardsSteamPower.cfg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            oldConfigFile.delete();
        }
        Configuration config = new Configuration(new File(configurationDir, "FlaxbeardsSteamPower.cfg"));
        config.load();

        // WORLD GEN
        genCopper = config.get("World Generation", "Generate Copper", true).getBoolean(true);
        genZinc = config.get("World Generation", "Generate Zinc", true).getBoolean(true);
        villagerId = config.get("World Generation", "FSP Villager ID", 694).getInt(694);
        genPoorOre = config.get("Integration", "[Railcraft] Generate Poor Zinc when RC is installed", true).getBoolean(true);

        // WEAPONS
        expensiveMusketRecipes = config.get("Weapons", "Hardcore Musket Cartridge recipe (1 gunpowder per cartridge)", false).getBoolean(true);
        disableMainBarrelRecipe = config.get("Weapons", "Remove ingot barrel recipe in case of conflicts (keeps plate recipe)", false).getBoolean(true);
        enableFirearms = config.get("Weapons", "Enable firearms", true).getBoolean(true);
        enableRL = config.get("Weapons", "Enable rocket launcher", true).getBoolean(true);
        enableRocket = config.get("Weapons", "Enable normal rocket", true).getBoolean(true);
        enableRocketConcussive = config.get("Weapons", "Enable concussive rocket", true).getBoolean(true);
        enableRocketMining = config.get("Weapons", "Enable mining charge", true).getBoolean(true);

        enableEnhancementAblaze = config.get("Weapons", "Enable blaze barrel enhancement", true).getBoolean(true);
        enableEnhancementRevolver = config.get("Weapons", "Enable revolver enhancement", true).getBoolean(true);
        enableEnhancementSpeedloader = config.get("Weapons", "Enable bolt action enhancement", true).getBoolean(true);
        enableEnhancementSilencer = config.get("Weapons", "Enable makeshift suppressor enhancement", true).getBoolean(true);
        enableEnhancementRecoil = config.get("Weapons", "Enable recoil pad enhancement", true).getBoolean(true);
        enableEnhancementSpeedy = config.get("Weapons", "Enable breech loader enhancement", true).getBoolean(true);
        enableEnhancementFastRockets = config.get("Weapons", "Enable streamlined barrel enhancement", true).getBoolean(true);
        enableEnhancementAmmo = config.get("Weapons", "Enable extended magazine enhancement", true).getBoolean(true);
        enableEnhancementAirStrike = config.get("Weapons", "Enable air strike enhancement", true).getBoolean(true);


        // MACHINES
        mortarRadius = config.get("Machines", "Item Mortar accuracy (radius in blocks)", 2).getInt();
        chance = config.get("Machines", "Chance of double drops from Rock Smasher (1 in X)", 4).getInt();
        duplicateLogs = config.get("Machines", "Chance of duplicate drops from Buzzsaw( 1 in X)", 6).getInt();
        dropItem = config.get("Machines", "Thumper drops items (may lag servers)", true).getBoolean(true);

        // STEAM SYSTEM
        config.addCustomCategoryComment("SteamSystem", "Disabling any piece marked crucial disables pretty much the whole mod.");
        enableBoiler = config.get("SteamSystem", "Enable boiler (Crucial)", true).getBoolean(true);
        enableFlashBoiler = config.get("SteamSystem", "Enable flash boiler", true).getBoolean(true);
        enableHorn = config.get("SteamSystem", "Enable horn", true).getBoolean(true);
        enableGauge = config.get("SteamSystem", "Enable pressure gauge (Crucial)", true).getBoolean(true);
        enablePipe = config.get("SteamSystem", "Enable steam pipe (Crucial)", true).getBoolean(true);
        enableRuptureDisc = config.get("SteamSystem", "Enable rupture disc", true).getBoolean(true);
        enableTank = config.get("SteamSystem", "Enable steam tank (Crucial)", true).getBoolean(true);
        enableValvePipe = config.get("SteamSystem", "Enable valve pipe", true).getBoolean(true);
        enableFluidSteamConverter = config.get("Blocks", "Enable steam pressurizer", true).getBoolean(true);


        // BLOCKS
        enableSaw = config.get("Blocks", "Enable the Buzzsaw", true).getBoolean(true);
        enableBlockPlacer = config.get("Blocks", "Enable Block Placer", true).getBoolean(true);
        enableCharger = config.get("Blocks", "Enable steam filler", true).getBoolean(true);
        enableChargingPad = config.get("Blocks", "Enable filling pad", true).getBoolean(true);
        enableCrucible = config.get("Blocks", "Enable crucible", true).getBoolean(true);
        enableHellCrucible = config.get("Blocks", "Enable nether crucible", true).getBoolean(true);
        enableEngineering = config.get("Blocks", "Enable engineering table", true).getBoolean(true);
        enableFan = config.get("Blocks", "Enable fan (disabling this disables vacuum)", true).getBoolean(true);
        //enableGenocide = config.get("Blocks", "Enable aquatic genocide machine", true).getBoolean(true);
        enableMortar = config.get("Blocks", "Enable item mortar", true).getBoolean(true);
        enableHammer = config.get("Blocks", "Enable steam hammer", true).getBoolean(true);
        enableHeater = config.get("Blocks", "Enable steam heater", true).getBoolean(true);
        enableMold = config.get("Blocks", "Enable mold block", true).getBoolean(true);
        enablePump = config.get("Blocks", "Enable Archimedes Screw", true).getBoolean(true);
        enableSmasher = config.get("Blocks", "Enable rock smasher", true).getBoolean(true);
        enableThumper = config.get("Blocks", "Enable thumper", true).getBoolean(true);
        enableVacuum = config.get("Blocks", "Enable vacuum", true).getBoolean(true);

        // EXOSUIT
        passiveDrain = config.get("Exosuit", "Passively drain steam while in use", true).getBoolean(true);
        enableExosuit = config.get("Exosuit", "Enable exosuit (disabling also disables all upgrades)", true).getBoolean(true);

        // EXOSUIT UPGRADES
        enableFallAssist = config.get("Exosuit Upgrades", "Enable fall assist", true).getBoolean(true);
        enableJumpAssist = config.get("Exosuit Upgrades", "Enable leap actuator", true).getBoolean(true);
        enableDoubleJump = config.get("Exosuit Upgrades", "Enable pulse nozzle", true).getBoolean(true);
        enableRunAssist = config.get("Exosuit Upgrades", "Enable modular accelerator", true).getBoolean(true);
        enableStealthUpgrade = config.get("Exosuit Upgrades", "Enable acoustic dampener", true).getBoolean(true);
        enableJetpack = config.get("Exosuit Upgrades", "Enable jetpack", true).getBoolean(true);
        enableThrusters = config.get("Exosuit Upgrades", "Enable thrusters", true).getBoolean(true);
        enableWings = config.get("Exosuit Upgrades", "Enable wings", true).getBoolean(true);
        enablePowerFist = config.get("Exosuit Upgrades", "Enable power fist", true).getBoolean(true);
        enableCanningMachine = config.get("Exosuit Upgrades", "Enable canning machine", true).getBoolean(true);
        enableExtendoFist = config.get("Exosuit Upgrades", "Enable extendo fist", true).getBoolean(true);
        enablePitonDeployer = config.get("Exosuit Upgrades", "Enable piton deployer", true).getBoolean(true);
        enableReinforcedTank = config.get("Exosuit Upgrades", "Enable reinforced tank", true).getBoolean(true);
        enableUberReinforcedTank = config.get("Exosuit Upgrades", "Enable heavily reinforced tank", true).getBoolean(true);

        enableCopperPlate = config.get("Exosuit Plates", "Enable copper plate", true).getBoolean(true);
        enableIronPlate = config.get("Exosuit Plates", "Enable iron plate", true).getBoolean(true);
        enableGoldPlate = config.get("Exosuit Plates", "Enable gold plate", true).getBoolean(true);
        enableBrassPlate = config.get("Exosuit Plates", "Enable brass plate", true).getBoolean(true);
        enableThaumiumPlate = config.get("Exosuit Plates", "Enable thaumium plate", true).getBoolean(true);
        enableElementiumPlate = config.get("Exosuit Plates", "Enable elementium plate", true).getBoolean(true);
        enableTerrasteelPlate = config.get("Exosuit Plates", "Enable terrasteel plate", true).getBoolean(true);
        enableYetiPlate = config.get("Exosuit Plates", "Enable yeti plate", true).getBoolean(true);
        enableFieryPlate = config.get("Exosuit Plates", "Enable fiery plate", true).getBoolean(true);
        enableSadistPlate = config.get("Exosuit Plates", "Enable sadist plate", true).getBoolean(true);
        enableVibrantPlate = config.get("Exosuit Plates", "Enable vibrant plate", true).getBoolean(true);
        enableEnderiumPlate = config.get("Exosuit Plates", "Enable enderium plate", true).getBoolean(true);

        enableEnderShroud = config.get("Exosuit Upgrades", "Enable ender shroud", true).getBoolean(true);

        //enableDoubleJump = config.get("Exosuit Upgrades", "Enable double jump", true).getBoolean(true);

        // ITEMS
        enableAstrolabe = config.get("Items", "Enable astrolabe", true).getBoolean(true);
        enableSpyglass = config.get("Items", "Enable spyglass", true).getBoolean(true);
        enableSteamTools = config.get("Items", "Enable steam tools", true).getBoolean(true);
        enableSurvivalist = config.get("Items", "Enable survivalist's toolkit", true).getBoolean(true);
        enableWrench = config.get("Items", "Enable wrench", true).getBoolean(true);
        enableCanister = config.get("Items", "Enable canisters", true).getBoolean(true);

        enableTopHat = config.get("Items", "Enable top hat", true).getBoolean(true);
        enableEmeraldHat = config.get("Items", "Enable emerald top hat", true).getBoolean(true);
        enableGoggles = config.get("Items", "Enable goggles/monacle", true).getBoolean(true);

        //MODULAR TOOL
        enableModularTool = config.get("Modular Tool", "Enable the Modular Tool", true).getBoolean(true);

        //MODULAR TOOL UPGRADES
        enableDrillBit = config.get("Modular Tool", "Enable the Drill upgrade", true).getBoolean(true);
        enableChainsawBit = config.get("Modular Tool", "Enable the Chainsaw upgrade", true).getBoolean(true);
        enableShovelBit = config.get("Modular Tool", "Enable the Shovel upgrade", true).getBoolean(true);
        enableSpeedUpgrade = config.get("Modular Tool", "Enable the Speed upgrade", true).getBoolean(true);
        enableWaterUpgrade = config.get("Modular Tool", "Enable the Water upgrade", true).getBoolean(true);
        enableHeaterUpgrade = config.get("Modular Tool", "Enable the Heater upgrade", true).getBoolean(true);
        enableDiamondUpgrade = config.get("Modular Tool", "Enable the Diamond upgrade", true).getBoolean(true);
        enableSilkUpgrade = config.get("Modular Tool", "Enable the Silk Touch upgrade", true).getBoolean(true);
        enableFortuneUpgrade = config.get("Modular Tool", "Enable the Fortune upgrade", true).getBoolean(true);

        // OTHER
        easterEggs = config.get("Other", "Enable Easter Eggs", true).getBoolean(true);
        wimpMode = config.get("Other", "Enable wimp mode (no explosions)", false).getBoolean(false);
        disableParticles = config.get("Other", "Disable block break particles (May solve crashes with guns, thumper)", false).getBoolean(false);

        if (enableBoiler && enableGauge && enableTank && enablePipe) {
            hasAllCrucial = true;
        } else {
            hasAllCrucial = false;
        }

        config.save();
    }
}
