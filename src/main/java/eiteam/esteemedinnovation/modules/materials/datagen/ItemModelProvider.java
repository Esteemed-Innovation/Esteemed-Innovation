package eiteam.esteemedinnovation.modules.materials.datagen;

import eiteam.esteemedinnovation.base.EsteemedInnovation;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    
    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, EsteemedInnovation.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        ForgeRegistries.ITEMS.getEntries().stream().filter(e -> EsteemedInnovation.MODID.equals(e.getKey().getRegistryName().getNamespace()))
          .forEach(e -> {
              String name = e.getKey().getRegistryName().getPath();
              Item i = e.getValue();
              
              if (i instanceof BlockItem) {
                  getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)));
              } else {
                  getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", modLoc("item/" + name));
              }
          });
    }
    
    @Override
    public String getName() {
        return "EsteemedInnovation Materials Item Model Provider";
    }
}
