package flaxbeard.steamcraft;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {
	public static boolean genCopper;
	public static boolean genZinc;
	
	public static boolean easterEggs;
	
	public static void load(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		genCopper = config.get("World Generation", "Generate Copper", true).getBoolean(true);
		genZinc = config.get("World Generation", "Generate Zinc", true).getBoolean(true);
		easterEggs = config.get("Other", "Enable Easter Eggs", true).getBoolean(true);


		config.save();
	}
}
