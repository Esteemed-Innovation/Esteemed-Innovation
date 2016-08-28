package eiteam.esteemedinnovation.api.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

/**
 * Base tile entity class, mainly so we can have handy helper methods across all of our TEs.
 */
public class TileEntityBase extends TileEntity {
    /**
     * Marks the tile entity for a server -> client resync, with a specific old state.
     * @param old The old state
     */
    protected void markForResync(IBlockState old) {
        worldObj.notifyBlockUpdate(pos, old, worldObj.getBlockState(pos), 0);
    }

    /**
     * Marks the tile entity for a server -> client resync, using the same blockstate for both the "newState" and "oldState" parameters.
     */
    protected void markForResync() {
        markForResync(worldObj.getBlockState(pos));
    }
}
