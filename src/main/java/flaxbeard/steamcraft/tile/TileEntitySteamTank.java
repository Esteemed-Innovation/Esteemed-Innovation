package flaxbeard.steamcraft.tile;

import net.minecraft.util.EnumFacing;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntitySteamTank extends SteamTransporterTileEntity implements ISteamTransporter {
    public TileEntitySteamTank() {
        super(80000, EnumFacing.HORIZONTALS);
    }
}
