package flaxbeard.steamcraft.tile;

import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntitySteamTank extends SteamTransporterTileEntity implements ISteamTransporter {
	
	public TileEntitySteamTank(){
		super(5000, ForgeDirection.VALID_DIRECTIONS);
	}
}