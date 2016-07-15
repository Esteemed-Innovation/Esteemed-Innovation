package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Used for items that use CCLib's IItemRenderer and require custom icon rendering behaviors.
 */
public interface IRenderItem {
    /**
     * Returns the desired ResourceLocation for the ItemStack's icon
     * @param self The ItemStack
     * @return The ResourceLocation, Null fallback.
     */
    @Nullable
    ResourceLocation getIcon(ItemStack self);

    /**
     * @param self The ItemStack
     * @return The number of render passes for the item.
     */
    int renderPasses(ItemStack self);
}
