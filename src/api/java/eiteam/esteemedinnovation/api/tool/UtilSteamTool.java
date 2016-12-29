package eiteam.esteemedinnovation.api.tool;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;

// TODO: Use correct ID ranges (1-10? we only have 2 upgrades for each tool). Perhaps instead of using an NBTTagList we
// could simply have CoreUpgrade and HeadUpgrade NBT tags.
// TODO: Return pairs instead of lists.
public class UtilSteamTool {
    @SuppressWarnings("unchecked")
    public static final Pair<Integer, Integer>[] ENGINEER_COORDINATES = new Pair[] {
      Pair.of(60, 12),
      Pair.of(37, 40)
    };

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
     * Gets all of the upgrades (except non-standard ones that do not implement SteamToolUpgrade)
     * that are installed in the tool
     * @param me The tool ItemStack.
     * @return The ArrayList of all the upgrades. This can be empty. Expect emptiness.
     */
    public static ArrayList<SteamToolUpgrade> getUpgrades(ItemStack me) {
        ArrayList<SteamToolUpgrade> upgrades = new ArrayList<>();
        if (!me.hasTagCompound() || !me.getTagCompound().hasKey("upgrades")) {
            return upgrades;
        }

        NBTTagCompound unbt = me.getTagCompound().getCompoundTag("upgrades");

        for (int i = 1; i < 10; i++) {
            if (unbt.hasKey(Integer.toString(i))) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(unbt.getCompoundTag(Integer.toString(i)));
                if (stack != null) {
                    Item item = stack.getItem();
                    if (item != null && item instanceof SteamToolUpgrade) {
                        upgrades.add((SteamToolUpgrade) item);
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
                    if (item != null && item instanceof SteamToolUpgrade) {
                        upgrades.add(stack);
                    }
                }
            }
        }

        return upgrades;
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
        if (!me.getTagCompound().hasKey("upgrades")) {
            me.getTagCompound().setTag("upgrades", new NBTTagCompound());
        }
        if (me.getTagCompound().getCompoundTag("upgrades").hasKey(Integer.toString(slot))) {
            me.getTagCompound().getCompoundTag("upgrades").removeTag(Integer.toString(slot));
        }
        NBTTagCompound stc = new NBTTagCompound();
        if (stack != null) {
            stack.writeToNBT(stc);
            me.getTagCompound().getCompoundTag("upgrades").setTag(Integer.toString(slot), stc);
        }
    }

    /**
     * Checks if the ItemStack has the Speed and Ticks NBT tags. If it doesn't, it creates them
     * and sets them to 0.
     * @param me The ItemStack of the tool
     * @return The NBTTagCompound of the tool.
     */
    public static NBTTagCompound checkNBT(ItemStack me) {
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.getTagCompound().hasKey("Speed")) {
            me.getTagCompound().setInteger("Speed", 0);
        }
        if (!me.getTagCompound().hasKey("Ticks")) {
            me.getTagCompound().setInteger("Ticks", 0);
        }
        return me.getTagCompound();
    }

    /**
     * The same as getInformation(ArrayList, SteamToolSlot), but relies on itemstacks instead of ISteamToolUpgrades
     * @param upgrades The ItemStacks that are being tested against; see #getUpgradeStacks
     * @param redSlot The slot that should be red. See getInformation.
     * @return The strings. Will return an empty array if there are no upgrades or strings.
     */
    @Nonnull
    public static ArrayList<String> getInformationFromStacks(ArrayList<ItemStack> upgrades, SteamToolSlot redSlot, ItemStack tool) {
        if (upgrades == null) {
            return new ArrayList<>();
        }

        ArrayList<String> strings = new ArrayList<>();

        for (ItemStack stack : upgrades) {
            SteamToolUpgrade upgrade = (SteamToolUpgrade) stack.getItem();
            TextFormatting format = upgrade.getToolSlot() == redSlot ? TextFormatting.RED : TextFormatting.DARK_GREEN;
            String info = upgrade.getInformation(stack, tool);
            String toAdd = info == null ? stack.getItem().getUnlocalizedName() + ".name" : info;
            strings.add(format + "" + I18n.format(toAdd));
        }

        return strings;
    }
}
