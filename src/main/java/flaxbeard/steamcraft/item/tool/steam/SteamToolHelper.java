package flaxbeard.steamcraft.item.tool.steam;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.tuple.MutablePair;
import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;

import java.util.ArrayList;

/**
 * Class for internal use in the steam tools. For utils relating to the steam tool, see
 * flaxbeard.steamcraft.api.tool.UtilSteamTool.java.
 */
public class SteamToolHelper {
    @SuppressWarnings("unchecked")
    public static final MutablePair<Integer, Integer>[] ENGINEER_COORDINATES = new MutablePair[]{
      MutablePair.of(60, 12),
      MutablePair.of(37, 40)
    };

    /**
     * Gets an ArrayList of the Strings that should be put in the item's tooltip.
     * @param upgrades The ISteamToolUpgrades that are being tested against; see #getUpgrades
     * @param redSlot The slot that should be red; drillHead, sawHead, or shovelHead usually.
     * @return The strings, or null if the upgrades were null.
     */
    public static ArrayList<String> getInformation(ArrayList<ISteamToolUpgrade> upgrades, SteamToolSlot redSlot) {
        if (upgrades == null) {
            return null;
        }

        ArrayList<String> strings = new ArrayList<>();

        for (ISteamToolUpgrade upgrade : upgrades) {
            String info = upgrade.getInformation();
            if (info == null) {
                info = StatCollector.translateToLocal(((Item) upgrade).getUnlocalizedName());
            }
            if (upgrade.getToolSlot() == redSlot) {
                strings.add(EnumChatFormatting.RED + "" + info);
            } else if (upgrade.getToolSlot() == SteamToolSlot.toolCore) {
                strings.add(EnumChatFormatting.DARK_GREEN + "" + info);
            }
        }
        return strings;
    }

    /**
     * Essentially setInventorySlotContents
     * @param me The ItemStack being edited.
     * @param slot ???
     * @param stack ???
     * Note: The original method was not documented, so I don't know what these params actually are.
     */
    public static void setNBTInventory(ItemStack me, int slot, ItemStack stack) {
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.stackTagCompound.hasKey("upgrades")) {
            me.stackTagCompound.setTag("upgrades", new NBTTagCompound());
        }
        if (me.stackTagCompound.getCompoundTag("upgrades").hasKey(Integer.toString(slot))) {
            me.stackTagCompound.getCompoundTag("upgrades").removeTag(Integer.toString(slot));
        }
        NBTTagCompound stc = new NBTTagCompound();
        if (stack != null) {
            stack.writeToNBT(stc);
            me.stackTagCompound.getCompoundTag("upgrades").setTag(Integer.toString(slot), stc);
        }
    }
}
