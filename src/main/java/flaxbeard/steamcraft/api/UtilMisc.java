package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class UtilMisc {
    public static boolean doesMatch(ItemStack item, String oreDict) {
        for (ItemStack i : OreDictionary.getOres(oreDict)) {
            if (i.isItemEqual(item)) {
                return true;
            }
        }
        return false;
    }
}
