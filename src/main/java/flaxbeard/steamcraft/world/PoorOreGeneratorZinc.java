package flaxbeard.steamcraft.world;

import com.google.common.collect.MapMaker;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.world.NoiseGen.NoiseGenSimplex;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Map;
import java.util.Random;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 *         Used with permission for FSP
 */
public class PoorOreGeneratorZinc {

    private final EventType eventType;
    private final WorldGenerator oreGen;
    private final double scale, denseArea, fringeArea;
    private final int yLevel, yRange, noiseSeed;

    private final Map<World, NoiseGen> noiseMap = new MapMaker().weakKeys().makeMap();

    public PoorOreGeneratorZinc(EventType eventType, int density, int yLevel, int yRange, int noiseSeed) {
        this(eventType, 0.0025, 0.85, 0.65, density, yLevel, yRange, noiseSeed);
    }

    public PoorOreGeneratorZinc(EventType eventType, double scale, double denseArea, double fringeArea, int density, int yLevel, int yRange, int noiseSeed) {
        this.eventType = eventType;
        this.scale = scale;
        this.denseArea = denseArea;
        this.fringeArea = fringeArea;
        this.yLevel = yLevel;
        this.yRange = yRange;
        this.noiseSeed = noiseSeed;
        oreGen = new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 2, density, Blocks.stone);
    }

    @SubscribeEvent
    public void generate(OreGenEvent.Post event) {

        World world = event.world;
        Random rand = event.rand;
        int worldX = event.worldX;
        int worldZ = event.worldZ;

        if (!TerrainGen.generateOre(world, rand, oreGen, worldX, worldZ, eventType))
            return;

        NoiseGen noise = noiseMap.get(world);
        if (noise == null) {
            long seed = world.getSeed();
            seed += world.provider.dimensionId;
            seed += noiseSeed;
            noise = new NoiseGenSimplex(new Random(seed), scale);
            noiseMap.put(world, noise);
        }

        if (canGen(world, rand, worldX, worldZ))
            for (int i = 0; i < 32; i++) {
                int x = worldX + rand.nextInt(16);
                int z = worldZ + rand.nextInt(16);
                double strength = noise.noise(x, z);
                if (strength > denseArea || (strength > fringeArea && rand.nextFloat() > 0.7)) {
                    int y = yLevel + Math.round((float) rand.nextGaussian() * yRange);
                    oreGen.generate(world, rand, x, y, z);
                }
            }
    }

    protected boolean canGen(World world, Random rand, int x, int z) {
        return true;
    }
}