package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.tile.TileEntityMold;
import net.minecraft.block.BlockContainer;
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

public class BlockMold extends BlockContainer implements IWrenchable {
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
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMold();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new AxisAlignedBB(x + 2 * px, y + 0.0F, z + 2 * px, x + 1.0F - 2 * px, y + 1.0F - 8 * px, z + 1.0F - 2 * px);
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
        } else {
            if (tile.changeTicks == 0 && (heldItem == null || !(heldItem.getItem() instanceof ItemBlock))) {
                tile.isOpen = !tile.isOpen;
                tile.changeTicks = 20;
                // markDirty
            }
        }

        return false;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
            EnumFacing output = facing;
            switch (facing) {
                case NORTH: {
                    output = EnumFacing.NORTH;
                    break;
                }
                case SOUTH: {
                    output = EnumFacing.DOWN;
                    break;
                }
                case WEST: {
                    output = EnumFacing.UP;
                    break;
                }
                case EAST: {
                    output = EnumFacing.SOUTH;
                    break;
                }
            }
            if (output == facing && facing.getIndex() > 1 && facing.getIndex() < 6) {
                switch (facing.getOpposite()) {
                    case NORTH: {
                        output = EnumFacing.NORTH;
                        break;
                    }
                    case SOUTH: {
                        output = EnumFacing.DOWN;
                        break;
                    }
                    case WEST: {
                        output = EnumFacing.UP;
                        break;
                    }
                    case EAST: {
                        output = EnumFacing.SOUTH;
                        break;
                    }
                }
            }
            world.setBlockState(pos, state.withProperty(FACING, output), 2);
            return true;
        }
        return false;
    }
}
