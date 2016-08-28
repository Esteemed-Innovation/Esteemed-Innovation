package eiteam.esteemedinnovation.api.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import eiteam.esteemedinnovation.item.tool.steam.ItemDrillHeadUpgrade;

import java.util.ArrayList;

public class UtilSteamTool {
    /**
     * Checks if the ItemStack has a particular upgrade. Note that you can also call directly on
     * the ISteamTool item rather than this. This is only used internally by EI for the actual
     * ISteamTool#hasUpgrade(ItemStack, Item) overrides in the steam tool classes.
     * @param me The ItemStack version of the drill
     * @param check The item that is being checked against, or the upgrade
     * @return Whether it has any upgrades.
     */
    public static boolean hasUpgrade(ItemStack me, Item check) {
        if (check == null) {
            return false;
        }

        if (me.hasTagCompound() && me.getTagCompound().hasKey("upgrades")) {
            for (int i = 1; i < 10; i++) {
                if (me.getTagCompound().getCompoundTag("upgrades").hasKey(Integer.toString(i))) {
                    ItemStack stack = ItemStack.loadItemStackFromNBT(me.getTagCompound().getCompoundTag("upgrades").getCompoundTag(Integer.toString(i)));
                    if (stack.getItem() == check) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets all of the upgrades (except non-standard ones that do not implement ISteamToolUpgrade)
     * that are installed in the tool
     * @param me The tool ItemStack.
     * @return The ArrayList of all the upgrades. This can be empty. Expect emptiness.
     */
    public static ArrayList<ISteamToolUpgrade> getUpgrades(ItemStack me) {
        ArrayList<ISteamToolUpgrade> upgrades = new ArrayList<>();
        if (!me.hasTagCompound() || !me.getTagCompound().hasKey("upgrades")) {
            return upgrades;
        }

        NBTTagCompound unbt = me.getTagCompound().getCompoundTag("upgrades");

        for (int i = 1; i < 10; i++) {
            if (unbt.hasKey(Integer.toString(i))) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(unbt.getCompoundTag(Integer.toString(i)));
                if (stack != null) {
                    Item item = stack.getItem();
                    if (item != null && item instanceof ISteamToolUpgrade) {
                        upgrades.add((ISteamToolUpgrade) item);
                    }
                }
            }
        }

        return upgrades;
    }

    /**
     * Exactly like getUpgrades, but obtains ItemStacks instead of ISteamToolUpgrades.
     * @param self The ItemStack of the tool
     * @return An ArrayList of all the upgrade ItemStacks.
     */
    public static ArrayList<ItemStack> getUpgradeStacks(ItemStack self) {
        ArrayList<ItemStack> upgrades = new ArrayList<>();
        if (!self.hasTagCompound() || !self.getTagCompound().hasKey("upgrades")) {
            return upgrades;
        }

        NBTTagCompound unbt = self.getTagCompound().getCompoundTag("upgrades");
        for (int i = 0; i < 10; i++) {
            if (unbt.hasKey(Integer.toString(i))) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(unbt.getCompoundTag(Integer.toString(i)));
                if (stack != null) {
                    Item item = stack.getItem();
                    if (item != null && item instanceof ISteamToolUpgrade) {
                        upgrades.add(stack);
                    }
                }
            }
        }

        return upgrades;
    }

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
