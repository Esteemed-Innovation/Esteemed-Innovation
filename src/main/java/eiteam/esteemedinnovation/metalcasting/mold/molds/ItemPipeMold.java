package eiteam.esteemedinnovation.metalcasting.mold.molds;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.mold.CrucibleMold;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static eiteam.esteemedinnovation.metalcasting.MetalcastingModule.INGOT_MOLD;

public class ItemPipeMold extends Item implements CrucibleMold {
    @Override
    public ResourceLocation getBlockTexture() {
        // TODO: Proper texture for this mold
        return ((CrucibleMold) INGOT_MOLD).getBlockTexture();
    }

    @Override
    public boolean canUseOn(CrucibleLiquid liquid) {
        return liquid != null && CrucibleRegistry.pipeLiquids.containsKey(liquid);
    }

    @Override
    public ItemStack getItemFromLiquid(CrucibleLiquid liquid) {
        ItemStack newStack = CrucibleRegistry.pipeLiquids.get(liquid).copy();
        newStack.stackSize = 1;
        return newStack;
    }

    @Override
    public int getCostToMold(CrucibleLiquid liquid) {
        return 54;
    }
}
