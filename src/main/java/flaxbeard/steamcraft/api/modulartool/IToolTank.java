package flaxbeard.steamcraft.api.modulartool;

import net.minecraft.item.ItemStack;

/**
 * Created by elijahfoster-wysocki on 10/25/14.
 */
public interface IToolTank {
    public boolean canFill(ItemStack stack);

    public int getStorage(ItemStack stack);
}
