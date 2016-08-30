package eiteam.esteemedinnovation.init.blocks;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.block.BlockEngineeringTable;
import eiteam.esteemedinnovation.block.BlockFunnel;
import eiteam.esteemedinnovation.block.BlockOreDepositGenerator;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.misc.OreDictEntries;
import eiteam.esteemedinnovation.item.BlockManyMetadataItem;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MiscellaneousBlocks implements IInitCategory {
    public enum Blocks {
        ENGINEERING_TABLE(new BlockEngineeringTable(), "engineering"),
        FUNNEL(new BlockFunnel(), "funnel"),
        ORE_DEPOSIT_BLOCK(new BlockOreDepositGenerator(), true, "ore_deposit_generator");

        public static Blocks[] LOOKUP = new Blocks[values().length];

        static {
            for (Blocks block : values()) {
                if (block.isEnabled()) {
                    LOOKUP[block.ordinal()] = block;
                }
            }
        }

        private Block block;

        Blocks(Block block, boolean meta, String name) {
            this(block, new BlockManyMetadataItem(block), name);
        }

        Blocks(Block block, String name) {
            this(block, new ItemBlock(block), name);
        }

        // You probably won't actually be able to use this one since ItemBlock constructors take the Block.
        Blocks(Block block, ItemBlock itemBlock, String name) {
            block.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            block.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(block);
            GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
            this.block = block;
        }

        public boolean isEnabled() {
            switch (this) {
                case ENGINEERING_TABLE: {
                    return Config.enableEngineering;
                }
                case FUNNEL: {
                    return Config.enableFunnel;
                }
                case ORE_DEPOSIT_BLOCK: {
                    return true;
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
                case ENGINEERING_TABLE: {
                    BookRecipeRegistry.addRecipe("engineering", new ShapedOreRecipe(block.getBlock(),
                      "xzx",
                      "x x",
                      "xxx",
                      'x', OreDictEntries.COBBLESTONE_ORE,
                      'z', OreDictEntries.PLATE_THIN_IRON
                    ));
                    break;
                }
                case FUNNEL: {
                    BookRecipeRegistry.addRecipe("funnel", new ShapedOreRecipe(block.getBlock(),
                      "c c",
                      "cbc",
                      " c ",
                      'c', OreDictEntries.INGOT_COPPER,
                      'b', Items.BOWL
                    ));
                    break;
                }
                case ORE_DEPOSIT_BLOCK: {
                    break;
                }
            }
        }
    }
}
