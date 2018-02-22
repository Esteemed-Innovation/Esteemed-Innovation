package eiteam.esteemedinnovation.steamsafety;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.misc.BlockManyMetadataItem;
import eiteam.esteemedinnovation.steamsafety.disc.BlockRuptureDisc;
import eiteam.esteemedinnovation.steamsafety.disc.TileEntityRuptureDisc;
import eiteam.esteemedinnovation.steamsafety.gauge.BlockSteamGauge;
import eiteam.esteemedinnovation.steamsafety.gauge.TileEntitySteamGauge;
import eiteam.esteemedinnovation.steamsafety.gauge.TileEntitySteamGaugeRenderer;
import eiteam.esteemedinnovation.steamsafety.whistle.BlockWhistle;
import eiteam.esteemedinnovation.steamsafety.whistle.TileEntityWhistle;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_STEAM_SYSTEM;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;

public class SafetyModule extends ContentModule implements ConfigurableModule {
    public static Block STEAM_GAUGE;
    public static Block RUPTURE_DISC;
    public static Block STEAM_WHISTLE;
    private static boolean enableRuptureDisc;
    private static boolean enableHorn;
    public static boolean enableGauge;

    @Override
    public void create(Side side) {
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        STEAM_GAUGE = setup(event, new BlockSteamGauge(), "steam_gauge");
        RUPTURE_DISC = setup(event, new BlockRuptureDisc(), "rupture_disc");
        STEAM_WHISTLE = setup(event, new BlockWhistle(), "steam_whistle");

        registerTileEntity(TileEntitySteamGauge.class, "steamGauge");
        registerTileEntity(TileEntityRuptureDisc.class, "ruptureDisc");
        registerTileEntity(TileEntityWhistle.class, "whistle");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, STEAM_GAUGE);
        setupItemBlock(event, RUPTURE_DISC, BlockManyMetadataItem::new);
        setupItemBlock(event, STEAM_WHISTLE);
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enableGauge) {
            BookRecipeRegistry.addRecipe("gauge", new ShapedOreRecipe(STEAM_GAUGE,
              " x ",
              "xrx",
              " x ",
              'x', INGOT_BRASS,
              'r', COMPASS));
        }
        if (Config.enableRuptureDisc) {
            BookRecipeRegistry.addRecipe("disc", new ShapedOreRecipe(RUPTURE_DISC,
              " x ",
              "xrx",
              " x ",
              'x', NUGGET_BRASS,
              'r', PLATE_THIN_ZINC
            ));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RUPTURE_DISC, 1, 0),
              PLATE_THIN_ZINC, new ItemStack(RUPTURE_DISC, 1, 1)));
        }
        if (Config.enableHorn) {
            BookRecipeRegistry.addRecipe("whistle1", new ShapedOreRecipe(STEAM_WHISTLE,
              " bb",
              " bn",
              "pp ",
              'n', NUGGET_BRASS,
              'b', PLATE_THIN_BRASS,
              'p', TransportationModule.BRASS_PIPE
            ));
            BookRecipeRegistry.addRecipe("whistle2", new ShapedOreRecipe(STEAM_WHISTLE,
              " bb",
              " bn",
              "pp ",
              'n', NUGGET_BRASS,
              'b', INGOT_BRASS,
              'p', TransportationModule.BRASS_PIPE
            ));
        }
        */
    }

    @Override
    public void finish(Side side) {
        if (enableRuptureDisc) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 2,
              new BookCategory("category.RuptureDisc.name",
                new BookEntry("research.RuptureDisc.name",
                  new BookPageItem("research.RuptureDisc.name", "research.RuptureDisc.0", new ItemStack(RUPTURE_DISC)),
                  new BookPageText("research.RuptureDisc.name", "research.RuptureDisc.1"),
                  new BookPageCrafting("", "disc"))));
        }

        if (enableHorn) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 3,
              new BookCategory("category.Whistle.name",
                new BookEntry("research.Whistle.name",
                  new BookPageItem("research.Whistle.name", "research.Whistle.0", new ItemStack(STEAM_WHISTLE)),
                  new BookPageCrafting("", "whistle1", "whistle2"))));
        }

        if (enableGauge) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 4,
              new BookCategory("category.Gauge.name",
                new BookEntry("research.Gauge.name",
                  new BookPageItem("research.Gauge.name", "research.Gauge.0", new ItemStack(STEAM_GAUGE)),
                  new BookPageCrafting("", "gauge"))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModelItemStack(new ItemStack(RUPTURE_DISC, 1, 0));
        registerModelItemStack(new ItemStack(RUPTURE_DISC, 1, 1));
        registerModel(STEAM_GAUGE);
        registerModel(STEAM_WHISTLE);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamGauge.class, new TileEntitySteamGaugeRenderer());
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableHorn = config.get(CATEGORY_STEAM_SYSTEM, "Enable Horn", true).getBoolean();
        enableGauge = config.get(CATEGORY_STEAM_SYSTEM, "Enable Pressure Gauge (Crucial)", true).getBoolean();
        enableRuptureDisc = config.get(CATEGORY_STEAM_SYSTEM, "Enable Rupture Disc", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return "enableHorn".equals(configSetting) ||
          "enableGauge".equals(configSetting) ||
          "enableRuptureDisc".equals(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        if ("enableHorn".equals(configSetting)) {
            return enableHorn;
        }
        if ("enableGauge".equals(configSetting)) {
            return enableGauge;
        }
        if ("enableRuptureDisc".equals(configSetting)) {
            return enableRuptureDisc;
        }
        return false;
    }
}
