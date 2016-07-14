package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySteamGauge;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSteamGauge extends BlockContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockSteamGauge() {
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
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntitySteamGauge) {
            return ((TileEntitySteamGauge) te).getComparatorOutput();
        }
        return 0;
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
        return getDefaultState().withProperty(FACING, side);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySteamGauge();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return null;
    }

    /*
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xf, float yf, float zf){
    	TileEntitySteamGauge gauge = (TileEntitySteamGauge)world.getTileEntity(x, y, z);
    	int pressurePerc = (int)(gauge.getPressure() * 100);
    	if (world.isRemote){
    		String color = "";

    		if (pressurePerc > 120){
    			color = "�4";
    		} else if (pressurePerc > 115){
    			color = "�c";
    		} else if (pressurePerc > 100){
    			color = "�e";
    		}

    		player.addChatComponentMessage(new ChatComponentText(color+"Current pressure: "+pressurePerc+"%"));

    	}

    	return true;
    }
    */
}
