package flaxbeard.steamcraft.api.steamnet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.steamnet.data.SteamNetworkData;

public class SteamNetworkRegistry {
	
	private static boolean loaderRegistered = false;
	private static SteamNetworkRegistry INSTANCE = new SteamNetworkRegistry();
	private HashMap<Integer, HashMap<String, SteamNetwork>> networks = new HashMap<Integer, HashMap<String, SteamNetwork>>(); 
	
	
	
	public static SteamNetworkRegistry getInstance(){
		return INSTANCE;
	}
	
	public static void initiate()
	{
		if(!loaderRegistered)
		{
			loaderRegistered = true;

			MinecraftForge.EVENT_BUS.register(new NetworkLoader());
			FMLCommonHandler.instance().bus().register(INSTANCE);
		}
	}
	
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, int dimID){
		System.out.println("Writing network registry for dimension" + dimID + " to NBT");
		NBTTagList nets = new NBTTagList();
		for (SteamNetwork net : networks.get(dimID).values()){
			nets.appendTag(net.writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("networks", nets);
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt, int dimID){
		System.out.println("reading network registry for dimension " + dimID + " from NBT");
		
		HashMap<String, SteamNetwork> nets = new HashMap<String, SteamNetwork>();
		if (nbt.hasKey("networks")){
			NBTTagList tagNets = (NBTTagList) nbt.getTag("networks");
			for (int i = 0; i < tagNets.tagCount(); i++){
				SteamNetwork net = SteamNetwork.readFromNBT(tagNets.getCompoundTagAt(i));
				nets.put(net.getName(), net);
			}
			networks.put(dimID, nets);
		}
			
	}
	

	
	public void onTick(ServerTickEvent e){
		for (HashMap<String, SteamNetwork> dimension : networks.values()){
			for (SteamNetwork net : dimension.values()){
				net.tick();
			}
		}
	}
	
	public SteamNetwork getNewNetwork(){
		SteamNetwork net = new SteamNetwork();
		String name = UUID.randomUUID().toString();
		System.out.println(name);
		net.setName(name);
		return net;
	}
	
	public void add(SteamNetwork network){
		if (!networks.containsKey(network.getDimension())){
			networks.put(network.getDimension(), new HashMap<String, SteamNetwork>());
		}
		HashMap<String, SteamNetwork> dimension = networks.get(network.getDimension());
		dimension.put(network.getName(), network);
		SteamNetworkData.get(network.getWorld()).markDirty();
	}
	
	public void remove(SteamNetwork network){
		if (networks.containsKey(network.getDimension())){
			HashMap<String, SteamNetwork> dimension = networks.get(network.getDimension());
			dimension.remove(network.getName());
			SteamNetworkData.get(network.getWorld()).markDirty();
		}
	}
	
	public SteamNetwork getNetwork(String name, ISteamTransporter trans){
		int d = trans.getDimension();
		if (networks.containsKey(d)){
			HashMap<String, SteamNetwork> dimension = networks.get(trans.getDimension());
			if (dimension.containsKey(name)){
				return dimension.get(name);
			}
		}
		
		// else
		return SteamNetwork.newOrJoin(trans);
		
		
		
	}
	
	public static void markDirty(SteamNetwork network){
		SteamNetworkData.get(network.getWorld()).markDirty();
	}
	
	/**
	 * 
	 * @author zenith (adapted from aidancbrady)
	 * 
	 *
	 */
	public static class NetworkLoader
	{
		@SubscribeEvent
		public void onChunkLoad(ChunkEvent.Load event)
		{
			if(event.getChunk() != null && !event.world.isRemote)
			{
				int x = event.getChunk().xPosition;
				int z = event.getChunk().zPosition;

				IChunkProvider cProvider = event.getChunk().worldObj.getChunkProvider();
				Chunk[] neighbors = new Chunk[5];

				neighbors[0] = event.getChunk();

				if(cProvider.chunkExists(x + 1, z)) neighbors[1] = cProvider.provideChunk(x + 1, z);
				if(cProvider.chunkExists(x - 1, z)) neighbors[2] = cProvider.provideChunk(x - 1, z);
				if(cProvider.chunkExists(x, z + 1)) neighbors[3] = cProvider.provideChunk(x, z + 1);
				if(cProvider.chunkExists(x, z - 1)) neighbors[4] = cProvider.provideChunk(x, z - 1);

				for(Chunk c : neighbors)
				{
					refreshChunk(c);
				}
			}
		}

		public synchronized void refreshChunk(Chunk c)
		{
			try {
				if(c != null)
				{
					Map copy = (Map)((HashMap)c.chunkTileEntityMap).clone();

					for(Iterator iter = copy.values().iterator(); iter.hasNext();)
					{
						Object obj = iter.next();

						if(obj instanceof ISteamTransporter)
						{
							((ISteamTransporter)obj).refresh();
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
