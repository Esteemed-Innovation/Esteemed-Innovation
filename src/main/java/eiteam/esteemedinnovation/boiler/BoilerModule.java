package eiteam.esteemedinnovation.boiler;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_STEAM_SYSTEM;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.BASICS_SECTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.INGOT_BRASS;
import static eiteam.esteemedinnovation.commons.OreDictEntries.PLATE_THIN_BRASS;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;
import static net.minecraft.init.Blocks.FURNACE;

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
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        if (enableBoiler) {
            RecipeUtility.addRecipe(event, true, "boiler1", BOILER,
              "xxx",
              "xfx",
              "xxx",
              'x', INGOT_BRASS,
              'f', FURNACE
            );
            RecipeUtility.addRecipe(event, true, "boiler2", BOILER,
              "xxx",
              "xfx",
              "xxx",
              'x', PLATE_THIN_BRASS,
              'f', FURNACE
            );
        }
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
