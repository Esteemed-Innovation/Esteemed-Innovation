package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.util.UtilMisc;
import flaxbeard.steamcraft.init.misc.OreDictEntries;
import flaxbeard.steamcraft.tile.TileEntityRuptureDisc;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.BlockContainer;
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

public class BlockRuptureDisc extends BlockContainer {
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
        TileEntity tile = world.getTileEntity(pos.offset(state.getValue(FACING).getOpposite()));
        return state.withProperty(ON_PIPE, tile != null && tile instanceof TileEntitySteamPipe);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing dir) {
        TileEntity tile = world.getTileEntity(pos.offset(dir.getOpposite()));

        if (tile != null && tile instanceof ISteamTransporter) {
            ISteamTransporter trans = (ISteamTransporter) tile;
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
    public TileEntity createNewTileEntity(World world, int meta) {
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
        if (state.getValue(IS_BURST) && heldItem != null && UtilMisc.doesMatch(heldItem, OreDictEntries.PLATE_ZINC)) {
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

    // TODO: Move these to a dedicated place that is not BlockRuptureDisc.
    /**
     * Overload for getDirectionalBoundingBox using a base AABB instead of a bunch of floats.
     * @param dir The direction the block is facing
     * @param base The base AxisAlignedBB
     * @param allowsUpDown Whether it allows vertical rotation (facing UP or facing DOWN)
     * @return The rotated AABB.
     */
    public static AxisAlignedBB getDirectionalBoundingBox(EnumFacing dir, AxisAlignedBB base, boolean allowsUpDown) {
        return getDirectionalBoundingBox(dir, (float) base.minX, (float) base.minY, (float) base.minZ,
          (float) base.maxX, (float) base.maxY, (float) base.maxZ, allowsUpDown);
    }

    /**
     * Gets an AxisAlignedBB according to the provided direction (rotation).
     * @param dir The direction
     * @param minX Minimum X for the AABB
     * @param minY Minimum Y
     * @param minZ Minimum Z
     * @param maxX Maximum X
     * @param maxY Maximum Y
     * @param maxZ Maximum Z
     * @param allowsUpDown Whether it allows vertical rotation (facing UP and facing DOWN)
     * @return The rotated AABB.
     */
    public static AxisAlignedBB getDirectionalBoundingBox(EnumFacing dir, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, boolean allowsUpDown) {
        switch (dir) {
            case NORTH: {
                return new AxisAlignedBB(1 - maxX, minY, 1 - maxZ, 1 - minX, maxY, 1 - minZ);
            }
            case SOUTH: {
                return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
            }
            case EAST: {
                return new AxisAlignedBB(minZ, minY, minX, maxZ, maxY, maxX);
            }
            case WEST: {
                return new AxisAlignedBB(1 - maxZ, minY, 1 - maxX, 1 - minZ, maxY, 1 - minX);
            }
            case UP: {
                if (!allowsUpDown) {
                    break;
                }
                return new AxisAlignedBB(minX, minZ, minY, maxX, maxZ, maxY);
            }
            case DOWN: {
                if (!allowsUpDown) {
                    break;
                }
                return new AxisAlignedBB(minX, 1 - minZ, minY, maxX, 1 - maxZ, maxY);
            }
        }
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
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
        return getDirectionalBoundingBox(state.getValue(FACING), minX, minY, minZ, maxX, maxY, maxZ, true);
    }
}
