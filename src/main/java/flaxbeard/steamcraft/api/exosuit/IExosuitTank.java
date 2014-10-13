package flaxbeard.steamcraft.api.exosuit;

import net.minecraft.item.ItemStack;

public interface IExosuitTank {
    public boolean canFill(ItemStack stack);

    public int getStorage(ItemStack stack);
}
