package flaxbeard.steamcraft.api;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author SatanicSanta
 *
 * Implement this interface on subclasses of Item to have the item work as pipe wrench.
 */
public interface IPipeWrench {

    /**
     * Called to ensure the pipe wrench can be used on a block.
     *
     * @param player - The player wrenching
     * @param x      - X coordinate for the block being wrenched
     * @param y      - Y coordinate for the block being wrenched
     * @param z      - Z coordinate for the block being wrenched
     *
     * @return true if wrenching is possible; false if not.
     */
    boolean canWrench(EntityPlayer player, int x, int y, int z);

    /**
     * Called after the pipe wrench has been used.
     *
     * @param player - The player wrenching
     * @param x      - X coordinate for the block being wrenched
     * @param y      - Y coordinate for the block being wrenched
     * @param z      - Z coordinate for the block being wrenched
     */
    void wrenchUsed(EntityPlayer player, int x, int y, int z);
}
