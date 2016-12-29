package eiteam.esteemedinnovation.hammer;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSteamHammer extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockSteamHammer() {
        super(Material.IRON);
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
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return null;
    }

    /*@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        TileEntitySteamCharger tile = (TileEntitySteamCharger) world.getTileEntity(x,y,z);
        if (tile.getStackInSlot(0) != null) {
            if (!world.isRemote ) {
                tile.dropItem(tile.getStackInSlot(0));
            }
            tile.setInventorySlotContents(0, null);
        }
        else
        {
            if (player.getHeldItem() != null) {
                if (player.getHeldItem().getItem() instanceof SteamChargable) {
                    SteamChargable item = (SteamChargable) player.getHeldItem().getItem();
                    if (item.canCharge(player.getHeldItem())) {
                        ItemStack copy = player.getCurrentEquippedItem().copy();
                        copy.stackSize = 1;
                        tile.setInventorySlotContents(0, copy);
                        player.getCurrentEquippedItem().stackSize -= 1;
                        tile.randomDegrees = world.rand.nextInt(361);
                    }
                }
            }
        }
        return false;
    }
    */

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        BlockPos under = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        IBlockState stateUnder = world.getBlockState(under);
        Block blockUnder = stateUnder.getBlock();
        if (blockUnder == Blocks.ANVIL) {
            if (!world.isRemote) {
                TileEntitySteamHammer tileentityfurnace = (TileEntitySteamHammer) world.getTileEntity(pos);
                if (tileentityfurnace != null) {
                    player.openGui(EsteemedInnovation.instance, 3, world, pos.getX(), pos.getY(), pos.getZ());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySteamHammer();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntitySteamHammer tileentitysteamhammer = (TileEntitySteamHammer) world.getTileEntity(pos);
        if (tileentitysteamhammer != null) {
            InventoryHelper.dropInventoryItems(world, pos, tileentitysteamhammer);
            world.updateComparatorOutputLevel(pos, state.getBlock());
        }
        super.breakBlock(world, pos, state);
    }
}