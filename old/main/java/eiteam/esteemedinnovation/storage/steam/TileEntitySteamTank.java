package eiteam.esteemedinnovation.storage.steam;

import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.storage.StorageModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntitySteamTank extends SteamTransporterTileEntity {
    public TileEntitySteamTank() {
        super(80000, EnumFacing.VALUES);
    }

    @Override
    public boolean canUpdate(IBlockState target) {
        return target.getBlock() == StorageModule.STEAM_TANK;
    }
}
