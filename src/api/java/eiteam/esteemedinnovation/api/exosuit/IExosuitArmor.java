package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.api.IEngineerable;
import eiteam.esteemedinnovation.api.ISteamChargable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ISpecialArmor;

public interface IExosuitArmor extends ISpecialArmor, IEngineerable, ISteamChargable {
    String getString();

    /**
     * Checks whether the ItemStack has the amount of power in its steam storage. The opposite of
     * @param me The ItemStack
     * @param powerNeeded The amount of power needed
     * @return True if it has power, false if it doesn't.
     * @see #needsPower(ItemStack, int)
     */
    boolean hasPower(ItemStack me, int powerNeeded);

    /**
     * Checks whether the ItemStack can have the amount of power added to its steam storage.
     * @param me The ItemStack
     * @param powerNeeded The amount of power to add
     * @return True if it will not exceed the limit with this amount of power added to it, false if
     *         it will, or if it is not a chestplate.
     */
    boolean needsPower(ItemStack me, int powerNeeded);

    default void drainSteam(ItemStack me, int amountToDrain) {
        if (me != null) {
            if (me.getTagCompound() == null) {
                me.setTagCompound(new NBTTagCompound());
            }
            if (!me.getTagCompound().hasKey("steamFill")) {
                me.getTagCompound().setInteger("steamFill", 0);
            }
            int fill = me.getTagCompound().getInteger("steamFill");
            fill = Math.max(0, fill - amountToDrain);
            me.getTagCompound().setInteger("steamFill", fill);
        }
    }
}
