package eiteam.esteemedinnovation.api.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;

/**
 * Base TileEntity class that ensures that no tick code is executed when the block in its occupied position is not
 * the expected one (usually, when this issue arises, it is air.)
 *
 * It implements ITickable, but children of this class should implement safeUpdate(), not update().
 *
 * This should only be necessary to use (as an alternative to TileEntityBase and ITickable explicitly) when you need to
 * access block state values within the update code.
 */
public abstract class TileEntityTickableSafe extends TileEntityBase implements ITickable {
    /**
     * @param target The block state that is in the position of this tile entity.
     * @return Whether we are safe to execute the ticking code for the provided block state.
     */
    public abstract boolean canUpdate(IBlockState target);

    /**
     * A safe variant of {@link ITickable#update()}. It is only executed when {@link #canUpdate(IBlockState)} is true.
     */
    public abstract void safeUpdate();

    @Override
    public void update() {
        if (canUpdate(worldObj.getBlockState(pos))) {
            safeUpdate();
        }
    }
}
