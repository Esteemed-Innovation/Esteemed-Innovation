package eiteam.esteemedinnovation.converter;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;

public class ConverterModule extends ContentModule {
    public static Block PRESSURE_CONVERTER;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        PRESSURE_CONVERTER = setup(event, new BlockFluidSteamConverter(), "pressure_converter");
        registerTileEntity(TileEntityFluidSteamConverter.class, "fluidSteamConverter");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, PRESSURE_CONVERTER);
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*BookRecipeRegistry.addRecipe("fsc1", new ShapedOreRecipe(PRESSURE_CONVERTER,
          "xlx",
          "gpp",
          "xlx",
          'x', "ingotBrass",
          'l', LEATHER_ORE,
          'p', TransportationModule.BRASS_PIPE,
          'g', PANE_GLASS_COLORLESS
        ));
        BookRecipeRegistry.addRecipe("fsc1", new ShapedOreRecipe(PRESSURE_CONVERTER,
          "xlx",
          "gpp",
          "xlx",
          'x', PLATE_THIN_BRASS,
          'l', LEATHER_ORE,
          'p', TransportationModule.BRASS_PIPE,
          'g', PANE_GLASS_COLORLESS
        ));
        */
    }

    @Override
    public void finish(Side side) {
        if (Config.enableFluidSteamConverter) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 16,
              new BookCategory("category.FSC.name",
                new BookEntry("research.FSC.name",
                  new BookPageItem("research.FSC.name", "research.FSC.0", new ItemStack(PRESSURE_CONVERTER)),
                  new BookPageCrafting("", "fsc1", "fsc2"),
                  new BookPageText("", "research.FSC.1"),
                  new BookPageText("", "research.FSC.2"))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(PRESSURE_CONVERTER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidSteamConverter.class, new TileEntityFluidSteamRenderer());
    }
}
