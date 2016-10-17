package eiteam.esteemedinnovation.api.steamnet.data;

import eiteam.esteemedinnovation.api.steamnet.SteamNetworkRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class SteamNetworkData extends WorldSavedData {

    private static final String ID = "EISteamNetworkData";
    private int dimID;

    public SteamNetworkData(String s) {
        super(ID);
    }

    public SteamNetworkData(int dimension) {
        super(ID);
        this.dimID = dimension;
        markDirty();
    }

    public static SteamNetworkData get(World world) {
        SteamNetworkData data = (SteamNetworkData) world.getPerWorldStorage().getOrLoadData(SteamNetworkData.class, ID);
        if (data == null) {
            int dimension = world.provider.getDimension();
            data = new SteamNetworkData(dimension);
            SteamNetworkRegistry.getInstance().newDimension(dimension);
            world.getPerWorldStorage().setData(ID, data);
        }
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        dimID = nbt.getInteger("dimID");
        SteamNetworkRegistry.getInstance().readFromNBT(nbt, dimID);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("dimID", dimID);
        SteamNetworkRegistry.getInstance().writeToNBT(nbt, dimID);

        return nbt;
    }
}
