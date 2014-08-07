package flaxbeard.steamcraft;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {
	public static final String VERSION = "0.25.1";
	public static boolean genCopper;
	public static boolean genZinc;
	public static boolean passiveDrain;
	
	public static boolean easterEggs;
	
	public static int mortarRadius;
	public static boolean expensiveMusketRecipes;
	public static int chance;
	public static boolean dropItem;
	public static boolean genPoorOre;
	public static boolean wimpMode;
	
	public static int villagerId;
	
	// blocks
	public static boolean enableBoiler;
	public static boolean enableCharger;
	public static boolean enableCrucible;
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
	public static boolean enableWrench;
	
	// items
	public static boolean enableAstrolabe;
	public static boolean enableDoubleJump; //NYI
	public static boolean enableEnhancementAblaze; //NYI
	public static boolean enableEnhancementRevolver;//NYI
	public static boolean enableExosuit; 
	public static boolean enableFallAssist; 
	public static boolean enableJetpack; 
	public static boolean enableFirearms;
	public static boolean enablePowerFist;
	public static boolean enableSpyglass;
	public static boolean enableSteamTools;
	public static boolean enableSurvivalist;
	public static boolean enableThrusters;
	public static boolean enableWings;
	public static boolean hasAllCrucial;
	
	
	public static void load(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		// WORLD GEN
		genCopper = config.get("World Generation", "Generate Copper", true).getBoolean(true);
		genZinc = config.get("World Generation", "Generate Zinc", true).getBoolean(true);
		villagerId = config.get("World Generation", "FSP Villager ID", 694).getInt(694);
		genPoorOre = config.get("Integration", "[Railcraft] Generate Poor Zinc when RC is installed", true).getBoolean(true);

		// WEAPONS
		expensiveMusketRecipes = config.get("Weapons", "Hardcore Musket Cartridge recipe (1 gunpowder per cartridge)", false).getBoolean(true);
		enableFirearms = config.get("Weapons", "Enable firearms",true).getBoolean(true);
		//enableEnhancementAblaze = config.get("Weapons", "Enable ablaze enhancement", true).getBoolean(true);
		//enableEnhancementRevolver = config.get("Weapons", "Enable revolver enhancement", true).getBoolean(true);
		
		
		// MACHINES
		mortarRadius = config.get("Machines", "Item Mortar accuracy (radius in blocks)", 2).getInt();
		chance = config.get("Machines", "Chance of double drops from Rock Smasher (1 in X)", 4).getInt();
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
		enableCharger = config.get("Blocks", "Enable steam filler", true).getBoolean(true);
		enableCrucible = config.get("Blocks", "Enable crucible", true).getBoolean(true);
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
		enableJetpack = config.get("Exosuit Upgrades", "Enable jetpack", true).getBoolean(true);
		enableThrusters = config.get("Exosuit Upgrades", "Enable thrusters", true).getBoolean(true);
		enableWings = config.get("Exosuit Upgrades", "Enable wings", true).getBoolean(true);
		enablePowerFist = config.get("Exosuit Upgrades", "Enable power fist", true).getBoolean(true);
		//enableDoubleJump = config.get("Exosuit Upgrades", "Enable double jump", true).getBoolean(true);
		
		// ITEMS
		enableAstrolabe = config.get("Items", "Enable astrolabe", true).getBoolean(true);
		enableSpyglass = config.get("Items", "Enable spyglass", true).getBoolean(true);
		enableSteamTools = config.get("Items", "Enable steam tools", true).getBoolean(true);
		enableSurvivalist = config.get("Items", "Enable survivalist's toolkit", true).getBoolean(true);
		enableWrench = config.get("Items", "Enable wrench", true).getBoolean(true);

		// OTHER
		easterEggs = config.get("Other", "Enable Easter Eggs", true).getBoolean(true);
		wimpMode = config.get("Other", "Enable wimp mode (no explosions)",false).getBoolean(false);
		
		if (enableBoiler && enableGauge && enableTank && enablePipe){
			hasAllCrucial = true;
		} else {
			hasAllCrucial = false;
		}
		
		config.save();
	}
}
