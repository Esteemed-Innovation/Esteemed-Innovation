package eiteam.esteemedinnovation.item;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.api.mold.ICrucibleMold;
import eiteam.esteemedinnovation.init.items.MetalcastingItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemPipeMold extends Item implements ICrucibleMold {
    @Override
    public ResourceLocation getBlockTexture() {
        // TODO: Proper texture for this mold
        return ((ICrucibleMold)MetalcastingItems.Items.INGOT_MOLD.getItem()).getBlockTexture();
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
