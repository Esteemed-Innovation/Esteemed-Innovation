package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface ICrucibleMold {
    ResourceLocation getBlockTexture();

    /**
     * @param liquid - The liquid in the Crucible
     *
     * @return true if the mold can be used
     */
    boolean canUseOn(CrucibleLiquid liquid);

    /**
     * @param liquid - The liquid in the Crucible
     */
    ItemStack getItemFromLiquid(CrucibleLiquid liquid);

    /**
     * @param liquid - The liquid in the Crucible
     */
    int getCostToMold(CrucibleLiquid liquid);
}
