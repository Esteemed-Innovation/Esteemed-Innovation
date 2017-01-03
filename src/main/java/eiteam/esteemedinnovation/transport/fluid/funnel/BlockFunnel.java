package eiteam.esteemedinnovation.transport.fluid.funnel;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFunnel extends Block {
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.UP);

    public BlockFunnel() {
        super(Material.ANVIL);
        setCreativeTab(EsteemedInnovation.tab);
        setDefaultState(blockState.getBaseState().withProperty(POWERED, false).withProperty(FACING, EnumFacing.DOWN));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean powered = false;
        TileEntityFunnel funnel = (TileEntityFunnel) WorldHelper.getTileEntitySafely(world, pos);
        if (funnel != null) {
            powered = funnel.getWorld().isBlockPowered(pos);
        }
        return state.withProperty(POWERED, powered);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing opposite = facing.getOpposite();
        return getDefaultState().withProperty(FACING, opposite == EnumFacing.UP ? EnumFacing.DOWN : opposite);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityFunnel();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
