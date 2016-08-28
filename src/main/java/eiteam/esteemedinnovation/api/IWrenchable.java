package eiteam.esteemedinnovation.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implement this when you want your machine to do something when it gets wrenched.
 */
public interface IWrenchable {
    /**
     * Called when a player uses a wrench on a tile entity.
     * @param stack The ItemStack wrench
     * @param player The player
     * @param world The world
     * @param pos The block position
     * @param hand The hand being used
     * @param facing The side of the block hit
     * @param state The current blockstate.
     * @param hitX See Item#onItemUseFirst
     * @param hitY See Item#onItemUseFirst
     * @param hitZ See Item#onItemUseFirst
     * @return Whether it was successful
     */
    boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ);
}
