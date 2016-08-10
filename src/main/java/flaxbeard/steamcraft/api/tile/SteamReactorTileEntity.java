package flaxbeard.steamcraft.api.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import flaxbeard.steamcraft.api.ISteamTransporter;

/**
 * Similar to the SteamTransporterTileEntity, this tile entity is used for steam "reactor" blocks.
 * For example: Steam Whistle, Rupture Disc.
 */
public class SteamReactorTileEntity extends TileEntityBase {
    /**
     * @param dir The FACING value for the reactor.
     * @return The pressure of the attached transporter.
     */
    public float getPressure(EnumFacing dir) {
        ISteamTransporter transporter = getAdjacentTransporter(dir);
        return transporter == null ? 0F : transporter.getPressure();
    }

    /**
     * Drains steam from the attached transporter.
     * @param s The amount of steam to drain.
     * @param dir The FACING value for the reactor.
     */
    public void drainSteam(int s, EnumFacing dir) {
        ISteamTransporter transporter = getAdjacentTransporter(dir);
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
        ISteamTransporter transporter = getAdjacentTransporter(dir);
        return transporter == null ? 0 : transporter.getSteamShare();
    }

    /**
     * Gets the attached transporter.
     * @return null if there is no ISteamTransporter adjacent to it.
     */
    public ISteamTransporter getAdjacentTransporter(EnumFacing dir) {
        EnumFacing d = dir.getOpposite();
        BlockPos transporterPos = pos.offset(d);
        TileEntity te = worldObj.getTileEntity(transporterPos);
        if (te != null && te instanceof ISteamTransporter) {
            return (ISteamTransporter) te;
        }
        return null;
    }

    public void markForUpdate() {
        markDirty();
    }
}
