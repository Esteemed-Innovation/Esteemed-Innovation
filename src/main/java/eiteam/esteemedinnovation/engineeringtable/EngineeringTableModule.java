package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.OreDictEntries;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class EngineeringTableModule extends ContentModule {
    public static Block ENGINEERING_TABLE;

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
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enableEngineering) {
            BookRecipeRegistry.addRecipe("engineering", new ShapedOreRecipe(ENGINEERING_TABLE,
              "xzx",
              "x x",
              "xxx",
              'x', OreDictEntries.COBBLESTONE_ORE,
              'z', OreDictEntries.PLATE_THIN_IRON
            ));
        }
        */
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        registerModel(ENGINEERING_TABLE);
    }
}
