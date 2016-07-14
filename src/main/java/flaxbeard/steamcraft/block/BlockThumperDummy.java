package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.init.blocks.SteamMachineryBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class BlockThumperDummy extends Block implements IWrenchable {
    public BlockThumperDummy() {
        super(Material.IRON);
        setHardness(3.5F);
    }

    @Override
    public int quantityDropped(Random rand) {
        return 0;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        Item item = Item.getItemFromBlock(SteamMachineryBlocks.Blocks.THUMPER.getBlock());
        return item == null ? null : new ItemStack(item, 1, 0);
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        int meta = getMetaFromState(state) - 1;
        BlockPos thumperPos = new BlockPos(pos.getX(), pos.getY() - meta, pos.getZ());
        IBlockState thumperState = world.getBlockState(thumperPos);
        if (thumperState.getBlock() == SteamMachineryBlocks.Blocks.THUMPER.getBlock() && facing != EnumFacing.UP &&
          facing != EnumFacing.DOWN) {
            world.setBlockState(thumperPos, thumperState.withProperty(BlockThumper.FACING, facing.getOpposite()), 2);
            return true;
        }
        return false;
    }

}
