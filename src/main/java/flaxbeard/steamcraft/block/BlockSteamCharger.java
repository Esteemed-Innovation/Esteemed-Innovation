package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.init.items.tools.GadgetItems;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.tools.ToolCore;

public class BlockSteamCharger extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockSteamCharger() {
        super(Material.IRON);
        setHardness(3.5F);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return new AxisAlignedBB(pos, new BlockPos(pos.getX() + 1, pos.getY() + 0.5F, pos.getZ() + 1));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()));
    }

    /**
     * Whether the item is chargeable (and can be charged) or it is an empty cell item.
     * @param item The ItemStack to check.
     * @return Whether the item can be placed in the TileEntity's inventory.
     */
    private boolean canItemBeCharged(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.getItem() instanceof ISteamChargable) {
            return ((ISteamChargable) item.getItem()).canCharge(item);
        } else {
            if (CrossMod.TINKERS_CONSTRUCT && item.getItem() instanceof ToolCore) {
                NBTTagCompound nbt = item.getTagCompound();
                return nbt.getCompoundTag("InfiTool").hasKey("Steam");
            }
            return item.getItem() == GadgetItems.Items.STEAM_CELL_EMPTY.getItem();
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntitySteamCharger tile = (TileEntitySteamCharger) world.getTileEntity(pos);
        if (tile == null) {
            return false;
        }
        ItemStack stackInSlot = tile.getStackInSlot(0);
        if (stackInSlot != null) {
            if (!world.isRemote) {
                tile.dropItem(stackInSlot);
            }
            tile.setInventorySlotContents(0, null);
        } else {
            if (canItemBeCharged(heldItem)) {
                ItemStack copy = heldItem.copy();
                copy.stackSize = 1;
                tile.setInventorySlotContents(0, copy);
                heldItem.stackSize -= 1;
                tile.randomDegrees = world.rand.nextInt(361);
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySteamCharger();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntitySteamCharger tileentitysteamcharger = (TileEntitySteamCharger) world.getTileEntity(pos);

        if (tileentitysteamcharger != null) {
            InventoryHelper.dropInventoryItems(world, pos, tileentitysteamcharger);
            world.updateComparatorOutputLevel(pos, state.getBlock());
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
            world.setBlockState(pos, state.withProperty(FACING, facing.getOpposite()), 2);
            return true;
        }
        return false;

    }
}