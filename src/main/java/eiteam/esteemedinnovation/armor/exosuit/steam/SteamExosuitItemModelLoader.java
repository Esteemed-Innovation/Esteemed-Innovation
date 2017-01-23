package eiteam.esteemedinnovation.armor.exosuit.steam;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class SteamExosuitItemModelLoader implements ICustomModelLoader {
    private static final ResourceLocation MODEL_NAME = new ResourceLocation(EsteemedInnovation.MOD_ID, "models/block/steam_exosuit_piece");

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return MODEL_NAME.equals(modelLocation);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return SteamExosuitItemModel.GENERIC_MODEL;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}
}