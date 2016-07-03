package flaxbeard.steamcraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class SteamcraftIslandGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case -1:
                generateNether(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 0:
                generateSurface(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 1:
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
                break;
        }
    }

    private void generateEnd(World world, Random random, int i, int j) {}

    private void generateSurface(World world, Random random, int i, int j) {
        if (random.nextInt(10) == 0) {
            int minY = 255;
            int maxY = 0;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 255; y > 0; y--) {
                        BlockPos pos = new BlockPos(i + x, y, j + z);
                        IBlockState state = world.getBlockState(pos);
                        if (state.getMaterial() != Material.AIR && !state.getBlock().isFoliage(world, pos)) {
                            if (y < minY) {
                                minY = y;
                            }
                            if (y > maxY) {
                                maxY = y;
                            }
                            break;
                        }
                    }
                }
            }
            if (maxY - minY <= 14) {
                int offset = Math.max(20, 255 - maxY - (maxY - minY) - random.nextInt(125));
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        int xOffset = 8 - Math.max(Math.abs(8 - x), Math.abs(7 - x));
                        int zOffset = 8 - Math.max(Math.abs(8 - z), Math.abs(7 - z));

                        int bottomOffset = Math.min(6, (int) ((xOffset + zOffset) / 1.5F)) - 2;
                        if (xOffset + zOffset > 4) {
                            for (int y = 255; y > (minY - bottomOffset); y--) {
                                BlockPos pos = new BlockPos(i + x, y, j + z);
                                IBlockState state = world.getBlockState(pos);
                                Material material = state.getMaterial();
                                if (material != Material.AIR && material != Material.WATER) {
                                    int meta = state.getBlock().getMetaFromState(state);
                                    world.setBlockToAir(pos);
                                    BlockPos min = new BlockPos(i + x, y - 1, j + z);
                                    if (y == (minY - bottomOffset) + 1 &&
                                      world.getBlockState(min).getBlock() == Blocks.DIRT) {
                                        world.setBlockState(min, Blocks.GRASS.getDefaultState());
                                    }
                                    BlockPos offsetPos = new BlockPos(i + x, y + offset, j + z);
                                    world.setBlockState(offsetPos, state, 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateNether(World world, Random random, int i, int j) {}
}
