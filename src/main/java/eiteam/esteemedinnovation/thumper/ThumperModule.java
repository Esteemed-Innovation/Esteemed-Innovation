package eiteam.esteemedinnovation.thumper;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;

public class ThumperModule extends ContentModule {
    public static Block THUMPER;
    public static Block THUMPER_DUMMY;

    @Override
    public void create(Side side) {
        THUMPER = setup(new BlockThumper(), "thumper");
        THUMPER_DUMMY = setup(new BlockThumperDummy(), "thumper_dummy", (CreativeTabs) null);
        registerTileEntity(TileEntityThumper.class, "thumper");
    }

    @Override
    public void recipes(Side side) {
        if (Config.enableThumper) {
            BookRecipeRegistry.addRecipe("thumper1", new ShapedOreRecipe(THUMPER,
              "pbp",
              "ebe",
              "xix",
              'i', BLOCK_IRON,
              'b', BLOCK_BRASS,
              'e', BRASS_PIPE,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'x', PLATE_THIN_BRASS
            ));
            BookRecipeRegistry.addRecipe("thumper2", new ShapedOreRecipe(THUMPER,
              "pbp",
              "ebe",
              "xix",
              'i', BLOCK_IRON,
              'b', BLOCK_BRASS,
              'e', BRASS_PIPE,
              'p', new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata()),
              'x', INGOT_BRASS
            ));
        }
    }

    @Override
    public void finish(Side side) {
        if (Config.enableThumper) {
            BookPageRegistry.addEntryToCategory(STEAMPOWER_CATEGORY, new BookEntry("research.Thumper.name",
              new BookPageItem("research.Thumper.name", "research.Thumper.0", new ItemStack(THUMPER)),
              new BookPageText("research.Thumper.name", "research.Thumper.1"),
              new BookPageCrafting("", "thumper1", "thumper2")));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        registerModel(THUMPER);
        registerModel(THUMPER_DUMMY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThumper.class, new TileEntityThumperRenderer());
    }
}
