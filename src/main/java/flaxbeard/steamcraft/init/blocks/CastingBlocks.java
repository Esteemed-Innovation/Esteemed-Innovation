package flaxbeard.steamcraft.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.block.BlockCarvingTable;
import flaxbeard.steamcraft.block.BlockMold;
import flaxbeard.steamcraft.block.BlockSteamcraftCrucible;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.init.items.MetalcastingItems;

import static flaxbeard.steamcraft.init.misc.OreDictEntries.PLANK_WOOD;

public class CastingBlocks implements IInitCategory {
    public enum Blocks {
        CRUCIBLE(new BlockSteamcraftCrucible(), "crucible"),
        NETHER_CRUCIBLE(new BlockSteamcraftCrucible(), "hellCrucible"),
        CARVING_TABLE(new BlockCarvingTable(), "carving"),
        MOLD(new BlockMold(), "mold");

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
            if (isEnabled()) {
                block.setCreativeTab(Steamcraft.tab);
                block.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
                block.setRegistryName(Steamcraft.MOD_ID, name);
                GameRegistry.register(block);
            }
            this.block = block;
        }

        public boolean isEnabled() {
            switch (this) {
                case CRUCIBLE: {
                    return Config.enableCrucible;
                }
                case NETHER_CRUCIBLE: {
                    return CRUCIBLE.isEnabled() && Config.enableHellCrucible;
                }
                case CARVING_TABLE:
                case MOLD: {
                    return Config.enableMold;
                }
            }
            return false;
        }

        public Block getBlock() {
            return block;
        }
    }

    @Override
    public void oreDict() {}

    @Override
    public void recipes() {
        for (Blocks block : Blocks.LOOKUP) {
            switch (block) {
                case CRUCIBLE: {
                    BookRecipeRegistry.addRecipe("crucible", new ItemStack(block.getBlock()),
                      "x x",
                      "x x",
                      "xxx",
                      'x', Items.BRICK
                      );
                    break;
                }
                case NETHER_CRUCIBLE: {
                    BookRecipeRegistry.addRecipe("hellCrucible", new ItemStack(block.getBlock()),
                      "x x",
                      "x x",
                      "xxx",
                      'x', Items.NETHERBRICK
                    );
                    break;
                }
                case CARVING_TABLE: {
                    BookRecipeRegistry.addRecipe("carving", new ShapedOreRecipe(block.getBlock(),
                      "xzx",
                      "x x",
                      "xxx",
                      'x', PLANK_WOOD,
                      'z', MetalcastingItems.Items.BLANK_MOLD.getItem()
                      ));
                    break;
                }
                case MOLD: {
                    BookRecipeRegistry.addRecipe("mold", new ItemStack(block.getBlock()),
                      "xxx",
                      "xxx",
                      'x', Items.BRICK
                    );
                    break;
                }
            }
        }
    }
}
