package eiteam.esteemedinnovation.tile;

import net.minecraft.util.EnumFacing;
import eiteam.esteemedinnovation.api.ISteamTransporter;
import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;

public class TileEntitySteamTank extends SteamTransporterTileEntity implements ISteamTransporter {
    public TileEntitySteamTank() {
        super(80000, EnumFacing.VALUES);
    }
}
