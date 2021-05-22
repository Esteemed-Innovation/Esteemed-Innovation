package eiteam.esteemedinnovation.steamsafety.whistle;

import eiteam.esteemedinnovation.api.SteamTransporter;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import eiteam.esteemedinnovation.steamsafety.disc.BlockRuptureDisc;
import eiteam.esteemedinnovation.transport.steam.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWhistle extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ON_PIPE = PropertyBool.create("on_pipe");

    public BlockWhistle() {
        super(Material.IRON);
        setHardness(1F);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ON_PIPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = WorldHelper.getTileEntitySafely(world, pos.offset(state.getValue(FACING).getOpposite()));
        return state.withProperty(ON_PIPE, tile instanceof TileEntitySteamPipe);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing dir) {
        if (dir == EnumFacing.UP || dir == EnumFacing.DOWN) {
            return false;
        }
        TileEntity tile = world.getTileEntity(pos.offset(dir.getOpposite()));

        if (tile != null && tile instanceof SteamTransporter) {
            SteamTransporter trans = (SteamTransporter) tile;
            return trans.acceptsGauge(dir.getOpposite());
        }
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if (neighbor != pos) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null) {
                World actualWorld = tileEntity.getWorld();
                IBlockState state = world.getBlockState(pos);
                EnumFacing dir = state.getValue(FACING);
                if (!canPlaceBlockOnSide(actualWorld, pos, dir)) {
                    dropBlockAsItem(actualWorld, pos, state, 0);
                    actualWorld.setBlockToAir(pos);
                }
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, meta, placer).withProperty(FACING, side);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityWhistle();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        boolean pipe = state.getActualState(source, pos).getValue(ON_PIPE);
        float minX = 6.5F * BlockRuptureDisc.UNIT;
        float minY = 6.5F * BlockRuptureDisc.UNIT;
        float minZ = (pipe ? -4F : 0F) * BlockRuptureDisc.UNIT;
        float maxX = 9.5F * BlockRuptureDisc.UNIT;
        float maxY = 16F * BlockRuptureDisc.UNIT;
        float maxZ = (pipe ? 1.5F : 5.5F) * BlockRuptureDisc.UNIT;
        return WorldHelper.getDirectionalBoundingBox(state.getValue(FACING), minX, minY, minZ, maxX, maxY, maxZ, false);
    }
}
