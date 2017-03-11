package eiteam.esteemedinnovation.commons.visual;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class GenericModelLoaderLocationMatch implements ICustomModelLoader {
    private final IModel loadedModel;
    private final ResourceLocation toMatch;

    public GenericModelLoaderLocationMatch(IModel loadedModel, ResourceLocation toMatch) {
        this.loadedModel = loadedModel;
        this.toMatch = toMatch;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.equals(toMatch);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return loadedModel;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}
}
