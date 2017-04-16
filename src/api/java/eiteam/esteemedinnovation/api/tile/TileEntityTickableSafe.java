package eiteam.esteemedinnovation.api.tile;

import eiteam.esteemedinnovation.api.wrench.PipeWrench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * Base TileEntity class that ensures that no tick code is executed when the block in its occupied position is not
 * the expected one (usually, when this issue arises, it is air.)
 * <p>
 * It implements {@link ITickable}, but children of this class should implement {@link #safeUpdate()}, not {@link #update()}.
 * <p>
 * This should only be necessary to use (as an alternative to {@link TileEntityBase} and ITickable explicitly) when you need to
 * access block state values within the update code.
 */
public abstract class TileEntityTickableSafe extends TileEntityBase implements ITickable {
    private boolean isInitialized;

    /**
     * @param target The block state that is in the position of this tile entity.
     * @return Whether we are safe to execute the ticking code for the provided block state.
     */
    public abstract boolean canUpdate(IBlockState target);

    /**
     * A safe variant of {@link ITickable#update()}. It is only executed when {@link #canUpdate(IBlockState)} is true.
     */
    public abstract void safeUpdate();

    /**
     * Like {@link TileEntity#onLoad()}, except that it is safe (see {@link #safeUpdate()}) and happens after the world
     * has loaded. onLoad() occurs during the world loading process, so you cannot access any blockstate values or
     * anything of that sort. This is designed to be used for initialization stuff.
     * <p>
     * This is called before {@link #safeUpdate()} is called.
     * <p>
     * You must either call the supermethod or {@link #setInitialized(boolean)} in your implementation, or at some
     * point during update or safeUpdate, otherwise this method will repeatedly get called.
     * <p>
     * This is not necessarily the first update. Rather, it is the first update after the TE has been uninitialized. It
     * just happens to also begin uninitialized. It truly is called whenever an update occurs and the TE is not
     * initialized (see {@link #isInitialized()}).
     * <p>
     * For example, you might have a device that sets some value in the TE based on the block's facing value. The
     * facing value probably won't change, so it doesn't make sense to retrieve that value every update. Using this,
     * you would store the value once, and then simply use it in {@link #safeUpdate()} when you need. However, if you
     * had some custom behavior utilizing a {@link PipeWrench} that would rotate the block, you would need to reset
     * that value in the TE. You can do that by having a proxy method in the TE that calls {@link #setInitialized(boolean)}
     * (since that method is protected for security reasons; more on that later) so that the next update resets the
     * value in the TE.
     * <p>
     * Here is a code sample for the scenario previously described:
     * <pre>
     * <code>
     *     // SomeTileEntity.java
     *     class SomeTileEntity extends TileEntityTickableSafe {
     *         private EnumFacing facing;
     *
     *         {@literal @}Override
     *         public void initialUpdate() {
     *             super.initialUpdate();
     *             facing = worldObj.getBlockState(pos).getValue(SomeBlock.FACING);
     *         }
     *
     *         {@literal @}Override
     *         public void safeUpdate() {
     *             // Destroys the block in the direction it is facing, for example.
     *             worldObj.destroyBlock(pos.offset(facing), true);
     *         }
     *
     *         void uninitialize() {
     *             setInitialized(false);
     *         }
     *     }
     *
     *     // SomeBlock.java, in same package
     *     class SomeBlock extends Block implements Wrenchable {
     *         static final PropertyDirection FACING = BlockDirectional.FACING;
     *
     *         // IBlockState implementations
     *
     *         {@literal @}Override
     *         public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
     *             // Rotate the block
     *             world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).rotateY()));
     *             TileEntity te = world.getTileEntity(pos);
     *             if (te instanceof SomeTileEntity) {
     *                 // Uninitialize the tile entity since our facing value has changed.
     *                 ((SomeTileEntity) te).uninitialize();
     *             }
     *         }
     *     }
     * </code>
     * </pre>
     */
    public void initialUpdate() {
        setInitialized(true);
    }

    protected void setInitialized(boolean value) {
        isInitialized = value;
    }

    protected boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void update() {
        if (canUpdate(worldObj.getBlockState(pos))) {
            if (!isInitialized()) {
                initialUpdate();
            }
            safeUpdate();
        }
    }
}
