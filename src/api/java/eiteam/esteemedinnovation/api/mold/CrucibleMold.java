package eiteam.esteemedinnovation.api.mold;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface CrucibleMold {
    /**
     * @param moldStack The ItemStack for this mold item.
     * @return The ResourceLocation for the texture to be overlayed on top of the mold block when this upgrade is in it.
     */
    ResourceLocation getBlockTexture(ItemStack moldStack);

    /**
     * @param moldStack The ItemStack for this mold item.
     * @param liquid The liquid in the Crucible.
     * @return true if the mold can be used.
     */
    default boolean canUseOn(CrucibleLiquid liquid, ItemStack moldStack) {
        return getItemFromLiquid(liquid, moldStack) != null;
    }

    /**
     * @param moldStack The ItemStack for this mold item.
     * @param liquid The liquid in the Crucible.
     * @return The output item when the mold block is opened with this mold and liquid inside of it.
     */
    default ItemStack getItemFromLiquid(CrucibleLiquid liquid, ItemStack moldStack) {
        return CrucibleRegistry.getMoldingOutput(liquid, (Item) this);
    }

    /**
     * @param moldStack The ItemStack for this mold item.
     * @param liquid The liquid in the Crucible.
     * @return The amount of liquid needed to cast metal to this mold.
     */
    int getCostToMold(CrucibleLiquid liquid, ItemStack moldStack);
}
