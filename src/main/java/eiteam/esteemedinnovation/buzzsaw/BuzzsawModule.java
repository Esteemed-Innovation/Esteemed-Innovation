package eiteam.esteemedinnovation.buzzsaw;

import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.OreDictEntries.PLATE_THIN_BRASS;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_TURBINE;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.tools.ToolsModule.TIMBER_CHAIN;

public class BuzzsawModule extends ContentModule {
    public static Block BUZZSAW;

    @Override
    public void create(Side side) {
        BUZZSAW = setup(new BlockSaw(), "saw");
        registerTileEntity(TileEntitySaw.class, "saw");
    }

    @Override
    public void recipes(Side side) {
        if (Config.enableSaw) {
            BookRecipeRegistry.addRecipe("saw", new ShapedOreRecipe(BUZZSAW,
              "btb",
              "p p",
              "mmm",
              'b', TIMBER_CHAIN,
              't', new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata()),
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'm', PLATE_THIN_BRASS
            ));
        }
    }
}
