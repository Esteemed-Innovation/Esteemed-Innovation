package eiteam.esteemedinnovation.api.mold;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface CrucibleMold {
    ResourceLocation getBlockTexture();

    /**
     * @param liquid - The liquid in the Crucible
     *
     * @return true if the mold can be used
     */
    default boolean canUseOn(CrucibleLiquid liquid) {
        return getItemFromLiquid(liquid) != null;
    }

    /**
     * @param liquid - The liquid in the Crucible
     */
    default ItemStack getItemFromLiquid(CrucibleLiquid liquid) {
        return CrucibleRegistry.getMoldingOutput(liquid, (Item) this);
    }

    /**
     * @param liquid - The liquid in the Crucible
     */
    int getCostToMold(CrucibleLiquid liquid);
}
