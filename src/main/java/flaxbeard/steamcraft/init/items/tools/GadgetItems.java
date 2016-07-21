package flaxbeard.steamcraft.init.items.tools;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.handler.CanisterHandler;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.misc.integration.CrossMod;
import flaxbeard.steamcraft.init.misc.integration.baubles.BaublesIntegration;
import flaxbeard.steamcraft.item.ItemAstrolabe;
import flaxbeard.steamcraft.item.ItemSteamCell;
import flaxbeard.steamcraft.item.ItemSteamcraftBook;
import flaxbeard.steamcraft.item.ItemWrench;
import flaxbeard.steamcraft.item.tool.ItemSpyglass;

import static net.minecraft.init.Items.*;
import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;

public class GadgetItems implements IInitCategory {
    public enum Items {
        BOOK(new ItemSteamcraftBook(), "book"),
        SPYGLASS(new ItemSpyglass(), "spyglass"),
        ASTROLABE(new ItemAstrolabe(), "astrolabe"),
        ITEM_CANISTER(new Item(), "canister"),
        SURVIVALIST_TOOLKIT(CrossMod.BAUBLES ? BaublesIntegration.getSurvivalist() : new Item().setMaxStackSize(1), "survivalist"),
        STEAM_CELL_EMPTY(new Item(), "steamcell_empty"),
        STEAM_CELL_FULL(new ItemSteamCell(), "steamcell_full"),
        STEAM_CELL_FILLER(CrossMod.BAUBLES ? BaublesIntegration.getSteamCellFiller() : null, "steamcell_filler"),
        WRENCH(new ItemWrench(), "wrench");

        public static Items[] LOOKUP = new Items[values().length];
        static {
            for (Items item : values()) {
                if (item.isEnabled()) {
                    LOOKUP[item.ordinal()] = item;
                }
            }
        }

        private Item item;

        Items(Item item, String name) {
            if (item != null) {
                item.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
                item.setCreativeTab(Steamcraft.tab);
                item.setRegistryName(Steamcraft.MOD_ID, name);
                GameRegistry.register(item);
                if (item instanceof IEnhancement) {
                    SteamcraftRegistry.registerEnhancement((IEnhancement) item);
                }
            }
            this.item = item;
        }

        public Item getItem() {
            return item;
        }

        public boolean isEnabled() {
            switch (this) {
                case BOOK: {
                    return true;
                }
                case SPYGLASS: {
                    return Config.enableSpyglass;
                }
                case ASTROLABE: {
                    return Config.enableAstrolabe;
                }
                case ITEM_CANISTER: {
                    return Config.enableCanister;
                }
                case SURVIVALIST_TOOLKIT: {
                    return Config.enableSurvivalist;
                }
                case STEAM_CELL_EMPTY: {
                    return Config.enableSteamCell;
                }
                case STEAM_CELL_FULL: {
                    return Config.enableSteamCell;
                }
                case STEAM_CELL_FILLER: {
                    return Config.enableSteamCell && Config.enableSteamCellBauble && CrossMod.BAUBLES;
                }
                case WRENCH: {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case BOOK: {
                    BookRecipeRegistry.addRecipe("book", new ShapelessOreRecipe(item.getItem(), BOOK,
                      ORE_COPPER, ORE_ZINC));
                    break;
                }
                case SPYGLASS: {
                    BookRecipeRegistry.addRecipe("spyglass1", new ShapedOreRecipe(item.getItem(),
                      "gb ",
                      "bgb",
                      " bb",
                      'b', INGOT_BRASS,
                      'g', PANE_GLASS_COLORLESS
                    ));
                    BookRecipeRegistry.addRecipe("spyglass2", new ShapedOreRecipe(item.getItem(),
                      "gb ",
                      "bgb",
                      " bb",
                      'b', PLATE_BRASS,
                      'g', PANE_GLASS_COLORLESS
                    ));
                    break;
                }
                case ASTROLABE: {
                    BookRecipeRegistry.addRecipe("astrolabe", new ShapedOreRecipe(item.getItem(),
                      " x ",
                      "xrx",
                      " x ",
                      'x', INGOT_BRASS,
                      'r', DUST_REDSTONE
                    ));
                    break;
                }
                case ITEM_CANISTER: {
                    GameRegistry.addRecipe(new CanisterHandler());
                    BookRecipeRegistry.addRecipe("canister", new ShapedOreRecipe(item.getItem(),
                      " i ",
                      "i i",
                      " i ",
                      'i', NUGGET_ZINC
                    ));
                    break;
                }
                case SURVIVALIST_TOOLKIT: {
                    BookRecipeRegistry.addRecipe("survivalist", new ShapedOreRecipe(item.getItem(),
                      "b s",
                      "xwx",
                      "xxx",
                      'x', LEATHER_ORE,
                      's', STRING_ORE,
                      'b', BRICK,
                      'w', STICK_WOOD
                    ));
                    break;
                }
                case STEAM_CELL_EMPTY: {
                    BookRecipeRegistry.addRecipe("steamcell",
                      new ShapedOreRecipe(item.getItem(),
                        "nbn",
                        "bpb",
                        "nbn",
                        'n', NUGGET_BRASS,
                        'b', NETHERBRICK,
                        'p', BLAZE_POWDER
                      ));
                    break;
                }
                case STEAM_CELL_FULL: {
                    break;
                }
                case STEAM_CELL_FILLER: {
                    BookRecipeRegistry.addRecipe("steamcellFiller",
                      new ShapedOreRecipe(item.getItem(),
                        " p ",
                        "i i",
                        "i i",
                        'p', SteamNetworkBlocks.Blocks.PIPE.getBlock(),
                        'i', PLATE_IRON
                      ));
                    break;
                }
                case WRENCH: {
                    BookRecipeRegistry.addRecipe("wrench1", new ShapedOreRecipe(item.getItem(),
                      "  i",
                      " bb",
                      "b  ",
                      'i', INGOT_IRON,
                      'b', PLATE_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("wrench2", new ShapedOreRecipe(item.getItem(),
                      "  i",
                      " bb",
                      "b  ",
                      'i', INGOT_IRON,
                      'b', INGOT_BRASS
                    ));
                    break;
                }
            }
        }
    }
}
