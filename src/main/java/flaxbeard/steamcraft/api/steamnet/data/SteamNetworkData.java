package flaxbeard.steamcraft.api.steamnet.data;

import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class SteamNetworkData extends WorldSavedData{

	private static final String ID = "FSPSteamNetworkData";
	
	public SteamNetworkData() {
		super(ID);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		SteamNetworkRegistry.getInstance().readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		SteamNetworkRegistry.getInstance().writeToNBT(nbt);
	}

}
