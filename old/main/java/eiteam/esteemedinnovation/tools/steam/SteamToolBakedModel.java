package eiteam.esteemedinnovation.tools.steam;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import eiteam.esteemedinnovation.commons.visual.GenericPerspectiveAwareModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Map;

public class SteamToolBakedModel extends GenericPerspectiveAwareModel<SteamToolModel> {
    SteamToolBakedModel(SteamToolModel parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, Map<String, IBakedModel> cache) {
        super(parent, quads, particle, format, transforms, cache);
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return SteamToolOverrideList.INSTANCE;
    }
}
