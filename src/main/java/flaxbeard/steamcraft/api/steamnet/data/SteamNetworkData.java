package flaxbeard.steamcraft.api.steamnet.data;

import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class SteamNetworkData extends WorldSavedData{
	
	private static final String ID = "FSPSteamNetworkData";
	private int dimID;
	
	public SteamNetworkData(String s){
		super(ID);
	}
	
	public SteamNetworkData(int dimension) {
		super(ID);
		this.dimID = dimension;
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		dimID = nbt.getInteger("dimID");
		SteamNetworkRegistry.getInstance().readFromNBT(nbt, dimID);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("dimID",dimID);
		SteamNetworkRegistry.getInstance().writeToNBT(nbt, dimID);
	}

	public static SteamNetworkData get(World world) {
		SteamNetworkData data = (SteamNetworkData) world.perWorldStorage.loadData(SteamNetworkData.class, ID);
        if (data == null) {
        	System.out.println("!!NEED NEW STEAM NETWORK DATA!!");
            data = new SteamNetworkData(world.provider.dimensionId);
            SteamNetworkRegistry.getInstance().newDimension(world.provider.dimensionId);
            world.perWorldStorage.setData(ID, data);
        }
        return data;
    }
}
