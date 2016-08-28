package eiteam.esteemedinnovation.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.block.BlockCarvingTable;
import eiteam.esteemedinnovation.block.BlockCrucible;
import eiteam.esteemedinnovation.block.BlockMold;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.items.MetalcastingItems;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.PLANK_WOOD;

public class CastingBlocks implements IInitCategory {
    public enum Blocks {
        CRUCIBLE(new BlockCrucible(), "crucible"),
        NETHER_CRUCIBLE(new BlockCrucible(), "hell_crucible"),
        CARVING_TABLE(new BlockCarvingTable(), "carving_table"),
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
            block.setCreativeTab(EsteemedInnovation.tab);
            block.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            block.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(block);
            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
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
    public void oreDict() {
        Blocks.values();
    }

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
