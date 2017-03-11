package eiteam.esteemedinnovation.armor.exosuit.leather;

import com.google.common.collect.ImmutableList;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.UtilPlates;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelState;

import java.util.ArrayList;
import java.util.List;

public class LeatherExosuitItemOverrideList extends ItemOverrideList {
    public static final ItemOverrideList INSTANCE = new LeatherExosuitItemOverrideList();

    public LeatherExosuitItemOverrideList() {
        super(ImmutableList.of());
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        ItemLeatherExosuitArmor armor = (ItemLeatherExosuitArmor) stack.getItem();

        List<ResourceLocation> locations = new ArrayList<>();
        String armorString = armor.getString();
        ResourceLocation baseArmorRL = new ResourceLocation(armorString);
        locations.add(baseArmorRL);
        ExosuitPlate plate = armor.getPlate(stack);
        if (plate != null) {
            locations.add(UtilPlates.getIconFromPlate(plate.getIdentifier(), armor));
        }

        LeatherExosuitItemBakedModel exosuitItemBakedModel = (LeatherExosuitItemBakedModel) originalModel;

        IModel processed = new LeatherExosuitItemModel(locations);

        String cacheKey = String.format("armor=%s,plate=%s", armor.getRegistryName(), plate);

        IBakedModel bakedModel = processed.bake(new SimpleModelState(exosuitItemBakedModel.getTransforms()), exosuitItemBakedModel.getFormat(), ModelLoader.defaultTextureGetter());
        exosuitItemBakedModel.cacheModel(cacheKey, bakedModel);

        return bakedModel;
    }
}
