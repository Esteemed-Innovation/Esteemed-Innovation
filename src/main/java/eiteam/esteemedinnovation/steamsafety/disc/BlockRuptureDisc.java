package eiteam.esteemedinnovation.steamsafety.disc;

import eiteam.esteemedinnovation.api.SteamTransporter;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import eiteam.esteemedinnovation.commons.OreDictEntries;
import eiteam.esteemedinnovation.transport.steam.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRuptureDisc extends Block {
    public static final PropertyBool IS_BURST = PropertyBool.create("is_burst");
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool ON_PIPE = PropertyBool.create("on_pipe");
    public static final float UNIT = 1F / 16F;

    public BlockRuptureDisc() {
        super(Material.IRON);
        setHardness(1F);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_BURST, FACING, ON_PIPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
          .withProperty(IS_BURST, (meta & 8) != 0)
          .withProperty(FACING, EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int burst = state.getValue(IS_BURST) ? 1 : 0;
        int dir = state.getValue(FACING).getIndex();
        return burst << 3 | dir;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = WorldHelper.getTileEntitySafely(world, pos.offset(state.getValue(FACING).getOpposite()));
        return state.withProperty(ON_PIPE, tile instanceof TileEntitySteamPipe);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing dir) {
        TileEntity tile = world.getTileEntity(pos.offset(dir.getOpposite()));

        if (tile != null && tile instanceof SteamTransporter) {
            SteamTransporter trans = (SteamTransporter) tile;
            return trans.acceptsGauge(dir.getOpposite());
        }
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess iba, BlockPos pos, BlockPos neighbor) {
        TileEntity tileEntity = iba.getTileEntity(pos);
        if (neighbor != pos && tileEntity != null) {
            IBlockState state = iba.getBlockState(pos);
            World world = tileEntity.getWorld();
            if (!canPlaceBlockOnSide(world, pos, state.getValue(FACING))) {
                dropBlockAsItem(world, pos, state, 0);
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer)
            .withProperty(IS_BURST, meta == 1)
            .withProperty(FACING, facing);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityRuptureDisc();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return null;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(IS_BURST) ? 1 : 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (state.getValue(IS_BURST) && heldItem != null && ItemStackUtility.isItemOreDictedAs(heldItem, OreDictEntries.PLATE_THIN_ZINC)) {
            state.cycleProperty(IS_BURST);
            if (!player.capabilities.isCreativeMode) {
                heldItem.stackSize -= 1;
            }
            return true;
        }
        return false;
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        boolean pipe = state.getActualState(source, pos).getValue(ON_PIPE);
        float minX = 4 * UNIT;
        float minY = 4 * UNIT;
        float minZ = pipe ? -5 * UNIT : 0F;
        float maxX = 12 * UNIT;
        float maxY = 12 * UNIT;
        float maxZ = pipe ? -2 * UNIT + 0.0005F : 3 * UNIT;
        return WorldHelper.getDirectionalBoundingBox(state.getValue(FACING), minX, minY, minZ, maxX, maxY, maxZ, true);
    }
}
