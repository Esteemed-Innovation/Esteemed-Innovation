package eiteam.esteemedinnovation.init.blocks;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.book.BookRecipeRegistry;
import eiteam.esteemedinnovation.block.pipe.BlockColdFluidPipe;
import eiteam.esteemedinnovation.block.pipe.BlockSteamPipe;
import eiteam.esteemedinnovation.init.IInitCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.INGOT_BRASS;
import static eiteam.esteemedinnovation.init.misc.OreDictEntries.PLATE_THIN_BRASS;

public class PipeBlocks implements IInitCategory {
    public enum Blocks {
        BRASS_PIPE(new BlockSteamPipe(), "brass_pipe"),
        COPPER_PIPE(new BlockColdFluidPipe(), "copper_pipe")
        ;

        private Block block;

        Blocks(Block block, String name) {
            block.setCreativeTab(EsteemedInnovation.tab);
            block.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            block.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(block);
            ItemBlock itemblock = new ItemBlock(block);
            itemblock.setRegistryName(block.getRegistryName());
            GameRegistry.register(itemblock);
            this.block = block;
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
        for (Blocks block : Blocks.values()) {
            switch (block) {
                case BRASS_PIPE: {
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
                      'x', PLATE_THIN_BRASS
                    ));
                    break;
                }
            }
        }
    }
}
