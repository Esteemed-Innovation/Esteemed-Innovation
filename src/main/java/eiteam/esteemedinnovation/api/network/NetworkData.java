package eiteam.esteemedinnovation.api.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NetworkData {
    
    public final HashMap<ResourceLocation, List<Network>> networks = new HashMap<>();
    private final World world;
    
    public NetworkData(World world) {
        this.world = world;
    }
    
    public CompoundNBT write(CompoundNBT nbt) {
        ListNBT networks = new ListNBT();
        this.networks.entrySet().forEach(entry -> {
            entry.getValue().forEach(n -> {
                CompoundNBT networkTag = new CompoundNBT();
                networkTag.putString("type", n.getType().toString());
                networks.add(n.writeToNBT(networkTag));
            });
        });
        nbt.put("networks", networks);
        return nbt;
    }
    
    public void read(CompoundNBT nbt) {
        networks.clear();
        ListNBT networks = nbt.getList("networks", Constants.NBT.TAG_COMPOUND);
        for(INBT inbt : networks) {
            CompoundNBT compound = (CompoundNBT) inbt;
            if(!compound.contains("type")) {
                continue;
            }
            ResourceLocation type = new ResourceLocation(compound.getString("type"));
            INetworkFactory factory = NetworkRegistry.INSTANCE.getFactory(type);
            if (factory != null) {
                Network network = factory.create(world, compound);
                List<Network> list = this.networks.computeIfAbsent(type, (key) -> new ArrayList<>());
                list.add(network);
            }
        }
    }
    
    public void add(Network network) {
        List<Network> list = networks.computeIfAbsent(network.getType(), (key) -> new ArrayList<>());
        list.add(network);
        World world = network.getWorld();
        if(world != null) {
            NetworkManager.get(world).markDirty();
        }
    }
    
    public void remove(Network network) {
        if(networks.containsKey(network.getType())) {
            List<Network> list = networks.get(network.getType());
            list.remove(network);
            World world = network.getWorld();
            if(world != null) {
                NetworkManager.get(world).markDirty();
            }
        }
    }
    
    public void update() {
        networks.values().forEach(list -> list.forEach(Network::update));
    }
}
