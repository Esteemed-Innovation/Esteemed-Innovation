package eiteam.esteemedinnovation.pendulum;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPendulumString extends Block {
    static final AxisAlignedBB AABB = new AxisAlignedBB(0.4000000059604645D, 0D, 0.4000000059604645D, 0.6000000238418579D, 1D, 0.6000000238418579D);
    public BlockPendulumString() {
        super(Material.CIRCUITS);
    }

    @Override
    public void neighborChanged(IBlockState self, World world, BlockPos selfPos, Block neighborBlock, BlockPos fromPos) {
        if (canSelfBeRemoved(world, selfPos, EnumFacing.UP) || canSelfBeRemoved(world, selfPos, EnumFacing.DOWN)) {
            world.setBlockToAir(selfPos);
        }
    }

    private static boolean canSelfBeRemoved(IBlockAccess world, BlockPos selfPosition, EnumFacing inDirection) {
        Block block = world.getBlockState(selfPosition.offset(inDirection)).getBlock();
        return (block != PendulumModule.PENDULUM_TORCH && block != PendulumModule.PENDULUM_STRING) &&
          !world.getBlockState(selfPosition.offset(inDirection)).isSideSolid(world, selfPosition.offset(inDirection), inDirection.getOpposite());
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
