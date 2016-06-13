package flaxbeard.steamcraft.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.steamcraft.block.BlockSteamcraftOre;
import flaxbeard.steamcraft.init.IInitCategory;

import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;

public class OreBlocks implements IInitCategory {
    public enum Blocks {
        OVERWORLD_COPPER_ORE(0),
        OVERWORLD_ZINC_ORE(1),
        NETHER_COPPER_ORE(2),
        NETHER_ZINC_ORE(3),
        END_COPPER_ORE(4),
        END_ZINC_ORE(5);

        public static Block block = new BlockSteamcraftOre("ore");
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
            return new ItemStack(block, size, getMetadata());
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
