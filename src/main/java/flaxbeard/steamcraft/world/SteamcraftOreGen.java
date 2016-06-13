package flaxbeard.steamcraft.world;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.init.blocks.OreBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import static flaxbeard.steamcraft.init.blocks.OreBlocks.Blocks.*;
import static net.minecraft.init.Blocks.STONE;
import static net.minecraft.init.Blocks.END_STONE;
import static net.minecraft.init.Blocks.NETHERRACK;

public class SteamcraftOreGen implements IWorldGenerator {
    //Support for World Generation Manager
    public String getName() {
       return "FlaxbeardsSteamPower";
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
            case 0: {
                generateSurface(world, random, chunkX * 16, chunkZ * 16);
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
            WorldGenMinable copper = getCopperOre(END_COPPER_ORE.getMetadata(), END_STONE);
            generateBlocks(world, copper, random, 128, 10, i, j);
        }

        if (Config.genZincEnd) {
            WorldGenMinable zinc = getZincOre(END_ZINC_ORE.getMetadata(), END_STONE);
            generateBlocks(world, zinc, random, 128, 10, i, j);
        }
    }

    private void generateSurface(World world, Random random, int i, int j) {
        if (Config.genCopperOverworld) {
            WorldGenMinable copper = getCopperOre(OVERWORLD_COPPER_ORE.getMetadata(), STONE);
            generateBlocks(world, copper, random, 80, 10, i, j);
        }

        if (Config.genZincOverworld) {
            WorldGenMinable zinc = getZincOre(OVERWORLD_ZINC_ORE.getMetadata(), STONE);
            generateBlocks(world, zinc, random, 75, 10, i, j);
        }
    }

    private void generateNether(World world, Random random, int i, int j) {
        if (Config.genCopperNether) {
            WorldGenMinable copper = getCopperOre(NETHER_COPPER_ORE.getMetadata(), NETHERRACK);
            generateBlocks(world, copper, random, 128, 10, i, j);
        }

        if (Config.genZincNether) {
            WorldGenMinable zinc = getZincOre(NETHER_ZINC_ORE.getMetadata(), NETHERRACK);
            generateBlocks(world, zinc, random, 128, 10, i, j);
        }
    }

    private void generateExtra(World world, Random random, int i, int j) {
        if (Config.genCopperExtras) {
            WorldGenMinable copper = getCopperOre(OVERWORLD_COPPER_ORE.getMetadata(), STONE);
            generateBlocks(world, copper, random, 128, 10, i, j);
        }

        if (Config.genZincExtras) {
            WorldGenMinable zinc = getZincOre(OVERWORLD_ZINC_ORE.getMetadata(), STONE);
            generateBlocks(world, zinc, random, 128, 10, i, j);
        }
    }

    private WorldGenMinable getCopperOre(int meta, Block block) {
        return getOre(meta, 10, block);
    }

    private WorldGenMinable getZincOre(int meta, Block block) {
        return getOre(meta, 7, block);
    }

    private WorldGenMinable getOre(int meta, int count, Block block) {
        BlockMatcher matcher = BlockMatcher.forBlock(block);
        return new WorldGenMinable(OreBlocks.Blocks.block.getStateFromMeta(meta), count, matcher);
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
