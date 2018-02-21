package eiteam.esteemedinnovation.buzzsaw;

import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;

public class BuzzsawModule extends ContentModule implements ConfigurableModule {
    public static Block BUZZSAW;
    static boolean enableSaw;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        BUZZSAW = setup(event, new BlockSaw(), "saw");
        registerTileEntity(TileEntitySaw.class, "saw");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, BUZZSAW);
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enableSaw) {
            BookRecipeRegistry.addRecipe("saw", new ShapedOreRecipe(BUZZSAW,
              "btb",
              "p p",
              "mmm",
              'b', TIMBER_CHAIN,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'm', PLATE_THIN_BRASS
            ));
        }*/
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableSaw = config.get(CATEGORY_BLOCKS, "Enable the Buzzsaw", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return "enableSaw".equals(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return enableSaw;
    }
}
