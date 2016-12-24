package eiteam.esteemedinnovation.metalcasting.mold.molds;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.mold.ICrucibleMold;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemIngotMold extends Item implements ICrucibleMold {
    private ResourceLocation icon = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/moldIngot.png");

    public ItemIngotMold() {
        this.maxStackSize = 1;
    }

    @Override
    public ResourceLocation getBlockTexture() {
        return icon;
    }

    @Override
    public boolean canUseOn(CrucibleLiquid liquid) {
        return (liquid.getIngot() != null);
    }

    @Override
    public ItemStack getItemFromLiquid(CrucibleLiquid liquid) {
        ItemStack newStack = liquid.getIngot().copy();
        newStack.stackSize = 1;
        return newStack;
    }

    @Override
    public int getCostToMold(CrucibleLiquid liquid) {
        return 9;
    }
}
