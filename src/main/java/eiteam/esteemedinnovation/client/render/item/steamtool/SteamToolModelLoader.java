package eiteam.esteemedinnovation.client.render.item.steamtool;

import eiteam.esteemedinnovation.EsteemedInnovation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class SteamToolModelLoader implements ICustomModelLoader {
    private static final ResourceLocation MODEL_NAME = new ResourceLocation(EsteemedInnovation.MOD_ID, "models/block/steam_tool");

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return MODEL_NAME.equals(modelLocation);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return SteamToolModel.GENERIC_MODEL;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}
}
