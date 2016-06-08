package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.IPipeWrench;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWrench extends Item implements IPipeWrench {
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tile = world.getTileEntity(pos);
        if (!world.isRemote || (tile != null && tile instanceof TileEntitySteamPipe && !player.isSneaking())) {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block != null && block instanceof IWrenchable) {
                boolean result = ((IWrenchable) block).onWrench(stack, player, world, pos, hand, side, state, hitX, hitY, hitZ);
                if (tile != null && tile instanceof IWrenchable) {
                    ((IWrenchable) tile).onWrench(stack, player, world, pos, hand, side, state, hitX, hitY, hitZ);
                }
                if (result) {
                    world.playSound(player, pos, Steamcraft.SOUND_WRENCH, SoundCategory.PLAYERS, 2F, 0.9F);
                    return EnumActionResult.SUCCESS;
                }
            } else if (tile != null && tile instanceof IWrenchable) {
                boolean result = ((IWrenchable) tile).onWrench(stack, player, world, pos, hand, side, state, hitX, hitY, hitZ);
                if (result) {
                    world.playSound(player, pos, Steamcraft.SOUND_WRENCH, SoundCategory.PLAYERS, 2F, 0.9F);
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.FAIL;
    }

    @Override
    public boolean canWrench(EntityPlayer player, BlockPos pos){
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand){
        player.swingArm(hand);
    }
}
