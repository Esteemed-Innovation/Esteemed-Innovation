package eiteam.esteemedinnovation.commons.util;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A new and improved version of Vanilla's WorldGenMinable.
 *
 * This version has better performance, by not checking the same block position and re-setting it over and over again.
 * Its actual generation speed should be pretty much the same as the vanilla one in most cases, but this one uses
 * a bit less memory by using less world calls and not creating a new BlockPos for every iteration.
 *
 * It also provides a superior API, {@link WorldGenMinableBase#canGenerateInPosition(BlockPos, IBlockAccess)} which
 * allows for proper position-sensitive checking. Be sure to remember that caves are generated after ores, though.
 *
 * Its constructors are the same, and unfortunately Google's predicate still has to be used :(
 */
public class WorldGenMinableBase extends WorldGenMinable {
    protected final int blockCount;
    protected final IBlockState oreState;
    protected final Predicate<IBlockState> predicate;

    public WorldGenMinableBase(IBlockState state, int blockCount) {
        this(state, blockCount, BlockMatcher.forBlock(Blocks.STONE));
    }

    public WorldGenMinableBase(IBlockState state, int blockCount, Predicate<IBlockState> predicate) {
        super(state, blockCount, predicate);
        this.blockCount = blockCount;
        oreState = state;
        this.predicate = predicate;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        float f = rand.nextFloat() * (float) Math.PI;
        double x1 = ((position.getX() + 8) + MathHelper.sin(f) * blockCount / 8.0F);
        double x2 = ((position.getX() + 8) - MathHelper.sin(f) * blockCount / 8.0F);
        double z1 = ((position.getZ() + 8) + MathHelper.cos(f) * blockCount / 8.0F);
        double z2 = ((position.getZ() + 8) - MathHelper.cos(f) * blockCount / 8.0F);
        double y1 = (position.getY() + rand.nextInt(3) - 2);
        double y2 = (position.getY() + rand.nextInt(3) - 2);

        Set<BlockPos> validBlockPositions = new HashSet<>(blockCount);

        for (int i = 0; i < blockCount; ++i) {
            float f1 = (float) i / blockCount;
            double x3 = x1 + (x2 - x1) * f1;
            double y3 = y1 + (y2 - y1) * f1;
            double z3 = z1 + (z2 - z1) * f1;
            double d9 = rand.nextDouble() * blockCount / 16.0D;
            double d10 = (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int minX = MathHelper.floor(x3 - d10 / 2.0D);
            int minY = MathHelper.floor(y3 - d10 / 2.0D);
            int minZ = MathHelper.floor(z3 - d10 / 2.0D);
            int maxX = MathHelper.floor(x3 + d10 / 2.0D);
            int maxY = MathHelper.floor(y3 + d10 / 2.0D);
            int maxZ = MathHelper.floor(z3 + d10 / 2.0D);

            BlockPos.MutableBlockPos posToReplace = new BlockPos.MutableBlockPos();
            for (int xBlock = minX; xBlock <= maxX; ++xBlock) {
                double xCheck = (xBlock + 0.5D - x3) / (d10 / 2.0D);

                if (xCheck * xCheck < 1.0D) {
                    for (int yBlock = minY; yBlock <= maxY; ++yBlock) {
                        double yCheck = (yBlock + 0.5D - y3) / (d10 / 2.0D);

                        if (xCheck * xCheck + yCheck * yCheck < 1.0D) {
                            for (int zBlock = minZ; zBlock <= maxZ; ++zBlock) {
                                posToReplace.setPos(xBlock, yBlock, zBlock);
                                if (!validBlockPositions.contains(posToReplace)) {
                                    double zCheck = (zBlock + 0.5D - z3) / (d10 / 2.0D);

                                    if (xCheck * xCheck + yCheck * yCheck + zCheck * zCheck < 1.0D) {
                                        validBlockPositions.add(posToReplace.toImmutable());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        validBlockPositions.stream()
          .filter(pos -> canGenerateInPosition(pos, world))
          .forEach(pos -> world.setBlockState(pos, oreState));
        return true;
    }

    /**
     * Only override this one if you are not calling the super method and are not getting the block state. If you are
     * calling the super method and obtaining the block state, use
     * {@link #canGenerateInPosition(BlockPos, IBlockState, IBlockAccess)}, as it already has obtained the block state
     * in this position.
     * @param position The position that is attempted to be generated in
     * @param world    The current world
     * @return Whether this position is valid for the block to generate in.
     */
    protected boolean canGenerateInPosition(BlockPos position, IBlockAccess world) {
        return canGenerateInPosition(position, world.getBlockState(position), world);
    }

    /**
     * Overload for {@link #canGenerateInPosition(BlockPos, IBlockAccess)} that already has obtained the block state.
     * This is called in the other canGenerateInPosition. Use this when you want a state-specific check and are
     * calling the super method.
     * @param position The position that is going to be generated in.
     * @param positionState The block state at this position.
     * @param world The world
     * @return Whether this position is valid for the block to generate in.
     */
    protected boolean canGenerateInPosition(BlockPos position, IBlockState positionState, IBlockAccess world) {
        return positionState.getBlock().isReplaceableOreGen(positionState, world, position, predicate);
    }
}
