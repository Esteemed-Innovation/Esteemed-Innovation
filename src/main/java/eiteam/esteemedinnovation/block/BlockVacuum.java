package eiteam.esteemedinnovation.block;

import eiteam.esteemedinnovation.api.wrench.IWrenchable;
import eiteam.esteemedinnovation.api.block.BlockSteamTransporter;
import eiteam.esteemedinnovation.misc.WorldHelper;
import eiteam.esteemedinnovation.tile.TileEntityVacuum;
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

public class BlockVacuum extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    private static final AxisAlignedBB VACUUM_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 13F / 16F);

    public BlockVacuum() {
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
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityVacuum) {
            TileEntityVacuum tileentityvacuum = (TileEntityVacuum) tileEntity;
            boolean isPowered = world.isBlockPowered(pos);
            tileentityvacuum.updateRedstoneState(isPowered);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        neighborChanged(state, world, pos, null);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, elb)), 2);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityVacuum();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            WorldHelper.rotateProperly(FACING, world, state, pos, facing);
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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return BlockRuptureDisc.getDirectionalBoundingBox(state.getValue(FACING), VACUUM_AABB, true);
    }
}
