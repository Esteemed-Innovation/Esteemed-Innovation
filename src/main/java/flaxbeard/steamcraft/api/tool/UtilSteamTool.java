package flaxbeard.steamcraft.api.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class UtilSteamTool {
    /**
     * Checks if the drill has a particular upgrade.
     * @param me The ItemStack version of the drill
     * @param check The item that is being checked against, or the upgrade
     * @return Whether it has any upgrades.
     */
    public static boolean hasUpgrade(ItemStack me, Item check) {
        if (check == null) {
            return false;
        }

        if (me.hasTagCompound() && me.stackTagCompound.hasKey("upgrades")) {
            for (int i = 1; i < 10; i++) {
                if (me.stackTagCompound.getCompoundTag("upgrades").hasKey(Integer.toString(i))) {
                    ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("upgrades").getCompoundTag(Integer.toString(i)));
                    if (stack.getItem() == check) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<ISteamToolUpgrade> getUpgrades(ItemStack me) {
        ArrayList<ISteamToolUpgrade> upgrades = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if (me.stackTagCompound.getCompoundTag("upgrades").hasKey(Integer.toString(i))) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(
                  me.stackTagCompound.getCompoundTag("upgrades")
                    .getCompoundTag(Integer.toString(i)));
                if (stack != null) {
                    Item item = stack.getItem();
                    if (item != null && item instanceof ISteamToolUpgrade) {
                        upgrades.add((ISteamToolUpgrade) item);
                    }
                }
            }
        }

        if (upgrades.isEmpty()) {
            return null;
        }

        return upgrades;
    }
}
