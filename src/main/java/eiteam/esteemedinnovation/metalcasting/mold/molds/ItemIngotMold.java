package eiteam.esteemedinnovation.metalcasting.mold.molds;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.mold.CrucibleMold;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemIngotMold extends Item implements CrucibleMold {
    private ResourceLocation icon = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/moldIngot.png");

    public ItemIngotMold() {
        this.maxStackSize = 1;
    }

    @Override
    public ResourceLocation getBlockTexture(ItemStack moldStack) {
        return icon;
    }

    @Override
    public int getCostToMold(CrucibleLiquid liquid, ItemStack moldStack) {
        return 9;
    }
}
