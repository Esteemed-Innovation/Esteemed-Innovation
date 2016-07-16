package flaxbeard.steamcraft.init.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.steamcraft.block.BlockBeacon;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.item.BlockManyMetadataItem;

import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;
import static flaxbeard.steamcraft.init.misc.CraftingHelpers.add3x3Recipe;

public class MetalBlocks implements IInitCategory {
    public enum Blocks {
        COPPER(0),
        ZINC(1),
        BRASS(2),
        GILDED_IRON(3);

        public static Block BLOCK = new BlockBeacon(Material.IRON);

        static {
            GameRegistry.register(BLOCK);
            GameRegistry.register(new BlockManyMetadataItem(BLOCK).setRegistryName(BLOCK.getRegistryName()));
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
            return new ItemStack(BLOCK, size, getMetadata());
        }
    }

    @Override
    public void oreDict() {
        OreDictionary.registerOre(BLOCK_COPPER, Blocks.COPPER.createItemStack());
        OreDictionary.registerOre(BLOCK_ZINC, Blocks.ZINC.createItemStack());
        OreDictionary.registerOre(BLOCK_BRASS, Blocks.BRASS.createItemStack());
        OreDictionary.registerOre(BLOCK_GILDED_IRON, Blocks.GILDED_IRON.createItemStack());
    }

    @Override
    public void recipes() {
        for (Blocks block : Blocks.values()) {
            String in = null;
            switch (block) {
                case COPPER: {
                    in = INGOT_COPPER;
                    break;
                }
                case ZINC: {
                    in = INGOT_ZINC;
                    break;
                }
                case BRASS: {
                    in = INGOT_BRASS;
                    break;
                }
                case GILDED_IRON: {
                    in = INGOT_GILDED_IRON;
                    break;
                }
            }
            if (in == null) {
                continue;
            }
            add3x3Recipe(block.createItemStack(), in);
        }
    }
}
