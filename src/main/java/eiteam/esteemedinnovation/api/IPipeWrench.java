package eiteam.esteemedinnovation.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * Implement this interface on subclasses of Item to have the item work as pipe wrench.
 */
public interface IPipeWrench {

    /**
     * Called to ensure the pipe wrench can be used on a block.
     *
     * @param player The player wrenching
     * @param pos The position of the block being wrenched
     *
     * @return true if wrenching is possible; false if not.
     */
    boolean canWrench(EntityPlayer player, BlockPos pos);

    /**
     * Called after the pipe wrench has been used.
     *
     * @param player The player wrenching
     * @param pos The position of the block being wrenched
     * @param hand The hand holding the wrench.
     */
    void wrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand);
}
