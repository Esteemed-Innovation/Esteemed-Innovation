package flaxbeard.steamcraft.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.block.*;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.item.BlockManyMetadataItem;

import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

public class SteamNetworkBlocks implements IInitCategory {
    public enum Blocks {
        BOILER(new BlockBoiler(), "boiler"),
        FLASH_BOILER(new BlockFlashBoiler(), "flash_boiler"),
        PIPE(new BlockPipe(), "pipe"),
        VALVE_PIPE(new BlockValvePipe(), "valve_pipe"),
        TANK(new BlockSteamTank(), "steam_tank", true),
        STEAM_GAUGE(new BlockSteamGauge(), "steam_gauge"),
        RUPTURE_DISC(new BlockRuptureDisc(), "rupture_disc", true),
        STEAM_WHISTLE(new BlockWhistle(), "steam_whistle"),
        PRESSURE_CONVERTER(new BlockFluidSteamConverter(), "fluid_steam_converter");

        private Block block;

        public static Blocks[] LOOKUP = new Blocks[values().length];
        static {
            for (Blocks block : values()) {
                if (block.isEnabled()) {
                    LOOKUP[block.ordinal()] = block;
                }
            }
        }

        Blocks(Block block, String name) {
            this(block, name, false);
        }

        Blocks(Block block, String name, boolean meta) {
            block.setCreativeTab(Steamcraft.tab);
            block.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
            block.setRegistryName(Steamcraft.MOD_ID, name);
            GameRegistry.register(block);
            ItemBlock itemblock = meta ? new BlockManyMetadataItem(block) : new ItemBlock(block);
            itemblock.setRegistryName(block.getRegistryName());
            GameRegistry.register(itemblock);
            this.block = block;
        }

        public boolean isEnabled() {
            switch (this) {
                case BOILER: {
                    return Config.enableBoiler;
                }
                case FLASH_BOILER: {
                    return BOILER.isEnabled() && Config.enableFlashBoiler;
                }
                case PIPE: {
                    return Config.enablePipe;
                }
                case VALVE_PIPE: {
                    return PIPE.isEnabled() && Config.enableValvePipe;
                }
                case TANK: {
                    return Config.enableTank;
                }
                case STEAM_GAUGE: {
                    return Config.enableGauge;
                }
                case RUPTURE_DISC: {
                    return Config.enableRuptureDisc;
                }
                case STEAM_WHISTLE: {
                    return Config.enableHorn;
                }
                case PRESSURE_CONVERTER: {
                    return Config.enableFluidSteamConverter;
                }
            }
            return false;
        }

        public Block getBlock() {
            return block;
        }
    }

    @Override
    public void oreDict() {
        Blocks.values();
    }

    @Override
    public void recipes() {
        for (Blocks block : Blocks.LOOKUP) {
            switch (block) {
                case BOILER: {
                    BookRecipeRegistry.addRecipe("boiler1", new ShapedOreRecipe(block.getBlock(),
                      "xxx",
                      "xfx",
                      "xxx",
                      'x', INGOT_BRASS,
                      'f', FURNACE
                    ));
                    BookRecipeRegistry.addRecipe("boiler2", new ShapedOreRecipe(block.getBlock(),
                      "xxx",
                      "xfx",
                      "xxx",
                      'x', PLATE_BRASS,
                      'f', FURNACE
                    ));
                    break;
                }
                case FLASH_BOILER: {
                    BookRecipeRegistry.addRecipe("flashBoiler1", new ShapedOreRecipe(new ItemStack(block.getBlock(), 2),
                      "xtx",
                      "pbp",
                      "nnn",
                      'x', INGOT_BRASS,
                      'b', Blocks.BOILER.getBlock(),
                      't', Blocks.TANK.getBlock(),
                      'n', NETHER_BRICK,
                      'p', Blocks.PIPE.getBlock()
                    ));
                    BookRecipeRegistry.addRecipe("flashBoiler2", new ShapedOreRecipe(new ItemStack(block.getBlock(), 2),
                      "xtx",
                      "pbp",
                      "nnn",
                      'x', PLATE_BRASS,
                      'b', Blocks.BOILER.getBlock(),
                      't', Blocks.TANK.getBlock(),
                      'n', NETHER_BRICK,
                      'p', Blocks.PIPE.getBlock()
                    ));
                    break;
                }
                case PIPE: {
                    BookRecipeRegistry.addRecipe("pipe1", new ShapedOreRecipe(block.getBlock(),
                      "xxx",
                      "   ",
                      "xxx",
                      'x', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("pipe2", new ShapedOreRecipe(block.getBlock(),
                      "xxx",
                      "   ",
                      "xxx",
                      'x', PLATE_BRASS
                    ));
                    break;
                }
                case VALVE_PIPE: {
                    BookRecipeRegistry.addRecipe("valvePipe", new ShapelessOreRecipe(block.getBlock(),
                      Blocks.PIPE.getBlock(), LEVER));
                    break;
                }
                case TANK: {
                    BookRecipeRegistry.addRecipe("tank1", new ShapedOreRecipe(block.getBlock(),
                      "iii",
                      "i i",
                      "iii",
                      'i', PLATE_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("tank2", new ShapedOreRecipe(block.getBlock(),
                      "iii",
                      "i i",
                      "iii",
                      'i', INGOT_BRASS
                    ));
                    break;
                }
                case STEAM_GAUGE: {
                    BookRecipeRegistry.addRecipe("gauge", new ShapedOreRecipe(block.getBlock(),
                      " x ",
                      "xrx",
                      " x ",
                      'x', INGOT_BRASS,
                      'r', COMPASS));
                    break;
                }
                case STEAM_WHISTLE: {
                    BookRecipeRegistry.addRecipe("whistle1", new ShapedOreRecipe(block.getBlock(),
                      " bb",
                      " bn",
                      "pp ",
                      'n', NUGGET_BRASS,
                      'b', PLATE_BRASS,
                      'p', Blocks.PIPE.getBlock()
                    ));
                    BookRecipeRegistry.addRecipe("whistle2", new ShapedOreRecipe(block.getBlock(),
                      " bb",
                      " bn",
                      "pp ",
                      'n', NUGGET_BRASS,
                      'b', INGOT_BRASS,
                      'p', Blocks.PIPE.getBlock()
                    ));
                    break;
                }
                case RUPTURE_DISC: {
                    BookRecipeRegistry.addRecipe("disc", new ShapedOreRecipe(block.getBlock(),
                      " x ",
                      "xrx",
                      " x ",
                      'x', NUGGET_BRASS,
                      'r', PLATE_ZINC
                    ));
                    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(block.getBlock(), 1, 0),
                      PLATE_ZINC, new ItemStack(block.getBlock(), 1, 1)));
                    break;
                }
                case PRESSURE_CONVERTER: {
                    BookRecipeRegistry.addRecipe("fsc1", new ShapedOreRecipe(block.getBlock(),
                      "xlx",
                      "gpp",
                      "xlx",
                      'x', "ingotBrass",
                      'l', LEATHER_ORE,
                      'p', Blocks.PIPE.getBlock(),
                      'g', PANE_GLASS_COLORLESS
                    ));
                    BookRecipeRegistry.addRecipe("fsc1", new ShapedOreRecipe(block.getBlock(),
                      "xlx",
                      "gpp",
                      "xlx",
                      'x', "plateSteamcraftBrass",
                      'l', LEATHER_ORE,
                      'p', Blocks.PIPE.getBlock(),
                      'g', PANE_GLASS_COLORLESS
                    ));
                    break;
                }
            }
        }
    }
}
