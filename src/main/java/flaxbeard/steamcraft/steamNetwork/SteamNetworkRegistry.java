package flaxbeard.steamcraft.steamNetwork;

import java.util.HashSet;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import flaxbeard.steamcraft.api.ISteamTransporter;

public class SteamNetworkRegistry {
	
	private static SteamNetworkRegistry INSTANCE = new SteamNetworkRegistry();
	private static HashSet<SteamNetwork> networks = new HashSet<SteamNetwork>();
	
	public static SteamNetworkRegistry getInstance(){
		return INSTANCE;
	}
	
	public static void onTick(ServerTickEvent e){
		for (SteamNetwork network : networks){
			if (networks.contains(network))
				network.tick();
		}
	}
	
	public static void add(SteamNetwork network){
		networks.add(network);
	}
	
	public static void remove(SteamNetwork network){
		networks.remove(network);
	}
	
	public void prune(){
		for (SteamNetwork network : networks){
			if (network.getSize() == 0){
				networks.remove(network);
			}
		}
	}
	
	
	
	
}
