package eiteam.esteemedinnovation.thumper;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;

public class ThumperModule extends ContentModule implements ConfigurableModule {
    public static Block THUMPER;
    public static Block THUMPER_DUMMY;
    static boolean enableThumper;
    static boolean dropItem;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        THUMPER = setup(event, new BlockThumper(), "thumper");
        THUMPER_DUMMY = setup(event, new BlockThumperDummy(), "thumper_dummy", (CreativeTabs) null);
        registerTileEntity(TileEntityThumper.class, "thumper");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, THUMPER);
        setupItemBlock(event, THUMPER, (CreativeTabs) null);
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enableThumper) {
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
        */
    }

    @Override
    public void finish(Side side) {
        if (enableThumper) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 13,
              new BookCategory("category.Thumper.name",
                new BookEntry("research.Thumper.name",
                  new BookPageItem("research.Thumper.name", "research.Thumper.0", new ItemStack(THUMPER)),
                  new BookPageText("research.Thumper.name", "research.Thumper.1"),
                  new BookPageCrafting("", "thumper1", "thumper2"))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(THUMPER);
        registerModel(THUMPER_DUMMY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThumper.class, new TileEntityThumperRenderer());
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        dropItem = config.get(Config.CATEGORY_MACHINES, "Thumper drops items (may lag servers)", true).getBoolean();
        enableThumper = config.get(CATEGORY_BLOCKS, "Enable Thumper", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return "enableThumper".equals(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return enableThumper;
    }
}
