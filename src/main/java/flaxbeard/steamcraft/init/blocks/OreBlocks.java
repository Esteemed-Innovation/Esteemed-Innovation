package flaxbeard.steamcraft.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.steamcraft.Steamcraft;
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

        public static Block ORE_BLOCK = GameRegistry.register(new BlockSteamcraftOre("ore"));
        public static Item ORE_ITEMBLOCK = GameRegistry.register(new ItemBlock(ORE_BLOCK).setUnlocalizedName("ore")
          .setRegistryName(Steamcraft.MOD_ID, "ore"));
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
//            return new ItemStack(ORE_BLOCK, size, getMetadata());
            return new ItemStack(ORE_ITEMBLOCK, size, getMetadata());
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
