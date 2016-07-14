package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityVacuum;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVacuum extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockVacuum() {
        super(Material.IRON);
        setHardness(3.5F);
        setResistance(7.5F);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityVacuum) {
            TileEntityVacuum tileentityvacuum = (TileEntityVacuum) tileEntity;
            boolean isPowered = tileentityvacuum.getWorld().isBlockIndirectlyGettingPowered(pos) > 0;
            tileentityvacuum.updateRedstoneState(isPowered);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        onNeighborChange(world, pos, null);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityVacuum();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING) == facing ? facing.getOpposite() : facing), 2);
        }
        return true;
    }
}
