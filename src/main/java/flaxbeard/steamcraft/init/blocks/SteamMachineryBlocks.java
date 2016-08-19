package flaxbeard.steamcraft.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.block.*;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.init.items.CraftingComponentItems;

import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;
import static flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks.Blocks.PIPE;
import static net.minecraft.init.Blocks.*;

public class SteamMachineryBlocks implements IInitCategory {
    public enum Blocks {
        ARCHIMEDES_SCREW(new BlockPump(), "pump"),
        ROCK_SMASHER(new BlockSmasher(), "smasher"),
        ROCK_SMASHER_DUMMY(new BlockDummy(), "smasher_dummy", true),
        STEAM_HEATER(new BlockSteamHeater(), "heater"),
        FILLING_PAD(new BlockChargingPad(), "charging_pad"),
        STEAM_FILLER(new BlockSteamCharger(), "charger"),
        STEAM_HAMMER(new BlockSteamHammer(), "hammer"),
        ITEM_MORTAR(new BlockItemMortar(), "item_mortar"),
        THUMPER(new BlockThumper(), "thumper"),
        THUMPER_DUMMY(new BlockThumperDummy(), "thumper_dummy", true),
        FAN(new BlockFan(), "fan"),
        VACUUM(new BlockVacuum(), "vacuum");

        private Block block;

        public static Blocks[] LOOKUP = new Blocks[values().length];
        static {
            for (Blocks block : values()) {
                if (block.isEnabled()) {
                    LOOKUP[block.ordinal()] = block;
                }
            }
        }

        Blocks(Block block, String name, boolean isDummy) {
            if (!isDummy) {
                block.setCreativeTab(Steamcraft.tab);
            }
            block.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
            block.setRegistryName(Steamcraft.MOD_ID, name);
            GameRegistry.register(block);
            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            this.block = block;
        }

        Blocks(Block block, String name) {
            this(block, name, false);
        }

        public Block getBlock() {
            return block;
        }

