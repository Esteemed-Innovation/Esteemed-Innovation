package flaxbeard.steamcraft.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.block.BlockEngineeringTable;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.init.misc.OreDictEntries;

public class MiscellaneousBlocks implements IInitCategory {
    public enum Blocks {
        ENGINEERING_TABLE(new BlockEngineeringTable(), "engineering");

        public static Blocks[] LOOKUP = new Blocks[values().length];

        static {
            for (Blocks block : values()) {
                if (block.isEnabled()) {
                    LOOKUP[block.ordinal()] = block;
                }
            }
        }

        private Block block;

        Blocks(Block block, String name) {
            block.setCreativeTab(Steamcraft.tab);
            block.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
            block.setRegistryName(Steamcraft.MOD_ID, name);
            GameRegistry.register(block);
            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            this.block = block;
        }

        public boolean isEnabled() {
            return Config.enableEngineering;
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
                      'z', OreDictEntries.PLATE_IRON
                    ));
                    break;
                }
            }
        }
    }
}
