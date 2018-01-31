package eiteam.esteemedinnovation.steamsafety;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.misc.BlockManyMetadataItem;
import eiteam.esteemedinnovation.steamsafety.disc.BlockRuptureDisc;
import eiteam.esteemedinnovation.steamsafety.disc.TileEntityRuptureDisc;
import eiteam.esteemedinnovation.steamsafety.gauge.BlockSteamGauge;
import eiteam.esteemedinnovation.steamsafety.gauge.TileEntitySteamGauge;
import eiteam.esteemedinnovation.steamsafety.gauge.TileEntitySteamGaugeRenderer;
import eiteam.esteemedinnovation.steamsafety.whistle.BlockWhistle;
import eiteam.esteemedinnovation.steamsafety.whistle.TileEntityWhistle;
import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.commons.OreDictEntries.PLATE_THIN_ZINC;
import static net.minecraft.init.Items.COMPASS;

public class SafetyModule extends ContentModule {
    public static Block STEAM_GAUGE;
    public static Block RUPTURE_DISC;
    public static Block STEAM_WHISTLE;

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
        if (Config.enableRuptureDisc) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 2,
              new BookCategory("category.RuptureDisc.name",
                new BookEntry("research.RuptureDisc.name",
                  new BookPageItem("research.RuptureDisc.name", "research.RuptureDisc.0", new ItemStack(RUPTURE_DISC)),
                  new BookPageText("research.RuptureDisc.name", "research.RuptureDisc.1"),
                  new BookPageCrafting("", "disc"))));
        }

        if (Config.enableHorn) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 3,
              new BookCategory("category.Whistle.name",
                new BookEntry("research.Whistle.name",
                  new BookPageItem("research.Whistle.name", "research.Whistle.0", new ItemStack(STEAM_WHISTLE)),
                  new BookPageCrafting("", "whistle1", "whistle2"))));
        }

        if (Config.enableGauge) {
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
}
