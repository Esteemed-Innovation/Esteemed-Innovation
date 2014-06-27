package flaxbeard.steamcraft;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {
	public static boolean genCopper;
	public static boolean genZinc;
	public static boolean passiveDrain;
	
	public static boolean easterEggs;
	
	public static int mortarRadius;
	
	public static void load(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		genCopper = config.get("World Generation", "Generate Copper", true).getBoolean(true);
		genZinc = config.get("World Generation", "Generate Zinc", true).getBoolean(true);
		mortarRadius = config.get("Machines", "Item Mortar accuracy (radius in blocks)", 2).getInt();
		passiveDrain = config.get("Exosuit", "Passively drain steam while in use", true).getBoolean(true);
		easterEggs = config.get("Other", "Enable Easter Eggs", true).getBoolean(true);


		config.save();
	}
}
