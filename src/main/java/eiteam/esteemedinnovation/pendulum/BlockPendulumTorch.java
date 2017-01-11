package eiteam.esteemedinnovation.pendulum;

import eiteam.esteemedinnovation.commons.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPendulumTorch extends Block {
    // Taken from BlockTorch
    static final AxisAlignedBB AABB = new AxisAlignedBB(0.4000000059604645D, 0.3999999762D, 0.4000000059604645D, 0.6000000238418579D, 1D, 0.6000000238418579D);
    static final PropertyBool IS_LIT = PropertyBool.create("lit");

    public BlockPendulumTorch() {
        super(Material.CIRCUITS);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos basePos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockToAir(basePos);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(basePos);
        while (world.getBlockState(pos.down()).getMaterial() == Material.AIR) {
            world.setBlockState(pos, PendulumModule.PENDULUM_STRING.getDefaultState());
            pos.move(EnumFacing.DOWN);
        }
        world.setBlockState(pos, state);
    }


    @Override
    public void neighborChanged(IBlockState selfState, World world, BlockPos selfPos, Block neighbor) {
        if (world.getBlockState(selfPos.up()).getMaterial() == Material.AIR) {
            dropBlockAsItem(world, selfPos, selfState, 0);
            world.setBlockToAir(selfPos);
        }
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return 0;
        }
        TileEntityPendulumTorch tile = (TileEntityPendulumTorch) WorldHelper.getTileEntitySafely(blockAccess, pos);
        return tile.canProvideWeakPower() ? 15 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityPendulumTorch();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_LIT);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = WorldHelper.getTileEntitySafely(world, pos);
        boolean isLit = false;
        if (tile instanceof TileEntityPendulumTorch) {
            TileEntityPendulumTorch tept = (TileEntityPendulumTorch) tile;
            isLit = tept.canProvideWeakPower();
        }
        return state.withProperty(IS_LIT, isLit);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (getActualState(state, world, pos).getValue(IS_LIT)) {
            double x = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double y = pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
            double z = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            world.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0D, 0D, 0D);
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getActualState(state, world, pos).getValue(IS_LIT) ? 7 : 0;
    }
}
