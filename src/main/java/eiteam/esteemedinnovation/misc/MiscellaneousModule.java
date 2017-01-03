package eiteam.esteemedinnovation.misc;

import eiteam.esteemedinnovation.api.book.BookEntry;
import eiteam.esteemedinnovation.api.book.BookPageItem;
import eiteam.esteemedinnovation.api.book.BookPageRegistry;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types;
import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.BASICS_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static net.minecraft.init.Blocks.PISTON;
import static net.minecraft.init.Items.FLINT_AND_STEEL;

public class MiscellaneousModule extends ContentModule {
    public static Item COMPONENT;

    @Override
    public void create(Side side) {
        COMPONENT = setup(new ItemCraftingComponent(), "crafting");
        registerTileEntity(TileEntityDummyBlock.class, "dummy");
    }

    @Override
    public void recipes(Side side) {
        BookRecipeRegistry.addRecipe("piston1", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.BRASS_PISTON.getMetadata()),
          " x ",
          "xpx",
          " i ",
          'x', INGOT_BRASS,
          'p', PISTON,
          'i', TransportationModule.BRASS_PIPE
        ));
        BookRecipeRegistry.addRecipe("piston2", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.BRASS_PISTON.getMetadata()),
          " x ",
          "xpx",
          " i ",
          'x', PLATE_THIN_BRASS,
          'p', PISTON,
          'i', TransportationModule.BRASS_PIPE
        ));
        BookRecipeRegistry.addRecipe("turbine1", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.BRASS_TURBINE.getMetadata()),
          " x ",
          "xnx",
          " x ",
          'x', INGOT_BRASS,
          'n', NUGGET_BRASS
        ));
        BookRecipeRegistry.addRecipe("turbine2", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.BRASS_TURBINE.getMetadata()),
          " x ",
          "xnx",
          " x ",
          'x', PLATE_THIN_BRASS,
          'n', NUGGET_BRASS
        ));
        BookRecipeRegistry.addRecipe("stock", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.GUN_STOCK.getMetadata()),
          "p  ",
          " p ",
          " pp",
          'p', PLANK_WOOD
        ));
        BookRecipeRegistry.addRecipe("flintlock1", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.FLINTLOCK.getMetadata()),
          "f i",
          "iri",
          'i', INGOT_IRON,
          'r', DUST_REDSTONE,
          'f', FLINT_AND_STEEL
        ));
        BookRecipeRegistry.addRecipe("flintlock2", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.FLINTLOCK.getMetadata()),
          "f i",
          "iri",
          'i', PLATE_THIN_IRON,
          'r', DUST_REDSTONE,
          'f', FLINT_AND_STEEL
        ));
        if (Config.disableMainBarrelRecipe) {
            BookRecipeRegistry.addRecipe("barrel1", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.IRON_BARREL.getMetadata()),
              "i  ",
              " i ",
              "  i",
              'i', INGOT_IRON
            ));
        } else {
            BookRecipeRegistry.addRecipe("barrel1", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.IRON_BARREL.getMetadata()),
              "i  ",
              " i ",
              "  i",
              'i', PLATE_THIN_IRON
            ));
        }
        BookRecipeRegistry.addRecipe("barrel2", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.IRON_BARREL.getMetadata()),
          "i  ",
          " i ",
          "  i",
          'i', PLATE_THIN_IRON
        ));
        BookRecipeRegistry.addRecipe("blunderBarrel1", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.BLUNDERBUSS_BARREL.getMetadata()),
          "i  ",
          " i ",
          "  i",
          'i', INGOT_BRASS
        ));
        BookRecipeRegistry.addRecipe("blunderBarrel2", new ShapedOreRecipe(new ItemStack(COMPONENT, 1, Types.BLUNDERBUSS_BARREL.getMetadata()),
          "i  ",
          " i ",
          "  i",
          'i', PLATE_THIN_BRASS
        ));
    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addEntryToCategory(BASICS_CATEGORY, new BookEntry("research.Bits.name",
          new BookPageItem("research.Bits.name", "research.Bits.0",
            new ItemStack(COMPONENT, 1, Types.BRASS_PISTON.getMetadata()),
            new ItemStack(COMPONENT, 1, Types.BRASS_TURBINE.getMetadata()))));
    }

    @Override
    public void preInitClient() {
        for (ItemCraftingComponent.Types type : ItemCraftingComponent.Types.values()) {
            registerModelItemStack(new ItemStack(COMPONENT, 1, type.getMetadata()));
        }
    }
}
