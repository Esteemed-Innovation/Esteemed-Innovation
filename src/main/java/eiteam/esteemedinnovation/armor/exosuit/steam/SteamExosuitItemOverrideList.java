package eiteam.esteemedinnovation.armor.exosuit.steam;

import com.google.common.collect.ImmutableList;
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
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class SteamExosuitItemOverrideList extends ItemOverrideList {
    public static final ItemOverrideList INSTANCE = new SteamExosuitItemOverrideList();

    public SteamExosuitItemOverrideList() {
        super(ImmutableList.of());
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        ItemSteamExosuitArmor armor = (ItemSteamExosuitArmor) stack.getItem();

        boolean isArmorDyed = false;

        ItemStack vanityStack = armor.getStackInSlot(stack, 2);
        if (vanityStack != null) {
            for (int id : OreDictionary.getOreIDs(vanityStack)) {
                String str = OreDictionary.getOreName(id);
                if (ModelSteamExosuit.findDyeStringFromOreDict(str) != null) {
                    isArmorDyed = true;
                    break;
                }
            }
        }

        /*
         This basically duplicates the old behavior with render passes. In 1.7 there were 3 render passes for the exo
         armor icons. The first one was always the baseArmorRL. The next one was either the grey or the base, depending
         on if it was dyed. The last one was either the plate texture or the base *again*.
          */
        List<ResourceLocation> locations = new ArrayList<>();
        ResourceLocation baseArmorRL = armor.getItemIconResource();
        locations.add(baseArmorRL);
        if (isArmorDyed) {
            locations.add(new ResourceLocation(baseArmorRL + "_grey"));
        } else {
            locations.add(baseArmorRL);
        }
        String plate = null;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("plate")) {
            plate = stack.getTagCompound().getString("plate");
            locations.add(UtilPlates.getIconFromPlate(plate, armor));
        } else {
            locations.add(baseArmorRL);
        }

        SteamExosuitItemBakedModel exosuitItemBakedModel = (SteamExosuitItemBakedModel) originalModel;

        IModel processed = new SteamExosuitItemModel(locations);

        String cacheKey = String.format("armor=%s,plate=%s,isDyed=%b", armor.getRegistryName(), plate, isArmorDyed);

        IBakedModel bakedModel = processed.bake(new SimpleModelState(exosuitItemBakedModel.getTransforms()), exosuitItemBakedModel.getFormat(), ModelLoader.defaultTextureGetter());
        exosuitItemBakedModel.cacheModel(cacheKey, bakedModel);

        return bakedModel;
    }
}
