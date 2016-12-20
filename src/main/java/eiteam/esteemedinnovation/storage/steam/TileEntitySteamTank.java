package eiteam.esteemedinnovation.storage.steam;

import eiteam.esteemedinnovation.api.steamtransporter.SteamTransporterTileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntitySteamTank extends SteamTransporterTileEntity {
    public TileEntitySteamTank() {
        super(80000, EnumFacing.VALUES);
    }
}
