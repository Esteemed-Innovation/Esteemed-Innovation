package eiteam.esteemedinnovation.api.tile;

import eiteam.esteemedinnovation.api.SteamTransporter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Similar to the {@link SteamTransporterTileEntity}, this tile entity is used for steam "reactor" blocks.
 * For example: Steam Whistle, Rupture Disc.
 * <p>
 * It provides default safe update (see {@link TileEntityTickableSafe}) methods that do nothing, because it is
 * completely possible to make a steam reactor that does not tick.
 */
public class SteamReactorTileEntity extends TileEntityTickableSafe {
    @Override
    public boolean canUpdate(IBlockState target) {
        return false;
    }

    @Override
    public void safeUpdate() {}

    /**
     * @param dir The FACING value for the reactor.
     * @return The pressure of the attached transporter.
     */
    public float getPressure(EnumFacing dir) {
        SteamTransporter transporter = getAdjacentTransporter(dir);
        return transporter == null ? 0F : transporter.getPressure();
    }

    /**
     * Drains steam from the attached transporter.
     * @param s The amount of steam to drain.
     * @param dir The FACING value for the reactor.
     */
    public void drainSteam(int s, EnumFacing dir) {
        SteamTransporter transporter = getAdjacentTransporter(dir);
        if (transporter != null) {
            transporter.decrSteam(s);
        }
    }

    /**
     * Gets the current amount of steam in the attached transporter.
     * @param dir The FACING value for the reactor..
     * @return The steam in the transporter.
     */
    public int getSteam(EnumFacing dir) {
        SteamTransporter transporter = getAdjacentTransporter(dir);
        return transporter == null ? 0 : transporter.getSteamShare();
    }

    /**
     * Gets the attached transporter.
     * @return null if there is no SteamTransporter adjacent to it.
     */
    public SteamTransporter getAdjacentTransporter(EnumFacing dir) {
        EnumFacing d = dir.getOpposite();
        BlockPos transporterPos = pos.offset(d);
        TileEntity te = world.getTileEntity(transporterPos);
        if (te instanceof SteamTransporter) {
            return (SteamTransporter) te;
        }
        return null;
    }

    public void markForUpdate() {
        markDirty();
    }
}
