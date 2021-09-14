package eiteam.esteemedinnovation.base.module;

import eiteam.esteemedinnovation.base.EsteemedInnovation;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Module {
    
    private String name;
    
    public Module(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setup(final FMLCommonSetupEvent event) {
    }
    
    public void setupClient(final FMLClientSetupEvent event) {
    }
    
    public <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name) {
        reg.register(thing.setRegistryName(name));
    }
    
    public <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
        register(reg, thing, new ResourceLocation(EsteemedInnovation.MODID, name));
    }
    
    public Item.Properties defaultItemProp() {
        return new Item.Properties().group(EsteemedInnovation.ITEM_GROUP);
    }
    
    public Item.Properties unstackableItemProp() {
        return defaultItemProp().maxStackSize(1);
    }
    
    
    public IDataProvider[] getDataProviders(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        return new IDataProvider[0];
    }
}
