package flaxbeard.steamcraft.api.steamnet;

import java.util.HashMap;
import java.util.UUID;

import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.Tuple3;

public class SteamNetworkRegistry {
	
	private static SteamNetworkRegistry INSTANCE = new SteamNetworkRegistry();
	private HashMap<String, SteamNetwork> networks = new HashMap<String, SteamNetwork>();
	
	
	public static SteamNetworkRegistry getInstance(){
		return INSTANCE;
	}
	
	public void onTick(ServerTickEvent e){
		for (SteamNetwork network : networks.values()){
			if (networks.values().contains(network))
				network.tick();
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
		networks.put(network.getName(), network);
	}
	
	public void remove(SteamNetwork network){
		networks.remove(network.getName());
	}
	
	public void prune(){
		for (SteamNetwork network : networks.values()){
			if (network.getSize() == 0){
				networks.remove(network);
			}
		}
	}
	
	public SteamNetwork getNetwork(String name){
		return networks.get(name);
	}
	
	
	
	
}
