package eiteam.esteemedinnovation.tools.steam;

import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.ItemDrillHeadUpgrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Class for internal use in the steam tools. For utils relating to the steam tool, see
 * eiteam.esteemedinnovation.api.tool.UtilSteamTool.java.
 */
public class SteamToolHelper {

    /**
     * Gets the Harvest Level upgrade installed in the tool. Though it returns an arraylist for
     * ultimate mod compatibility, chances are it will be of size 1 or null.
     * @param me The tool ItemStack.
     * @return null if there is no harvest level modifier, otherwise the ItemStack.
     */
    public static ItemStack getHarvestLevelModifier(ItemStack me) {
        for (int i = 0; i < 10; i++) {
            if (me.getTagCompound().getCompoundTag("upgrades").hasKey(Integer.toString(i))) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(
                  me.getTagCompound().getCompoundTag("upgrades").getCompoundTag(Integer.toString(i)));
                if (stack != null) {
                    Item item = stack.getItem();
                    if (item != null && item instanceof ItemDrillHeadUpgrade) {
                        return stack;
                    }
                }
            }
        }
        return null;
    }
}
