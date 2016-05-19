package flaxbeard.steamcraft.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class UtilMisc {
    
	private UtilMisc() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static boolean doesMatch(ItemStack item, String oreDict) {
        for (ItemStack i : OreDictionary.getOres(oreDict)) {
            if (i.isItemEqual(item)) {
                return true;
            }
        }
        return false;
    }
}
