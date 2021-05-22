package eiteam.esteemedinnovation.armor.exosuit.leather;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import eiteam.esteemedinnovation.api.exosuit.ExosuitRegistry;
import eiteam.esteemedinnovation.armor.ArmorModule;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LeatherExosuitItemModel implements IModel {
    public static final IModel GENERIC_MODEL = new LeatherExosuitItemModel(new ArrayList<>());

    private List<ResourceLocation> locations;

    /**
     * @param locations The list of all resource locations for the model. If it is empty, it *must* be mutable. If it
     *                  has at least one item, it can be immutable. It would be easier to just always use mutable
     *                  lists for this.
     */
    public LeatherExosuitItemModel(List<ResourceLocation> locations) {
        // Headpiece fallback. The list can never be empty because it is used in #bake.
        if (locations.isEmpty()) {
            locations.add((ArmorModule.LEATHER_EXO_HEAD).getItemIconResource());
        }
        this.locations = locations;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        List<? extends ItemLeatherExosuitArmor> armors = Arrays.asList(
          ArmorModule.LEATHER_EXO_BOOTS,
          ArmorModule.LEATHER_EXO_CHEST,
          ArmorModule.LEATHER_EXO_HEAD,
          ArmorModule.LEATHER_EXO_LEGS);
        Collection<ResourceLocation> allPlateIcons = new ArrayList<>();
        Collection<ResourceLocation> allArmorIcons = new ArrayList<>();
        for (ItemLeatherExosuitArmor armor : armors) {
            allPlateIcons.addAll(
              ExosuitRegistry.plates.values().stream()
                .map(p -> p.getIcon(armor))
                .collect(Collectors.toList())
            );
            allArmorIcons.add(armor.getItemIconResource());
        }
        Collection<ResourceLocation> allIcons = new ArrayList<>();
        allIcons.addAll(allPlateIcons);
        allIcons.addAll(allArmorIcons);
        return allIcons;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);

        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        IBakedModel coreModel = new ItemLayerModel(ImmutableList.copyOf(locations)).bake(state, format, bakedTextureGetter);
        builder.addAll(coreModel.getQuads(null, null, 0));

        TextureAtlasSprite someTexture = bakedTextureGetter.apply(locations.get(0));

        return new LeatherExosuitItemBakedModel(this, builder.build(), someTexture, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap());
    }
}
