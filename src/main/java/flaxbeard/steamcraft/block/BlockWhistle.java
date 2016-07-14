package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityWhistle;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWhistle extends BlockContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockWhistle() {
        super(Material.IRON);
        setHardness(1F);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
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
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing dir) {
        TileEntity tile = null;
        switch (dir) {
            case NORTH: {
                tile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1));
                break;
            }
            case SOUTH: {
                tile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1));
                break;
            }
            case WEST: {
                tile = world.getTileEntity(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()));
                break;
            }
            case EAST: {
                tile = world.getTileEntity(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()));
            }
        }

        if (tile != null && tile instanceof ISteamTransporter) {
            ISteamTransporter trans = (ISteamTransporter) tile;
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
                if (canPlaceBlockOnSide(actualWorld, pos, dir)) {
                    dropBlockAsItem(actualWorld, pos, state, 0);
                    actualWorld.setBlockToAir(pos);
                }
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getStateFromMeta(meta == 1 ? side.ordinal() + 10 : side.ordinal());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityWhistle();
    }
}
