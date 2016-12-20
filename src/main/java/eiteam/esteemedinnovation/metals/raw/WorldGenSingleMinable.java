package eiteam.esteemedinnovation.metals.raw;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings("Guava")
@ParametersAreNonnullByDefault
public class WorldGenSingleMinable extends WorldGenerator {
    private IBlockState state;

    public WorldGenSingleMinable(IBlockState state) {
        this.state = state;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        //noinspection SimplifiableIfStatement
        if (BlockOreDepositGenerator.canReplace(position, world)) {
            return world.setBlockState(position, state);
        }
        return false;
    }
}
