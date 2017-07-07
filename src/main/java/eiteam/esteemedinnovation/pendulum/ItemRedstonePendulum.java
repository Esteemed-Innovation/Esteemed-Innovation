package eiteam.esteemedinnovation.pendulum;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRedstonePendulum extends ItemBlock {
    public ItemRedstonePendulum(Block block) {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos actualPos = pos.down();
        if (facing != EnumFacing.DOWN || !player.canPlayerEdit(actualPos, facing, stack)) {
            return EnumActionResult.FAIL;
        }
        IBlockState stateInPlaced = world.getBlockState(actualPos);
        if (stateInPlaced.getMaterial() != Material.AIR) {
            return EnumActionResult.FAIL;
        }

        // Technically, this immediately gets set back to air. However the post-place logic will replace it in the
        // correct position. See BlockPendulumTorch for more information.
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
}
