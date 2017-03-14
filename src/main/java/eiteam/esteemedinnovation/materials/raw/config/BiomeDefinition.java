package eiteam.esteemedinnovation.materials.raw.config;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A Java version of the Biome notation in the configurable Ores JSON. It is used for ore generation.
 */
public class BiomeDefinition {
    protected final int dimension;
    protected final Biome biome;
    protected final int minY;
    protected final int maxY;
    protected final int maxVeinsPerChunk;
    protected final int maxVeinSize;
    protected final List<String> replaceableBlocksOreDict;
    protected final List<Pair<Block, Integer>> replaceableBlocksAndMeta;

    public BiomeDefinition(int dimension, @Nonnull Biome biome, int minY, int maxY, int maxVeinSize, int maxVeinsPerChunk, List<String> replaceableBlocksOreDict, List<Pair<Block, Integer>> replaceableBlocksAndMeta) {
        this.dimension = dimension;
        this.biome = biome;
        this.minY = minY;
        this.maxY = maxY;
        this.maxVeinsPerChunk = maxVeinsPerChunk;
        this.maxVeinSize = maxVeinSize;
        this.replaceableBlocksOreDict = replaceableBlocksOreDict;
        this.replaceableBlocksAndMeta = replaceableBlocksAndMeta;
    }

    public int getDimension() {
        return dimension;
    }

    public Biome getBiome() {
        return biome;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxVeinsPerChunk() {
        return maxVeinsPerChunk;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public ImmutableList<String> getReplaceableBlocksOreDict() {
        return ImmutableList.copyOf(replaceableBlocksOreDict);
    }

    public ImmutableList<Pair<Block, Integer>> getReplaceableBlocksAndMeta() {
        return ImmutableList.copyOf(replaceableBlocksAndMeta);
    }

    /**
     * @param state The state to check
     * @return Whether the provided blockstate matches this BiomeDefinition.
     */
    public boolean matches(IBlockState state) {
        // Early return for performance
        if (state == null || (replaceableBlocksOreDict.isEmpty() && replaceableBlocksAndMeta.isEmpty())) {
            return false;
        }

        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);
        if (replaceableBlocksAndMeta.contains(Pair.of(block, meta)) ||
          replaceableBlocksAndMeta.contains(Pair.of(block, OreDictionary.WILDCARD_VALUE))) {
            return true;
        }
        Item itemBlock = Item.getItemFromBlock(block);
        if (itemBlock == null) {
            return false;
        }
        int[] ids = OreDictionary.getOreIDs(new ItemStack(itemBlock, 1, meta));
        for (int id : ids) {
            if (replaceableBlocksOreDict.contains(OreDictionary.getOreName(id))) {
                return true;
            }
        }
        return false;
    }
}
