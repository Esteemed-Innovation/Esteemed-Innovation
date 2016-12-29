package eiteam.esteemedinnovation.api.mold;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface CrucibleMold {
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
