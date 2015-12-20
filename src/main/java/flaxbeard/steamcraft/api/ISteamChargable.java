package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;

public interface ISteamChargable {
    /**
     * How much steam is used per use
     */
    int steamPerDurability();

    /**
     * Called to ensure the item can be charged with steam devices
     *
     * @param me - The itemstack of the item
     *
     * @return true if the item can charge
     */
    boolean canCharge(ItemStack me);
}
