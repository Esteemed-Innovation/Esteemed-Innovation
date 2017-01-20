package eiteam.esteemedinnovation.metalcasting.mold.molds;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.mold.CrucibleMold;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemPlateMold extends Item implements CrucibleMold {
    private ResourceLocation icon = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/moldPlate.png");

    public ItemPlateMold() {
        this.maxStackSize = 1;
    }

    @Override
    public ResourceLocation getBlockTexture() {
        return icon;
    }

    @Override
    public int getCostToMold(CrucibleLiquid liquid) {
        return 6;
    }
}
