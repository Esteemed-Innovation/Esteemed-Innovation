package flaxbeard.steamcraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks.Blocks.ROCK_SMASHER;

public class BlockDummy extends BlockContainer {
    public BlockDummy() {
        super(Material.IRON);
        setHardness(50.0F);
        setLightOpacity(0);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata){
        return new TileEntityDummyBlock();
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        int smasherCount = 0;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        smasherCount += world.getBlockState(new BlockPos(x + 1, y, z)).getBlock() == ROCK_SMASHER.getBlock() ? 1 : 0;
        smasherCount += world.getBlockState(new BlockPos(x - 1, y, z)) == ROCK_SMASHER.getBlock() ? 1 : 0;
        smasherCount += world.getBlockState(new BlockPos(x, y, z + 1)) == ROCK_SMASHER.getBlock() ? 1 : 0;
        smasherCount += world.getBlockState(new BlockPos(x, y, z - 1)) == ROCK_SMASHER.getBlock() ? 1 : 0;

        // The TileEntity simple acts as a middleman to get the mutable World object.
        TileEntity tile = world.getTileEntity(pos);
        if (smasherCount < 2 && tile != null) {
            tile.getWorld().setBlockToAir(pos);
        }
    }

    @Override
    public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos){
        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player){
        return false;
    }
}
