package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.api.IEngineerable;
import eiteam.esteemedinnovation.api.ISteamChargable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ISpecialArmor;

public interface IExosuitArmor extends ISpecialArmor, IEngineerable, ISteamChargable {
    /**
     * TODO Return a ResourceLocation and rename to a more descriptive name.
     * @return The string representing the location for the armor's item texture.
     */
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

    /**
     * Drains the provided amount of steam from the armor.
     * @param me The armor piece to drain from.
     * @param amountToDrain The amount of steam to drain from the armor.
     */
    void drainSteam(ItemStack me, int amountToDrain);

    /**
     * @param me The armor piece to check in.
     * @param check The item upgrade to check for.
     * @return Whether the provided piece of armor has the provided exosuit upgrade. Should return false if either are null.
     */
    boolean hasUpgrade(ItemStack me, Item check);
}
