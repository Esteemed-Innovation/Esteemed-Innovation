package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.tile.TileEntityValvePipe;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockValvePipe extends BlockPipe {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityValvePipe &&
          world.getBlockState(neighbor).canProvidePower()) {
            TileEntityValvePipe valve = (TileEntityValvePipe) tileEntity;
            boolean isNotPowered = tileEntity.getWorld().isBlockIndirectlyGettingPowered(pos) <= 0;
            valve.updateRedstoneState(isNotPowered);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityValvePipe();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem == null || !(heldItem.getItem() instanceof ItemBlock)) {
            TileEntityValvePipe tile = (TileEntityValvePipe) world.getTileEntity(pos);
            if (tile != null && !tile.isTurning()) {
                tile.setTurning();
            }
        }
        return true;
    }
}
