package eiteam.esteemedinnovation.boiler;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_STEAM_SYSTEM;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.BASICS_SECTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;

public class BoilerModule extends ContentModule implements ConfigurableModule {
    public static Block BOILER;
    public static boolean enableBoiler;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        BOILER = setup(event, new BlockBoiler(), "boiler");
        registerTileEntity(TileEntityBoiler.class, "boiler");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, BOILER);
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enableBoiler) {
            BookRecipeRegistry.addRecipe("boiler1", new ShapedOreRecipe(BOILER,
              "xxx",
              "xfx",
              "xxx",
              'x', INGOT_BRASS,
              'f', FURNACE
            ));
            BookRecipeRegistry.addRecipe("boiler2", new ShapedOreRecipe(BOILER,
              "xxx",
              "xfx",
              "xxx",
              'x', PLATE_THIN_BRASS,
              'f', FURNACE
            ));
        }
        */
    }

    @Override
    public void finish(Side side) {
        BookPageRegistry.addCategoryToSection(BASICS_SECTION, 3,
          new BookCategory("category.SteamSystem.name",
            new BookEntry("research.SteamSystem.name",
              new BookPageItem("research.SteamSystem.name", "research.SteamSystem.0", new ItemStack(BOILER), new ItemStack(BRASS_PIPE)),
              new BookPageText("research.SteamSystem.name", "research.SteamSystem.1"),
              new BookPageText("research.SteamSystem.name", "research.SteamSystem.2"),
              new BookPageText("research.SteamSystem.name", "research.SteamSystem.3"),
              new BookPageText("research.SteamSystem.name", "research.SteamSystem.4"),
              new BookPageText("research.SteamSystem.name", "research.SteamSystem.5"))));
        BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 0,
          new BookCategory("category.Boiler.name",
            new BookEntry("research.Boiler.name",
              new BookPageItem("research.Boiler.name", "research.Boiler.0", new ItemStack(BOILER)),
              new BookPageCrafting("", "boiler1", "boiler2"))));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(BOILER);
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableBoiler = config.get(CATEGORY_STEAM_SYSTEM, "Enable Boiler (Crucial)", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return "enableBoiler".equals(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        return enableBoiler;
    }
}
