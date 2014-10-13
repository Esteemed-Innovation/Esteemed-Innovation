package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;

public interface ISteamChargable {
    public int steamPerDurability();

    public boolean canCharge(ItemStack me);
}
