package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityFluidSteamConverter;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidSteamConverter extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final float RING_MIN = 4F / 16F;
    private static final float RING_MAX = 12F / 16F;

    public BlockFluidSteamConverter() {
        super(Material.IRON);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        final float z2 = 0.999F;
        EnumFacing dir = state.getValue(FACING).getOpposite();
        //Steamcraft.log.debug(meta);
        switch (dir) {
            case DOWN: {
                return new AxisAlignedBB(x + RING_MIN, y, z + RING_MIN, x + RING_MAX, y + z2, z + RING_MAX);
            }
            case UP: {
                return new AxisAlignedBB(x + RING_MIN, y, z + RING_MIN, x + RING_MAX, y + z2, z + RING_MAX);
            }
            case WEST: {
                return new AxisAlignedBB(x, y + RING_MIN, z + RING_MIN, x + z2, y + RING_MAX, z + RING_MAX);
            }
            case NORTH: {
                return new AxisAlignedBB(x + 1 - RING_MAX, y + RING_MIN, z + 1 - z2, x + 1 - RING_MIN, y + RING_MAX, z + 1);
            }
            case EAST: {
                return new AxisAlignedBB(x + 1 - RING_MAX, y + RING_MIN, z + 1 - RING_MAX, x + 1, y + RING_MAX, z + 1 - RING_MIN);
            }
            case SOUTH: {
                return new AxisAlignedBB(x + RING_MIN, y + RING_MIN, z, x + RING_MAX, y + RING_MAX, z + z2);
            }
        }
        return super.getCollisionBoundingBox(state, world, pos);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFluidSteamConverter();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            EnumFacing curFacing = state.getValue(FACING);
            EnumFacing playerFacing = player.getHorizontalFacing();
            EnumFacing newFacing = curFacing == playerFacing ? player.getHorizontalFacing().getOpposite() : playerFacing;
            world.setBlockState(pos, state.withProperty(FACING, newFacing));
        } else {
            // TODO: Mark dirty/mark for update?
        }
        return true;
    }
}
