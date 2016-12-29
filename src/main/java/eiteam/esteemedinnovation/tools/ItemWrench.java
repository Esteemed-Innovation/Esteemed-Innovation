package eiteam.esteemedinnovation.tools;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.wrench.PipeWrench;
import eiteam.esteemedinnovation.api.wrench.Wrenchable;
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

public class ItemWrench extends Item implements PipeWrench {
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tile = world.getTileEntity(pos);
        EnumActionResult endResult = EnumActionResult.PASS;
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            boolean doBlock = block != null && block instanceof Wrenchable;
            boolean doTile = tile != null && tile instanceof Wrenchable;
            if (doBlock) {
                boolean result = ((Wrenchable) block).onWrench(stack, player, world, pos, hand, side, state, hitX, hitY, hitZ);
                if (result) {
                    world.playSound(player, pos, EsteemedInnovation.SOUND_WRENCH, SoundCategory.PLAYERS, 2F, 0.9F);
                    endResult = EnumActionResult.SUCCESS;
                }
            }
            if (doTile) {
                boolean result = ((Wrenchable) tile).onWrench(stack, player, world, pos, hand, side, state, hitX, hitY, hitZ);
                if (result) {
                    world.playSound(player, pos, EsteemedInnovation.SOUND_WRENCH, SoundCategory.PLAYERS, 2F, 0.9F);
                    endResult = EnumActionResult.SUCCESS;
                }
            }
        }
        return endResult;
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
