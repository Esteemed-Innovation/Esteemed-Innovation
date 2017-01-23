package eiteam.esteemedinnovation.armor.exosuit.steam;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Map;

public class SteamExosuitItemBakedModel implements IPerspectiveAwareModel {
    public final SteamExosuitItemModel parent;
    public final Map<String, IBakedModel> cache;
    public final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
    private final ImmutableList<BakedQuad> quads;
    private final TextureAtlasSprite particle;
    public final VertexFormat format;

    public SteamExosuitItemBakedModel(SteamExosuitItemModel parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, Map<String, IBakedModel> cache) {
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
    public boolean isGui3d() {
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
    public ItemOverrideList getOverrides() {
        return SteamExosuitItemOverrideList.INSTANCE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, cameraTransformType);
    }
}
