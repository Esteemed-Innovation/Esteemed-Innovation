package eiteam.esteemedinnovation.converter;

import eiteam.esteemedinnovation.api.block.BlockSteamTransporter;
import eiteam.esteemedinnovation.api.wrench.IWrenchable;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
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

public class BlockFluidSteamConverter extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    private static final float RING_MIN = 2.5F / 16F;
    private static final float RING_MAX = 13.5F / 16F;
    private static final AxisAlignedBB AABB = new AxisAlignedBB(RING_MIN, RING_MIN, 0, RING_MAX, RING_MAX, 1);

    public BlockFluidSteamConverter() {
        super(Material.IRON);
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing dir = state.getValue(FACING);
        return WorldHelper.getDirectionalBoundingBox(dir, AABB, true);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, elb).getOpposite()));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityFluidSteamConverter();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            WorldHelper.rotateProperly(FACING, world, state, pos, facing);
        } else {
            // TODO: Mark dirty/mark for update?
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
