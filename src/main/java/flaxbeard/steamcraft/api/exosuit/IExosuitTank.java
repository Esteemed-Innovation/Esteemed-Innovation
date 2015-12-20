package flaxbeard.steamcraft.api.exosuit;

import net.minecraft.item.ItemStack;

public interface IExosuitTank {
    boolean canFill(ItemStack stack);

    int getStorage(ItemStack stack);
}
