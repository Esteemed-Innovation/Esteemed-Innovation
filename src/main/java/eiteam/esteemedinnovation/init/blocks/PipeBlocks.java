package eiteam.esteemedinnovation.init.blocks;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.transport.fluid.pipes.BlockColdFluidPipe;
import eiteam.esteemedinnovation.transport.steam.BlockSteamPipe;
import eiteam.esteemedinnovation.init.InitCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PipeBlocks implements InitCategory {
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
    public void recipes() {}
}
