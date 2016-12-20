package eiteam.esteemedinnovation.tools.steam;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import eiteam.esteemedinnovation.api.tool.ISteamToolUpgrade;
import eiteam.esteemedinnovation.api.tool.ToolUpgradeRegistry;
import eiteam.esteemedinnovation.init.items.tools.ToolUpgradeItems;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.MOD_ID;

public class SteamToolModel implements IModel {
    @Nonnull
    private ResourceLocation core;
    @Nonnull
    private ResourceLocation head;

    public static final IModel GENERIC_MODEL = new SteamToolModel(null, null, 0, "drill");

    public SteamToolModel(@Nullable ResourceLocation core, @Nullable ResourceLocation head, int which, @Nonnull String tool) {
        if (core == null) {
            core = new ResourceLocation(MOD_ID, "items/" + tool + "BaseCore" + which);
        }
        if (head == null) {
            head = new ResourceLocation(MOD_ID, "items/" + tool + "BaseHead" + which);
        }
        this.core = core;
        this.head = head;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        Collection<ResourceLocation> resources = new ArrayList<>();
        resources.add(new ResourceLocation(MOD_ID, "items/axeBaseCore0"));
        resources.add(new ResourceLocation(MOD_ID, "items/axeBaseCore1"));
        resources.add(new ResourceLocation(MOD_ID, "items/axeBaseHead0"));
        resources.add(new ResourceLocation(MOD_ID, "items/axeBaseHead1"));

        resources.add(new ResourceLocation(MOD_ID, "items/drillBaseCore0"));
        resources.add(new ResourceLocation(MOD_ID, "items/drillBaseCore1"));
        resources.add(new ResourceLocation(MOD_ID, "items/drillBaseHead0"));
        resources.add(new ResourceLocation(MOD_ID, "items/drillBaseHead1"));

        resources.add(new ResourceLocation(MOD_ID, "items/shovelBaseCore0"));
        resources.add(new ResourceLocation(MOD_ID, "items/shovelBaseCore1"));
        resources.add(new ResourceLocation(MOD_ID, "items/shovelBaseHead0"));
        resources.add(new ResourceLocation(MOD_ID, "items/shovelBaseHead1"));

        for (ToolUpgradeItems.Items item : ToolUpgradeItems.Items.LOOKUP) {
            Item vItem = item.getItem();
            Collections.addAll(resources, ToolUpgradeRegistry.getResources((ISteamToolUpgrade) vItem));
        }

        return resources;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = IPerspectiveAwareModel.MapWrapper.getTransforms(state);

        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        IBakedModel coreModel = new ItemLayerModel(ImmutableList.of(core, head)).bake(state, format, bakedTextureGetter);
        builder.addAll(coreModel.getQuads(null, null, 0));

        TextureAtlasSprite headTexture = bakedTextureGetter.apply(head);

        return new SteamToolBakedModel(this, builder.build(), headTexture, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap());
    }

    @Override
    public IModelState getDefaultState() {
        return null;
    }
}
