package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityFan;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFan extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    private static final AxisAlignedBB FAN_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 6F / 16F);

    public BlockFan() {
        super(Material.IRON);
        setHardness(3.5F);
        setResistance(7.5F);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityFan) {
            boolean poweredByRedstone = world.isBlockIndirectlyGettingPowered(pos) > 0;
            TileEntityFan fan = (TileEntityFan) tileEntity;
            fan.updateRedstoneState(poweredByRedstone);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        onNeighborChange(world, pos, null);
    }



    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, elb)), 2);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return BlockRuptureDisc.getDirectionalBoundingBox(state.getValue(FACING), FAN_AABB, true);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFan();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            EnumFacing curFacing = state.getValue(FACING);
            EnumFacing playerFacing = BlockPistonBase.getFacingFromEntity(pos, player);
            EnumFacing newFacing = curFacing == playerFacing ? playerFacing.getOpposite() : playerFacing;
            world.setBlockState(pos, state.withProperty(FACING, newFacing), 2);
        }
        return true;
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
}
