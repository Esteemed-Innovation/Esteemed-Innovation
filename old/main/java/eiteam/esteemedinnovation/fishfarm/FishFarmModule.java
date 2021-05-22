package eiteam.esteemedinnovation.fishfarm;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class FishFarmModule extends ContentModule {
    @Override
    public void create(Side side) {
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.EI_MODID, "FloatingItem"), EntityFloatingItem.class, "FloatingItem", 0, EsteemedInnovation.instance, 64, 20, true);
    }
}
