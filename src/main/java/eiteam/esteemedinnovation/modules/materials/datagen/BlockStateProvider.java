package eiteam.esteemedinnovation.modules.materials.datagen;

import eiteam.esteemedinnovation.base.EsteemedInnovation;
import eiteam.esteemedinnovation.modules.materials.ClassSensitivePlateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.base.ModNames.GILDED_IRON;
import static eiteam.esteemedinnovation.base.ModNames.Suffix;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    
    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, EsteemedInnovation.MODID, exFileHelper);
    }
    
    @Override
    protected void registerStatesAndModels() {
        ForgeRegistries.BLOCKS.getEntries().stream().filter(e -> EsteemedInnovation.MODID.equals(e.getKey().getNamespace()))
          .forEach(e -> {
              String name = e.getKey().getPath();
              Block b = e.getValue();
              if (name.endsWith(Suffix.PRESSURE_PLATE)) {
                  ModelFile onFile = models().getBuilder(name).parent(models().getExistingFile(mcLoc("block/pressure_plate_up")))
                    .texture("texture", modLoc("block/" + name.replace(Suffix.PRESSURE_PLATE, Suffix.BLOCK)));
                  ModelFile offFile = models().getBuilder(name + "_down").parent(models().getExistingFile(mcLoc("block/pressure_plate_down")))
                    .texture("texture", modLoc("block/" + name.replace(Suffix.PRESSURE_PLATE, Suffix.BLOCK)));
                  if (name.startsWith(GILDED_IRON)) {
                      getVariantBuilder(b).partialState().with(WeightedPressurePlateBlock.POWER, 0).setModels(new ConfiguredModel(offFile));
                      for (int i = 1; i <= 15; i++) {
                          getVariantBuilder(b).partialState().with(WeightedPressurePlateBlock.POWER, i).setModels(new ConfiguredModel(onFile));
                      }
                  } else {
                      getVariantBuilder(b).partialState().with(ClassSensitivePlateBlock.POWERED, true).setModels(new ConfiguredModel(onFile));
                      getVariantBuilder(b).partialState().with(ClassSensitivePlateBlock.POWERED, false).setModels(new ConfiguredModel(offFile));
                  }
              } else {
                  simpleBlock(b, models().cubeAll(name, EsteemedInnovation.resourceLocation("block/" + name)));
              }
          });
    }
    
    @Nonnull
    @Override
    public String getName() {
        return "EsteemedInnovation Materials BlockState Provider";
    }
}
