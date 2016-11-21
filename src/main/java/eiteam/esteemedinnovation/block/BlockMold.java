package eiteam.esteemedinnovation.block;

import eiteam.esteemedinnovation.api.mold.ICrucibleMold;
import eiteam.esteemedinnovation.api.wrench.IWrenchable;
import eiteam.esteemedinnovation.misc.WorldHelper;
import eiteam.esteemedinnovation.tile.TileEntityMold;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMold extends Block implements IWrenchable {
    private static final float px = (1.0F / 16.0F);
    public static PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockMold() {
        super(Material.ROCK);
        setHardness(3.5F);
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
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityMold tileentitymold = (TileEntityMold) world.getTileEntity(pos);

        if (tileentitymold != null) {
            InventoryHelper.dropInventoryItems(world, pos, tileentitymold);

            world.updateComparatorOutputLevel(pos, state.getBlock());
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMold();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(2 * px, 0.0F, 2 * px, 1.0F - 2 * px, 1.0F - 8 * px, 1.0F - 2 * px);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityMold tile = (TileEntityMold) world.getTileEntity(pos);
        if (tile == null) {
            return false;
        }
        boolean editingMold = false;
        if (heldItem != null) {
            if (heldItem.getItem() instanceof ICrucibleMold) {
                editingMold = true;
            }
        }
        if (player.isSneaking()) {
            editingMold = true;
        }
        if (editingMold) {
            if (tile.isOpen) {
                if (tile.mold != null) {
                    if (!world.isRemote) {
                        if (!player.capabilities.isCreativeMode) {
                            tile.dropItem(tile.mold);
                        }
                    }
                    tile.mold = null;
                    // markDirty
                }
                if (heldItem != null) {
                    if (heldItem.getItem() instanceof ICrucibleMold) {
                        tile.mold = heldItem;
                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                        // markDirty

                    }
                }
            }
            return true;
        } else {
            if (tile.changeTicks == 0 && (heldItem == null || !(heldItem.getItem() instanceof ItemBlock))) {
                tile.isOpen = !tile.isOpen;
                tile.changeTicks = 20;
                // markDirty
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
            WorldHelper.rotateProperly(FACING, world, state, pos, facing);
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
}
