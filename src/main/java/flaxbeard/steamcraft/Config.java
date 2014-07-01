package flaxbeard.steamcraft;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {
	public static boolean genCopper;
	public static boolean genZinc;
	public static boolean passiveDrain;
	
	public static boolean easterEggs;
	
	public static int mortarRadius;
	public static boolean expensiveMusketRecipes;
	public static int chance;
	public static boolean dropItem;
	
	public static void load(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		genCopper = config.get("World Generation", "Generate Copper", true).getBoolean(true);
		genZinc = config.get("World Generation", "Generate Zinc", true).getBoolean(true);
		expensiveMusketRecipes = config.get("Weapons", "Hardcore Musket Cartridge recipe (1 gunpowder per cartridge)", false).getBoolean(true);
		
		mortarRadius = config.get("Machines", "Item Mortar accuracy (radius in blocks)", 2).getInt();
		chance = config.get("Machines", "Chance of double drops from Rock Smasher (1 in X)", 4).getInt();
		dropItem = config.get("Machines", "Thumper drops items (may lag servers)", true).getBoolean(true);

		passiveDrain = config.get("Exosuit", "Passively drain steam while in use", true).getBoolean(true);
		easterEggs = config.get("Other", "Enable Easter Eggs", true).getBoolean(true);


		config.save();
	}
}
