package eiteam.esteemedinnovation.metalcasting.mold.molds;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.mold.CrucibleMold;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static eiteam.esteemedinnovation.metalcasting.MetalcastingModule.INGOT_MOLD;

public class ItemPipeMold extends Item implements CrucibleMold {
    @Override
    public ResourceLocation getBlockTexture(ItemStack moldStack) {
        // TODO: Proper texture for this mold
        return ((CrucibleMold) INGOT_MOLD).getBlockTexture(moldStack);
    }

    @Override
    public int getCostToMold(CrucibleLiquid liquid, ItemStack moldStack) {
        return 54;
    }
}
