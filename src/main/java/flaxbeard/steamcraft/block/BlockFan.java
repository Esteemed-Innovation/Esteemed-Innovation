package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityFan;
import net.minecraft.block.Block;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFan extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

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
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityFan) {
            boolean poweredByRedstone = tileEntity.getWorld().isBlockIndirectlyGettingPowered(pos) > 0;
            TileEntityFan fan = (TileEntityFan) tileEntity;
            fan.updateRedstoneState(poweredByRedstone);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        onNeighborChange(world, pos, null);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        EnumFacing dir = state.getValue(FACING);
        float x1 = pos.getX() + (dir.getFrontOffsetX() < 0 ? 0.625F : 0);
        float y1 = pos.getY() + (dir.getFrontOffsetY() < 0 ? 0.625F : 0);
        float z1 = pos.getZ() + (dir.getFrontOffsetZ() < 0 ? 0.625F : 0);
        float x2 = pos.getX() + 1F + (dir.getFrontOffsetX() > 0 ? -0.625F : 0);
        float y2 = pos.getY() + 1F + (dir.getFrontOffsetY() > 0 ? -0.625F : 0);
        float z2 = pos.getZ() + 1F + (dir.getFrontOffsetZ() > 0 ? -0.625F : 0);
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFan();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            EnumFacing curFacing = state.getValue(FACING);
            EnumFacing playerFacing = player.getHorizontalFacing();
            EnumFacing newFacing = curFacing == playerFacing ? playerFacing.getOpposite() : playerFacing;
            world.setBlockState(pos, state.withProperty(FACING, newFacing), 2);
        }
        return true;
    }
}
