package eiteam.esteemedinnovation.metals.raw;

import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.init.blocks.OreBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import static net.minecraft.init.Blocks.STONE;
import static net.minecraft.init.Blocks.END_STONE;
import static net.minecraft.init.Blocks.NETHERRACK;

public class ExtraDimensionalOreGenerator implements IWorldGenerator {
    //Support for World Generation Manager
    public String getName() {
       return "EsteemedInnovation";
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        for (int id : Config.copperExtraDimensionIDs) {
            if (id == world.provider.getDimension()) {
                generateExtra(world, random, chunkX * 16, chunkZ * 16);
            }
        }
        for (int id : Config.zincExtraDimensionIDs) {
            if (id == world.provider.getDimension()) {
                generateExtra(world, random, chunkX * 16, chunkZ * 16);
            }
        }

        switch (world.provider.getDimension()) {
            case -1: {
                generateNether(world, random, chunkX * 16, chunkZ * 16);
                break;
            }
            case 1: {
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
                break;
            }
        }
    }

    private void generateEnd(World world, Random random, int i, int j) {
        if (Config.genCopperEnd) {
            WorldGenMinable copper = getCopperOre(BlockGenericOre.OreBlockTypes.END_COPPER, END_STONE);
            generateBlocks(world, copper, random, 128, 10, i, j);
        }

        if (Config.genZincEnd) {
            WorldGenMinable zinc = getZincOre(BlockGenericOre.OreBlockTypes.END_ZINC, END_STONE);
            generateBlocks(world, zinc, random, 128, 10, i, j);
        }
    }

    private void generateNether(World world, Random random, int i, int j) {
        if (Config.genCopperNether) {
            WorldGenMinable copper = getCopperOre(BlockGenericOre.OreBlockTypes.NETHER_COPPER, NETHERRACK);
            generateBlocks(world, copper, random, 128, 10, i, j);
        }

        if (Config.genZincNether) {
            WorldGenMinable zinc = getZincOre(BlockGenericOre.OreBlockTypes.NETHER_ZINC, NETHERRACK);
            generateBlocks(world, zinc, random, 128, 10, i, j);
        }
    }

    private void generateExtra(World world, Random random, int i, int j) {
        if (Config.genCopperExtras) {
            WorldGenMinable copper = getCopperOre(BlockGenericOre.OreBlockTypes.OVERWORLD_COPPER, STONE);
            generateBlocks(world, copper, random, 128, 10, i, j);
        }

        if (Config.genZincExtras) {
            WorldGenMinable zinc = getZincOre(BlockGenericOre.OreBlockTypes.OVERWORLD_ZINC, STONE);
            generateBlocks(world, zinc, random, 128, 10, i, j);
        }
    }

    private WorldGenMinable getCopperOre(BlockGenericOre.OreBlockTypes type, Block block) {
        return getOre(type, 10, block);
    }

    private WorldGenMinable getZincOre(BlockGenericOre.OreBlockTypes type, Block block) {
        return getOre(type, 7, block);
    }

    private WorldGenMinable getOre(BlockGenericOre.OreBlockTypes type, int count, Block block) {
        BlockMatcher matcher = BlockMatcher.forBlock(block);
        IBlockState state = OreBlocks.Blocks.ORE_BLOCK.getDefaultState().withProperty(BlockGenericOre.VARIANT, type);
        return new WorldGenMinable(state, count, matcher);
    }

    private void generateBlocks(World world, WorldGenMinable block, Random random, int maxY, int maxPerChunk, int x, int z) {
        for (int i = 0; i < maxPerChunk; i++) {
            int xPos = x + random.nextInt(16);
            int yPos = random.nextInt(maxY);
            int zPos = z + random.nextInt(16);
            BlockPos pos = new BlockPos(xPos, yPos, zPos);
            block.generate(world, random, pos);
        }
    }
}
