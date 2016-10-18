package eiteam.esteemedinnovation.block;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.wrench.IWrenchable;
import eiteam.esteemedinnovation.api.block.BlockSteamTransporter;
import eiteam.esteemedinnovation.init.blocks.SteamNetworkBlocks;
import eiteam.esteemedinnovation.misc.FluidHelper;
import eiteam.esteemedinnovation.tile.TileEntityBoiler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBoiler extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyBool IS_ON = PropertyBool.create("on");
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockBoiler() {
        super(Material.IRON);
        setHardness(5F);
        setResistance(10F);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, IS_ON);
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
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityBoiler) {
            TileEntityBoiler boiler = (TileEntityBoiler) tile;
            return getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(IS_ON, boiler.isBurning());
        }
        return state;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile == null || !(tile instanceof TileEntityBoiler)) {
            return;
        }
        TileEntityBoiler boiler = (TileEntityBoiler) tile;
        if (boiler.isBurning()) {
            EnumFacing facing = state.getValue(FACING);
            float f = (float) pos.getX() + 0.5F;
            float f1 = (float) pos.getY() + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) pos.getZ() + 0.5F;
            float f3 = 0.52F;
            float f4 = rand.nextFloat() * 0.6F - 0.3F;

            double xCoord;
            double yCoord = (double) f1;
            double zCoord;

            switch (facing) {
                case WEST: {
                    xCoord = (double) f - f3;
                    zCoord = (double) f2 + f4;
                    break;
                }
                case EAST: {
                    xCoord = (double) f + f3;
                    zCoord = (double) f2 + f4;
                    break;
                }
                case NORTH: {
                    xCoord = (double) f + f4;
                    zCoord = (double) f2 - f3;
                    break;
                }
                case SOUTH: {
                    xCoord = (double) f + f4;
                    zCoord = (double) f2 + f3;
                    break;
                }
                default: {
                    return;
                }
            }

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xCoord, yCoord, zCoord, 0D, 0D, 0D);
            world.spawnParticle(EnumParticleTypes.FLAME, xCoord, yCoord, zCoord, 0D, 0D, 0D);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        setDefaultFacing(world, pos, state);
    }

    private void setDefaultFacing(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            IBlockState north = world.getBlockState(pos.north());
            IBlockState south = world.getBlockState(pos.south());
            IBlockState west = world.getBlockState(pos.west());
            IBlockState east = world.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            } else if (enumfacing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            } else if (enumfacing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            } else if (enumfacing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }

            world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBoiler();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityBoiler tileEntity = (TileEntityBoiler) world.getTileEntity(pos);

        boolean isClient = !world.isRemote;

        if (tileEntity != null) {
            if (!FluidHelper.playerIsHoldingWaterContainer(player) && isClient) {
                player.openGui(EsteemedInnovation.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            } else {
                FluidHelper.fillTankFromItem(heldItem, tileEntity.myTank, !player.capabilities.isCreativeMode);
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity != null && tileentity instanceof TileEntityBoiler) {
            InventoryHelper.dropInventoryItems(world, pos, (TileEntityBoiler) tileentity);
            world.updateComparatorOutputLevel(pos, state.getBlock());
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(SteamNetworkBlocks.Blocks.BOILER.getBlock());
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return true;
        } else if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
            world.setBlockState(pos, state.withProperty(FACING, facing.getOpposite()), 2);
        }
        return false;
    }
}
