package eiteam.esteemedinnovation.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ISteamChargable {
    /**
     * How much steam is used per use
     */
    int steamPerDurability();

    /**
     * Called to ensure the item can be charged with steam devices
     *
     * @param me The itemstack of the item
     *
     * @return true if the item can charge
     */
    boolean canCharge(ItemStack me);

    /**
     * Adds an amount of steam to the item.
     * @param me The ItemStack
     * @param amount How much steam to add.
     * @param player The player
     * @return Whether it was a successful add.
     */
    boolean addSteam(ItemStack me, int amount, EntityPlayer player);
}
