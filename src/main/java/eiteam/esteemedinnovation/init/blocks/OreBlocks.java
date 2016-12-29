package eiteam.esteemedinnovation.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import eiteam.esteemedinnovation.metals.raw.BlockGenericOre;
import eiteam.esteemedinnovation.init.InitCategory;
import eiteam.esteemedinnovation.misc.BlockManyMetadataItem;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.ORE_ZINC;
import static eiteam.esteemedinnovation.init.misc.OreDictEntries.ORE_COPPER;

public class OreBlocks implements InitCategory {
    public enum Blocks {
        OVERWORLD_COPPER_ORE(0),
        OVERWORLD_ZINC_ORE(1),
        NETHER_COPPER_ORE(2),
        NETHER_ZINC_ORE(3),
        END_COPPER_ORE(4),
        END_ZINC_ORE(5);

        public static Block ORE_BLOCK = new BlockGenericOre("ore");

        static {
            GameRegistry.register(ORE_BLOCK);
            GameRegistry.register(new BlockManyMetadataItem(ORE_BLOCK).setRegistryName(ORE_BLOCK.getRegistryName()));
        }
        private int meta;

        Blocks(int meta) {
            this.meta = meta;
        }

        public int getMetadata() {
            return meta;
        }

        public ItemStack createItemStack() {
            return createItemStack(1);
        }

        public ItemStack createItemStack(int size) {
            return new ItemStack(ORE_BLOCK, size, getMetadata());
        }
    }

    @Override
    public void oreDict() {
        for (Blocks block : Blocks.values()) {
            String dict = block.getMetadata() % 2 == 0 ? ORE_COPPER : ORE_ZINC;
            OreDictionary.registerOre(dict, block.createItemStack());
        }
    }

    @Override
    public void recipes() {}
}
