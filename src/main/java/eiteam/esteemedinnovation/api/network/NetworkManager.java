package eiteam.esteemedinnovation.api.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.Set;

public class NetworkManager extends WorldSavedData {

    private static final String ID = "EISteamNetworkData";
    
    private final NetworkData network;

    public NetworkManager(String s, World world) {
        super(s);
        this.network = new NetworkData(world);
        
        markDirty();
    }
    
    public static NetworkManager get(World world) {
        return get((ServerWorld) world);
    }

    public static NetworkManager get(ServerWorld world) {
        return world.getSavedData().getOrCreate(() -> new NetworkManager(ID, world), ID);
    }
    
    public NetworkData getNetworkData() {
        return network;
    }

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        network.read(nbt);
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        return network.write(nbt);
    }
}
