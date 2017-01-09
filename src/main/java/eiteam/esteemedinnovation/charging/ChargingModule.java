package eiteam.esteemedinnovation.charging;

import baubles.api.BaubleType;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.misc.ItemBauble;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.commons.EsteemedInnovation.GADGET_CATEGORY;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.STEAMPOWER_CATEGORY;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;
import static net.minecraft.init.Items.BLAZE_POWDER;
import static net.minecraft.init.Items.NETHERBRICK;

public class ChargingModule extends ContentModule {
    public static Block FILLING_PAD;
    public static Block STEAM_FILLER;
    public static Item STEAM_CELL_FULL;
    public static Item STEAM_CELL_EMPTY;
    public static Item STEAM_CELL_FILLER;

    @Override
    public void create(Side side) {
        FILLING_PAD = setup(new BlockChargingPad(), "charging_pad");
        STEAM_FILLER = setup(new BlockSteamCharger(), "charger");
        STEAM_CELL_FULL = setup(new ItemSteamCell(), "steamcell_full");
        STEAM_CELL_EMPTY = setup(new Item(), "steamcell_empty");
        STEAM_CELL_FILLER = CrossMod.BAUBLES ? setup(new ItemBauble(BaubleType.AMULET).setMaxStackSize(1), "steamcell_filler") : null;

        registerTileEntity(TileEntitySteamCharger.class, "steamCharger");
        registerTileEntity(TileEntityChargingPad.class, "chargingPad");
    }

    @Override
    public void recipes(Side side) {
        if (Config.enableCharger) {
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
            if (Config.enableSteamCellBauble && CrossMod.BAUBLES) {
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
    }

    @Override
    public void finish(Side side) {
        if (Config.enableSteamCell) {
            BookPageRegistry.addEntryToCategory(GADGET_CATEGORY, new BookEntry("research.SteamCell.name",
              new BookPageItem("research.SteamCell.name", "research.SteamCell.0", new ItemStack(STEAM_CELL_EMPTY), new ItemStack(STEAM_CELL_FULL)),
              new BookPageCrafting("", "steamcell")));
        }

        if (Config.enableSteamCellBauble && CrossMod.BAUBLES) {
            BookPageRegistry.addEntryToCategory(GADGET_CATEGORY, new BookEntry("research.SteamCellFiller.name",
              new BookPageItem("research.SteamCellFiller.name", "research.SteamCellFiller.0", new ItemStack(STEAM_CELL_FILLER)),
              new BookPageCrafting("", "steamcellFiller")));
        }

        if (Config.enableCharger) {
            BookPageRegistry.addEntryToCategory(STEAMPOWER_CATEGORY, new BookEntry("research.Filler.name",
              new BookPageItem("research.Filler.name", "research.Filler.0", new ItemStack(STEAM_FILLER)),
              new BookPageText("research.Filler.name", "research.Filler.1"),
              new BookPageCrafting("", "filler")));
        }

        if (Config.enableChargingPad && Config.enableCharger) {
            BookPageRegistry.addEntryToCategory(STEAMPOWER_CATEGORY, new BookEntry("research.FillingPad.name",
              new BookPageItem("research.FillingPad.name", "research.FillingPad.0", new ItemStack(FILLING_PAD)),
              new BookPageCrafting("", "fillingPad1", "fillingPad2")));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
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
}
