package eiteam.esteemedinnovation.item.tool.steam;

import eiteam.esteemedinnovation.api.tool.ISteamToolUpgrade;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * Class for internal use in the steam tools. For utils relating to the steam tool, see
 * eiteam.esteemedinnovation.api.tool.UtilSteamTool.java.
 */
public class SteamToolHelper {
    @SuppressWarnings("unchecked")
    public static final Pair<Integer, Integer>[] ENGINEER_COORDINATES = new Pair[] {
      Pair.of(60, 12),
      Pair.of(37, 40)
    };

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
            ISteamToolUpgrade upgrade = (ISteamToolUpgrade) stack.getItem();
            TextFormatting format = upgrade.getToolSlot() == redSlot ? TextFormatting.RED : TextFormatting.DARK_GREEN;
            String info = upgrade.getInformation(stack, tool);
            String toAdd = info == null ? stack.getItem().getUnlocalizedName() + ".name" : info;
            strings.add(format + "" + I18n.format(toAdd));
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
}
