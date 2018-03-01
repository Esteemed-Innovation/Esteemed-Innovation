package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.commons.OreDictEntries;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;

public class EngineeringTableModule extends ContentModule implements ConfigurableModule {
    public static Block ENGINEERING_TABLE;
    public static boolean enableEngineering;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        ENGINEERING_TABLE = setup(event, new BlockEngineeringTable(), "engineering");
        registerTileEntity(TileEntityEngineeringTable.class, "engineeringTable");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, ENGINEERING_TABLE);
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        if (enableEngineering) {
            addRecipe(event, true, "engineering", ENGINEERING_TABLE,
              "xzx",
              "x x",
              "xxx",
              'x', OreDictEntries.COBBLESTONE_ORE,
              'z', OreDictEntries.PLATE_THIN_IRON
            );
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(ENGINEERING_TABLE);
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableEngineering = config.get(CATEGORY_BLOCKS, "Enable Engineering Table", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return "enableEngineering".equals(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return enableEngineering;
    }
}
