package eiteam.esteemedinnovation.charging;

import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_BLOCKS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_ITEMS;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.GADGET_SECTION;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_SECTION;

public class ChargingModule extends ContentModule implements ConfigurableModule {
    private static final int STEAMCELL_CAPACITY_DEFAULT = 100;
    public static Block FILLING_PAD;
    public static Block STEAM_FILLER;
    public static Item STEAM_CELL_FULL;
    public static Item STEAM_CELL_EMPTY;
    public static Item STEAM_CELL_FILLER;
    static boolean enableSteamCellBauble;
    static boolean enableSteamCell;
    static boolean enableChargingPad;
    static boolean enableCharger;
    static int steamCellCapacity;

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        FILLING_PAD = setup(event, new BlockChargingPad(), "charging_pad");
        STEAM_FILLER = setup(event, new BlockSteamCharger(), "charger");

        registerTileEntity(TileEntitySteamCharger.class, "steamCharger");
        registerTileEntity(TileEntityChargingPad.class, "chargingPad");
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        setupItemBlock(event, FILLING_PAD);
        setupItemBlock(event, STEAM_FILLER);

        STEAM_CELL_FULL = setup(event, new ItemSteamCell(), "steamcell_full");
        STEAM_CELL_EMPTY = setup(event, new Item(), "steamcell_empty");
        STEAM_CELL_FILLER = setup(event, new ItemSteamCellFiller(), "steamcell_filler");
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*if (Config.enableCharger) {
            if (Config.enableChargingPad) {
                BookRecipeRegistry.addRecipe("fillingPad1", new ShapedOreRecipe(FILLING_PAD,
                  "p p",
                  "pcp",
                  "pbp",
                  'c', STEAM_FILLER,
                  'p', BRASS_PIPE,
                  'b', INGOT_BRASS
                ));
                BookRecipeRegistry.addRecipe("fillingPad2", new ShapedOreRecipe(FILLING_PAD,
                  "p p",
                  "pcp",
                  "pbp",
                  'c', STEAM_FILLER,
                  'p', BRASS_PIPE,
                  'b', PLATE_THIN_BRASS
                ));
            }
            BookRecipeRegistry.addRecipe("filler", new ShapedOreRecipe(STEAM_FILLER,
              " p ",
              "xpx",
              "xpx",
              'x', COBBLESTONE_ORE,
              'p', BRASS_PIPE
            ));
        }

        if (Config.enableSteamCell) {
            BookRecipeRegistry.addRecipe("steamcell",
              new ShapedOreRecipe(STEAM_CELL_EMPTY,
                "nbn",
                "bpb",
                "nbn",
                'n', NUGGET_BRASS,
                'b', NETHERBRICK,
                'p', BLAZE_POWDER
              ));
            if (Config.enableSteamCellBauble) {
                BookRecipeRegistry.addRecipe("steamcellFiller",
                  new ShapedOreRecipe(STEAM_CELL_FILLER,
                    " p ",
                    "i i",
                    "i i",
                    'p', BRASS_PIPE,
                    'i', PLATE_THIN_IRON
                  ));
            }
        }
        */
    }

    @Override
    public void finish(Side side) {
        if (enableSteamCell) {
            BookPageRegistry.addCategoryToSection(GADGET_SECTION, 7,
              new BookCategory("category.SteamCells.name",
                new BookEntry("research.SteamCell.name",
                  new BookPageItem("research.SteamCell.name", "research.SteamCell.0", new ItemStack(STEAM_CELL_EMPTY), new ItemStack(STEAM_CELL_FULL)),
                  new BookPageCrafting("", "steamcell"))));
        }

        if (enableSteamCellBauble) {
            BookPageRegistry.addEntryToCategory("category.SteamCells.name",
              new BookEntry("research.SteamCellFiller.name",
                new BookPageItem("research.SteamCellFiller.name", "research.SteamCellFiller.0", new ItemStack(STEAM_CELL_FILLER)),
                new BookPageCrafting("", "steamcellFiller")));
        }

        if (enableCharger) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 6,
              new BookCategory("category.Filler.name",
                new BookEntry("research.Filler.name",
                  new BookPageItem("research.Filler.name", "research.Filler.0", new ItemStack(STEAM_FILLER)),
                  new BookPageText("research.Filler.name", "research.Filler.1"),
                  new BookPageCrafting("", "filler"))));
        }

        if (enableChargingPad && enableCharger) {
            BookPageRegistry.addCategoryToSection(STEAMPOWER_SECTION, 7,
              new BookCategory("category.FillingPad.name",
                new BookEntry("research.Filler.name",
                  new BookPageItem("research.FillingPad.name", "research.FillingPad.0", new ItemStack(FILLING_PAD)),
                  new BookPageCrafting("", "fillingPad1", "fillingPad2"))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(STEAM_FILLER);
        registerModel(FILLING_PAD);
        registerModel(STEAM_CELL_EMPTY);
        registerModel(STEAM_CELL_FULL);
        registerModel(STEAM_CELL_FILLER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamCharger.class, new TileEntitySteamChargerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChargingPad.class, new TileEntityChargingPadRenderer());
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        enableCharger = config.get(CATEGORY_BLOCKS, "Enable Steam Filler", true).getBoolean();
        enableChargingPad = config.get(CATEGORY_BLOCKS, "Enable Filling Pad", true).getBoolean();
        enableSteamCell = config.get(CATEGORY_ITEMS, "Enable Steam Cell", true).getBoolean();
        steamCellCapacity = config.get(CATEGORY_ITEMS, "Steam Cell capacity", STEAMCELL_CAPACITY_DEFAULT).getInt();
        enableSteamCellBauble = config.get(CATEGORY_ITEMS, "Enable Steam Cell Bauble", true).getBoolean();
    }

    @Override
    public boolean doesRecipeBelongTo(String configSetting) {
        return "enableCharger".equals(configSetting) ||
          "enableChargingPad".equals(configSetting) ||
          "enableSteamCell".equals(configSetting) ||
          "enableSteamCellBauble".equals(configSetting);
    }

    @Override
    public boolean isRecipeEnabled(String configSetting) {
        if ("enableCharger".equals(configSetting)) {
            return enableCharger;
        }
        if ("enableChargingPad".equals(configSetting)) {
            return enableCharger && enableChargingPad;
        }
        if ("enableSteamCell".equals(configSetting)) {
            return enableSteamCell;
        }
        if ("enableSteamCellBauble".equals(configSetting)) {
            return enableSteamCell && enableSteamCellBauble;
        }
        return false;
    }
}
