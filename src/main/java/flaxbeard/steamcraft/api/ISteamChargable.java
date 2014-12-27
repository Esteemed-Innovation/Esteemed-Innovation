package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;

public interface ISteamChargable {
    /**
     * How much steam is used per use
     */
    public int steamPerDurability();

    /**
     * Called to ensure the item can be charged with steam devices
     *
     * @param me - The itemstack of the item
     *
     * @return true if the item can charge
     */
    public boolean canCharge(ItemStack me);
}
