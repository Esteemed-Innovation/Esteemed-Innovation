package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks;
import flaxbeard.steamcraft.tile.TileEntityThumper;
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

public class BlockThumper extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockThumper() {
        super(Material.IRON);
        setHardness(3.5F);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        BlockPos above = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        TileEntityThumper tile = (TileEntityThumper) world.getTileEntity(pos);
        if (world.getBlockState(above).getBlock() == SteamMachineryBlocks.Blocks.THUMPER_DUMMY.getBlock() && tile != null) {
            World actualWorld = tile.getWorld();
            if (!actualWorld.isRemote) {
                dropBlockAsItem(actualWorld, pos, getDefaultState(), 0);
            }
            actualWorld.setBlockToAir(pos);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        for (int yAdd = 0; yAdd < 4; yAdd++) {
            BlockPos dummyPos = new BlockPos(pos.getX(), pos.getY() + yAdd, pos.getZ());
            if (world.getBlockState(dummyPos).getBlock() == SteamMachineryBlocks.Blocks.THUMPER_DUMMY.getBlock()) {
                world.setBlockToAir(dummyPos);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase elb, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, elb.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityThumper();
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
