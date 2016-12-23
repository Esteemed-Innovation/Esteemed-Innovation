package eiteam.esteemedinnovation.heater;

import eiteam.esteemedinnovation.api.wrench.IWrenchable;
import eiteam.esteemedinnovation.transport.steam.BlockSteamPipe;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import eiteam.esteemedinnovation.steamsafety.disc.BlockRuptureDisc;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockSteamHeater extends BlockSteamPipe implements IWrenchable {
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    // Actual states taken from BlockSteamPipe
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    private static final AxisAlignedBB HEATER_AABB = new AxisAlignedBB(0, 0, 11F / 16F, 1, 1, 1);

    public BlockSteamHeater() {
        super();
        setHardness(3.625F);
        setResistance(7.5F);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, NORTH, EAST, SOUTH, WEST, UP, DOWN);
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
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer)), 2);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySteamHeater();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        WorldHelper.rotateProperly(FACING, world, state, pos, facing);
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity) {
        super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity);
        AxisAlignedBB aabb = BlockRuptureDisc.getDirectionalBoundingBox(state.getValue(FACING), HEATER_AABB, true).offset(pos);
        if (aabb.intersectsWith(entityBox)) {
            collidingBoxes.add(aabb);
        }
    }
}