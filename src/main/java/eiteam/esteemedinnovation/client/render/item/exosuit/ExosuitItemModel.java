package eiteam.esteemedinnovation.client.render.item.exosuit;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import eiteam.esteemedinnovation.api.exosuit.ExosuitRegistry;
import eiteam.esteemedinnovation.api.exosuit.IExosuitUpgrade;
import eiteam.esteemedinnovation.init.items.armor.ArmorItems;
import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExosuitItemModel implements IModel {
    public static final IModel GENERIC_MODEL = new ExosuitItemModel(new ArrayList<>());

    private List<ResourceLocation> locations;

    /**
     * @param locations The list of all resource locations for the model. If it is empty, it *must* be mutable. If it
     *                  has at least one item, it can be immutable. It would be easier to just always use mutable
     *                  lists for this.
     */
    public ExosuitItemModel(List<ResourceLocation> locations) {
        // Headpiece fallback. The list can never be empty because it is used in #bake.
        if (locations.isEmpty()) {
            locations.add(new ResourceLocation(((ItemExosuitArmor) ArmorItems.Items.EXOSUIT_HEADPIECE.getItem()).getString()));
        }
        this.locations = locations;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        Collection<ItemExosuitArmor> armors = new ArrayList<>();
        for (ArmorItems.Items enumItem : ArmorItems.Items.LOOKUP) {
            Item item = enumItem.getItem();
            if (item instanceof ItemExosuitArmor) {
                armors.add((ItemExosuitArmor) item);
            }
        }
        Collection<ResourceLocation> allPlateIcons = new ArrayList<>();
        Collection<ResourceLocation> allArmorIcons = new ArrayList<>();
        for (ItemExosuitArmor armor : armors) {
            allPlateIcons.addAll(
              ExosuitRegistry.plates.values().stream()
                .map(p -> p.getIcon(armor))
                .collect(Collectors.toList())
            );
            String baseIcon = armor.getString();
            allArmorIcons.add(new ResourceLocation(baseIcon));
            allArmorIcons.add(new ResourceLocation(baseIcon + "_grey"));
        }
        List<ResourceLocation> allIcons = ExosuitRegistry.upgrades.stream()
          .map(IExosuitUpgrade::getOverlay)
          .collect(Collectors.toList());
        allIcons.addAll(allPlateIcons);
        allIcons.addAll(allArmorIcons);
        return allIcons;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = IPerspectiveAwareModel.MapWrapper.getTransforms(state);

        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        IBakedModel coreModel = new ItemLayerModel(ImmutableList.copyOf(locations)).bake(state, format, bakedTextureGetter);
        builder.addAll(coreModel.getQuads(null, null, 0));

        TextureAtlasSprite someTexture = bakedTextureGetter.apply(locations.get(0));

        return new ExosuitItemBakedModel(this, builder.build(), someTexture, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap());
    }

    @Override
    public IModelState getDefaultState() {
        return null;
    }
}
