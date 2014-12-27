package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface ICrucibleMold {
    public ResourceLocation getBlockTexture();

    /**
     * @param liquid - The liquid in the Crucible
     *
     * @return true if the mold can be used
     */
    public boolean canUseOn(CrucibleLiquid liquid);

    /**
     * @param liquid - The liquid in the Crucible
     */
    public ItemStack getItemFromLiquid(CrucibleLiquid liquid);

    /**
     * @param liquid - The liquid in the Crucible
     */
    public int getCostToMold(CrucibleLiquid liquid);
}