        public boolean isEnabled() {
            switch (this) {
                case ARCHIMEDES_SCREW: {
                    return Config.enablePump;
                }
                case ROCK_SMASHER:
                case ROCK_SMASHER_DUMMY: {
                    return Config.enableSmasher;
                }
                case STEAM_HEATER: {
                    return Config.enableHeater && SteamNetworkBlocks.Blocks.PIPE.isEnabled();
                }
                case FILLING_PAD: {
                    return Config.enableChargingPad && STEAM_FILLER.isEnabled();
                }
                case STEAM_FILLER: {
                    return Config.enableCharger;
                }
                case STEAM_HAMMER: {
                    return Config.enableHammer;
                }
                case ITEM_MORTAR: {
                    return Config.enableMortar;
                }
                case THUMPER:
                case THUMPER_DUMMY: {
                    return Config.enableThumper;
                }
                case FAN: {
                    return Config.enableFan;
                }
                case VACUUM: {
                    return Config.enableVacuum && FAN.isEnabled();
                }
            }
            return false;
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
                case ARCHIMEDES_SCREW: {
                    BookRecipeRegistry.addRecipe("pump1", new ShapedOreRecipe(block.getBlock(),
                      "gng",
                      "iii",
                      "ngn",
                      'i', PLATE_BRASS,
                      'n', NUGGET_BRASS,
                      'g', PANE_GLASS_COLORLESS
                    ));
                    BookRecipeRegistry.addRecipe("pump2", new ShapedOreRecipe(block.getBlock(),
                      "gng",
                      "iii",
                      "ngn",
                      'i', INGOT_BRASS,
                      'n', NUGGET_BRASS,
                      'g', PANE_GLASS_COLORLESS
                    ));
                    break;
                }
                case ROCK_SMASHER: {
                    BookRecipeRegistry.addRecipe("smasher1", new ShapedOreRecipe(block.getBlock(),
                      "bpi",
                      "bpi",
                      "bpi",
                      'i', PLATE_IRON,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', PLATE_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("smasher2", new ShapedOreRecipe(block.getBlock(),
                      "bpi",
                      "bpi",
                      "bpi",
                      'i', INGOT_IRON,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("smasher3", new ShapedOreRecipe(block.getBlock(),
                      "bpi",
                      "bpi",
                      "bpi",
                      'i', INGOT_IRON,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', PLATE_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("smasher4", new ShapedOreRecipe(block.getBlock(),
                      "bpi",
                      "bpi",
                      "bpi",
                      'i', PLATE_IRON,
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'b', INGOT_BRASS
                    ));
                    break;
                }
                case STEAM_HEATER: {
                    BookRecipeRegistry.addRecipe("heater1", new ShapedOreRecipe(block.getBlock(),
                      "ccc",
                      "xfx",
                      " p ",
                      'x', INGOT_BRASS,
                      'c', NUGGET_COPPER,
                      'f', FURNACE,
                      'p', PIPE.getBlock()
                    ));
                    BookRecipeRegistry.addRecipe("heater2", new ShapedOreRecipe(block.getBlock(),
                      "ccc",
                      "xfx",
                      " p ",
                      'x', PLATE_BRASS,
                      'c', NUGGET_COPPER,
                      'f', FURNACE,
                      'p', PIPE.getBlock()
                    ));
                    break;
                }
                case FILLING_PAD: {
                    BookRecipeRegistry.addRecipe("fillingPad1", new ShapedOreRecipe(block.getBlock(),
                      "p p",
                      "pcp",
                      "pbp",
                      'c', Blocks.STEAM_FILLER.getBlock(),
                      'p', PIPE.getBlock(),
                      'b', INGOT_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("fillingPad2", new ShapedOreRecipe(block.getBlock(),
                      "p p",
                      "pcp",
                      "pbp",
                      'c', Blocks.STEAM_FILLER.getBlock(),
                      'p', PIPE.getBlock(),
                      'b', PLATE_BRASS
                    ));
                    break;
                }
                case STEAM_FILLER: {
                    BookRecipeRegistry.addRecipe("filler", new ShapedOreRecipe(block.getBlock(),
                      " p ",
                      "xpx",
                      "xpx",
                      'x', COBBLESTONE_ORE,
                      'p', PIPE.getBlock()
                    ));
                    break;
                }
                case STEAM_HAMMER: {
                    BookRecipeRegistry.addRecipe("hammer1", new ShapedOreRecipe(block.getBlock(),
                      " ix",
                      "bix",
                      'x', INGOT_BRASS,
                      'i', INGOT_IRON,
                      'b', BLOCK_IRON
                    ));
                    BookRecipeRegistry.addRecipe("hammer2", new ShapedOreRecipe(block.getBlock(),
                      " ix",
                      "bix",
                      'x', PLATE_BRASS,
                      'i', INGOT_IRON,
                      'b', BLOCK_IRON
                    ));
                    break;
                }
                case ITEM_MORTAR: {
                    BookRecipeRegistry.addRecipe("itemMortar1", new ShapedOreRecipe(block.getBlock(),
                      "p p",
                      "pbp",
                      "ccc",
                      'p', PLATE_BRASS,
                      'c', PLATE_COPPER,
                      'b', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("itemMortar2", new ShapedOreRecipe(block.getBlock(),
                      "p p",
                      "pbp",
                      "ccc",
                      'p', INGOT_BRASS,
                      'c', PLATE_COPPER,
                      'b', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("itemMortar3", new ShapedOreRecipe(block.getBlock(),
                      "p p",
                      "pbp",
                      "ccc",
                      'p', PLATE_BRASS,
                      'c', INGOT_COPPER,
                      'b', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("itemMortar4", new ShapedOreRecipe(block.getBlock(),
                      "p p",
                      "pbp",
                      "ccc",
                      'p', INGOT_BRASS,
                      'c', INGOT_COPPER,
                      'b', CraftingComponentItems.Items.BRASS_PISTON.createItemStack()
                    ));
                    break;
                }
                case THUMPER: {
                    BookRecipeRegistry.addRecipe("thumper1", new ShapedOreRecipe(block.getBlock(),
                      "pbp",
                      "ebe",
                      "xix",
                      'i', BLOCK_IRON,
                      'b', BLOCK_BRASS,
                      'e', PIPE.getBlock(),
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'x', PLATE_BRASS
                    ));
                    BookRecipeRegistry.addRecipe("thumper2", new ShapedOreRecipe(block.getBlock(),
                      "pbp",
                      "ebe",
                      "xix",
                      'i', BLOCK_IRON,
                      'b', BLOCK_BRASS,
                      'e', PIPE.getBlock(),
                      'p', CraftingComponentItems.Items.BRASS_PISTON.createItemStack(),
                      'x', INGOT_BRASS
                    ));
                    break;
                }
                case FAN: {
                    BookRecipeRegistry.addRecipe("fan1", new ShapedOreRecipe(block.getBlock(),
                      "xxx",
                      "btb",
                      "xxx",
                      'x', INGOT_BRASS,
                      'b', IRON_BARS,
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                    ));
                    BookRecipeRegistry.addRecipe("fan2", new ShapedOreRecipe(block.getBlock(),
                      "xxx",
                      "btb",
                      "xxx",
                      'x', PLATE_BRASS,
                      'b', IRON_BARS,
                      't', CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()
                      ));
                    break;
                }
                case VACUUM: {
                    BookRecipeRegistry.addRecipe("vacuum1", new ShapedOreRecipe(block.getBlock(),
                      " x ",
                      "pfx",
                      " x ",
                      'x', INGOT_BRASS,
                      'p', PIPE.getBlock(),
                      'f', Blocks.FAN.getBlock()
                    ));
                    BookRecipeRegistry.addRecipe("vacuum2", new ShapedOreRecipe(block.getBlock(),
                      " x ",
                      "pfx",
                      " x ",
                      'x', PLATE_BRASS,
                      'p', PIPE.getBlock(),
                      'f', Blocks.FAN.getBlock()
                    ));
                    break;
                }
			default:
				break;
            }
        }
    }
}
