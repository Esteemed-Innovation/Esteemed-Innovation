package eiteam.esteemedinnovation.commons.visual;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Map;

/**
 * A base standard implementation of {@link IPerspectiveAwareModel}. It does not implement certain subclass-specific
 * ones like {@link IPerspectiveAwareModel#isGui3d()} and {@link IPerspectiveAwareModel#getOverrides()}.
 * @param <MODEL> The model type that this baked model is for.
 */
public abstract class GenericPerspectiveAwareModel<MODEL extends IModel> implements IPerspectiveAwareModel {
    private final MODEL parent;
    private final Map<String, IBakedModel> cache;
    private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
    private final ImmutableList<BakedQuad> quads;
    private final TextureAtlasSprite particle;
    private final VertexFormat format;

    public GenericPerspectiveAwareModel(MODEL parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, Map<String, IBakedModel> cache) {
        this.quads = quads;
        this.particle = particle;
        this.format = format;
        this.parent = parent;
        this.transforms = transforms;
        this.cache = cache;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particle;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, cameraTransformType);
    }

    public boolean isCached(String mapString) {
        return cache.containsKey(mapString);
    }

    public IBakedModel getCachedModel(String key) {
        return cache.get(key);
    }

    public void cacheModel(String key, IBakedModel model) {
        cache.put(key, model);
    }

    public ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms() {
        return transforms;
    }

    public VertexFormat getFormat() {
        return format;
    }
}
