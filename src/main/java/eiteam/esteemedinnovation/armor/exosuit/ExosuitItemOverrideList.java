package eiteam.esteemedinnovation.armor.exosuit;

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

public class ExosuitItemOverrideList extends ItemOverrideList {
    public static final ItemOverrideList INSTANCE = new ExosuitItemOverrideList();

    public ExosuitItemOverrideList() {
        super(ImmutableList.of());
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        ItemExosuitArmor armor = (ItemExosuitArmor) stack.getItem();

        boolean isArmorDyed = false;

        ItemStack vanityStack = armor.getStackInSlot(stack, 2);
        if (vanityStack != null) {
            for (int id : OreDictionary.getOreIDs(vanityStack)) {
                String str = OreDictionary.getOreName(id);
                if (ModelExosuit.findDyeStringFromOreDict(str) != null) {
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
        String armorString = armor.getString();
        ResourceLocation baseArmorRL = new ResourceLocation(armorString);
        locations.add(baseArmorRL);
        if (isArmorDyed) {
            locations.add(new ResourceLocation(armorString + "_grey"));
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

        ExosuitItemBakedModel exosuitItemBakedModel = (ExosuitItemBakedModel) originalModel;

        IModel processed = new ExosuitItemModel(locations);

        String cacheKey = String.format("armor=%s,plate=%s,isDyed=%b", armor.getRegistryName(), plate, isArmorDyed);

        IBakedModel bakedModel = processed.bake(new SimpleModelState(exosuitItemBakedModel.transforms), exosuitItemBakedModel.format, ModelLoader.defaultTextureGetter());
        exosuitItemBakedModel.cache.put(cacheKey, bakedModel);

        return bakedModel;
    }
}
