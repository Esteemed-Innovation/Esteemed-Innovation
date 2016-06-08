package flaxbeard.steamcraft.api.steamnet;

import flaxbeard.steamcraft.api.steamnet.data.SteamNetworkData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class SteamNetworkRegistry {
    private static boolean loaderRegistered = false;

    private static SteamNetworkRegistry INSTANCE = new SteamNetworkRegistry();
    private HashSet<Integer> initialized = new HashSet<>();

    /**
     * Key: Dimension ID, Value: All networks in that dimension.
     */
    private HashMap<Integer, ArrayList<SteamNetwork>> networks = new HashMap<>();

    public static SteamNetworkRegistry getInstance() {
        return INSTANCE;
    }

    public static void initialize() {
        if (!loaderRegistered) {
            loaderRegistered = true;
            MinecraftForge.EVENT_BUS.register(INSTANCE);
        }
    }

    public static void markDirty(SteamNetwork network) {
        World world = network.getWorld();
        if (world != null) {
            SteamNetworkData.get(world).markDirty();
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt, int dimID) {
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt, int dimID) {
        initialized.add(dimID);
    }

    public boolean isInitialized(int dim) {
        return initialized.contains(dim);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent e) {
        for (ArrayList<SteamNetwork> nets : networks.values()) {
            Iterator<SteamNetwork> dimension = nets.iterator();
            while (dimension.hasNext()) {
                SteamNetwork net = dimension.next();
                if (!net.tick()) {
                    dimension.remove();
                }
            }
        }
    }

    public SteamNetwork getNewNetwork() {
        SteamNetwork net = new SteamNetwork();
        String name = UUID.randomUUID().toString();
        net.setName(name);
        return net;
    }

    public void add(SteamNetwork network) {
        if (!networks.containsKey(network.getDimension())) {
            networks.put(network.getDimension(), new ArrayList<SteamNetwork>());
        }
        ArrayList<SteamNetwork> dimension = networks.get(network.getDimension());
        dimension.add(network);
        World world = network.getWorld();
        if (world != null) {
            SteamNetworkData.get(world).markDirty();
        }
    }

    public void remove(SteamNetwork network) {
        if (networks.containsKey(network.getDimension())) {
            ArrayList<SteamNetwork> dimension = networks.get(network.getDimension());
            dimension.remove(network);
            World world = network.getWorld();
            if (world != null) {
                SteamNetworkData.get(world).markDirty();
            }
        }
    }

    public void newDimension(int dimensionId) {
        initialized.add(dimensionId);
    }
}
