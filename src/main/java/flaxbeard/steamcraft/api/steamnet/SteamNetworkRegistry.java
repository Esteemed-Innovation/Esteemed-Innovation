package flaxbeard.steamcraft.api.steamnet;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.steamnet.data.SteamNetworkData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class SteamNetworkRegistry {

    private static boolean loaderRegistered = false;

    private static SteamNetworkRegistry INSTANCE = new SteamNetworkRegistry();
    private HashSet<Integer> initialized = new HashSet<Integer>();
    private HashMap<Integer, HashMap<String, SteamNetwork>> networks = new HashMap<Integer, HashMap<String, SteamNetwork>>();


    public static SteamNetworkRegistry getInstance() {
        return INSTANCE;
    }

    public static void initialize() {
        if (!loaderRegistered) {
            loaderRegistered = true;

            MinecraftForge.EVENT_BUS.register(new NetworkLoader());
            FMLCommonHandler.instance().bus().register(INSTANCE);
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
        //////Steamcraft.log.debug("Tick");
        if (networks.values() != null && networks.values().size() > 0) {
            try {
                for (HashMap<String, SteamNetwork> dimension : networks.values()) {
                    for (SteamNetwork net : dimension.values()) {
                        if (!net.tick()) {
                            dimension.remove(net.getName());
                        }
                    }
                }
            } catch (ConcurrentModificationException ex) {
                ////Steamcraft.log.debug("FSP: ConcurrentModificationException in network tick");
                //ex.printStackTrace();
            }

        }

    }

    public SteamNetwork getNewNetwork() {
        //////Steamcraft.log.debug("Returning new network");
        SteamNetwork net = new SteamNetwork();
        String name = UUID.randomUUID().toString();
        //////Steamcraft.log.debug(name);
        net.setName(name);
        return net;
    }

    public void add(SteamNetwork network) {
        if (!networks.containsKey(network.getDimension())) {
            networks.put(network.getDimension(), new HashMap<String, SteamNetwork>());
        }
        HashMap<String, SteamNetwork> dimension = networks.get(network.getDimension());
        dimension.put(network.getName(), network);
        World world = network.getWorld();
        if (world != null) {
            SteamNetworkData.get(world).markDirty();
        }
        //printNetworks(network.getDimension());
    }

    public void remove(SteamNetwork network) {
        if (networks.containsKey(network.getDimension())) {
            HashMap<String, SteamNetwork> dimension = networks.get(network.getDimension());
            dimension.remove(network.getName());
            World world = network.getWorld();
            if (world != null) {
                SteamNetworkData.get(world).markDirty();
            }

        }
    }

    public SteamNetwork getNetwork(String name, ISteamTransporter trans) {
        int d = trans.getDimension();
        if (networks.containsKey(d)) {
            HashMap<String, SteamNetwork> dimension = networks.get(trans.getDimension());
            if (dimension.containsKey(name)) {
                return dimension.get(name);
            }
        }

        // else
        return SteamNetwork.newOrJoin(trans);


    }

    public void printNetworks(int dim) {
        for (SteamNetwork net : networks.get(dim).values()) {
            ////Steamcraft.log.debug(net.getName());
        }
    }

    public void newDimension(int dimensionId) {
        initialized.add(dimensionId);

    }

    /**
     * @author zenith (adapted from aidancbrady)
     */
    public static class NetworkLoader {
        @SubscribeEvent
        public void onChunkLoad(ChunkEvent.Load event) {
            if (event.getChunk() != null && !event.world.isRemote) {
                int x = event.getChunk().xPosition;
                int z = event.getChunk().zPosition;

                IChunkProvider cProvider = event.getChunk().worldObj.getChunkProvider();
                Chunk[] neighbors = new Chunk[5];

                neighbors[0] = event.getChunk();

                if (cProvider.chunkExists(x + 1, z)) neighbors[1] = cProvider.provideChunk(x + 1, z);
                if (cProvider.chunkExists(x - 1, z)) neighbors[2] = cProvider.provideChunk(x - 1, z);
                if (cProvider.chunkExists(x, z + 1)) neighbors[3] = cProvider.provideChunk(x, z + 1);
                if (cProvider.chunkExists(x, z - 1)) neighbors[4] = cProvider.provideChunk(x, z - 1);

                for (Chunk c : neighbors) {
                    refreshChunk(c);
                }
            }
        }

        public synchronized void refreshChunk(Chunk c) {
//			try {
//				if(c != null)
//				{
//					Map copy = (Map)((HashMap)c.chunkTileEntityMap).clone();
//
//					for(Iterator iter = copy.values().iterator(); iter.hasNext();)
//					{
//						Object obj = iter.next();
//
//						if(obj instanceof ISteamTransporter)
//						{
//							((ISteamTransporter)obj).refresh();
//						}
//					}
//				}
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
        }
    }

}
