package eiteam.esteemedinnovation.materials.raw.config;

import eiteam.esteemedinnovation.commons.util.WorldGenMinableBase;
import eiteam.esteemedinnovation.materials.MaterialsModule;
import eiteam.esteemedinnovation.materials.raw.BlockGenericOre;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import static eiteam.esteemedinnovation.materials.MaterialsModule.ORE_BLOCK;

public class ConfigurableOreGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int coordX = chunkX * 16;
        int coordZ = chunkZ * 16;

        for (OreGenerationDefinition ore : MaterialsModule.oresConfig.getOres()) {
            for (BiomeDefinition biome : ore.getBiomeDefinitions()) {
                if (biome.getDimension() == world.provider.getDimension()) {
                    Biome currentBiome = world.getBiomeForCoordsBody(new BlockPos(coordX, 0, coordZ));
                    if (biome.getBiomeMatcher().matches(currentBiome)) {
                        generateOre(biome, coordX, coordZ, ore.getOreType(biome.getDimension()), random, world);
                    }
                }
            }
        }
    }

    private static void generateOre(BiomeDefinition biomeDef, int baseX, int baseZ, BlockGenericOre.OreBlockTypes ore, Random random, World world) {
        WorldGenMinable minable = new WorldGenMinableBase(ORE_BLOCK.getDefaultState().withProperty(BlockGenericOre.VARIANT, ore), biomeDef.getMaxVeinSize(), biomeDef::matches);
        int minY = biomeDef.getMinY();
        int maxY = biomeDef.getMaxY();
        for (int i = 0; i < biomeDef.getMaxVeinsPerChunk(); i++) {
            int x = baseX + random.nextInt(16);
            int y = minY + random.nextInt(maxY - minY);
            int z = baseZ + random.nextInt(16);
            minable.generate(world, random, new BlockPos(x, y, z));
        }
    }
}