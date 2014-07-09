package flaxbeard.steamcraft.steamNetwork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.Tuple3;

public class SteamNetwork {
	
	private static Random random = new Random();
	private String name;
	private int steam;
	private int capacity;
	private boolean isPopulated = false;
	private Tuple3<Integer, Integer, Integer>[] transporterCoords;
	private HashMap<Tuple3<Integer, Integer, Integer>,ISteamTransporter> transporters = new HashMap<Tuple3<Integer, Integer, Integer>,ISteamTransporter>();
	
	public SteamNetwork(){
		this.steam = 0;
		this.capacity = 0;
	}
	
	public SteamNetwork(int steam, int capacity){
		this.steam = steam;
		this.capacity = capacity;
	}
	
	public String getName(){
		return this.name;
	}
	
	protected void setName(String name){
		this.name = name;
	}
	
	protected void tick(){
		
		if (this.getPressure() > 1.2F){
			for (ISteamTransporter trans : transporters.values()){
				if (!trans.getWorldObj().isRemote && shouldExplode(oneInX(this.getPressure(), trans.getPressureResistance()))){
					trans.explode();
					Tuple3<Integer, Integer, Integer> c = trans.getCoords();
					trans.getWorldObj().createExplosion(null, c.first+0.5F, c.second+0.5F, c.third+0.5F, 4.0F, true);
				}
			}
		}
		
		
	}
	
	public synchronized static void newOrJoin(ISteamTransporter trans){
		HashSet<ISteamTransporter> others = getNeighboringTransporters(trans);
		HashSet<SteamNetwork> nets = new HashSet();
		boolean hasJoinedNetwork = false;
		if (others.size() > 0){
			for (ISteamTransporter t : others){
				System.out.println("Checking other!");
				if (t.getNetwork() != null){
					System.out.println(t.getNetwork().name);
					SteamNetwork net = t.getNetwork();
					if (net != null){
						nets.add(net);
					}
				}
			}
			if (nets.size() > 0){
				System.out.println("Other net(s) found: " + nets.size());
				SteamNetwork main = null;
				for (SteamNetwork net : nets){
					if (main != null){
						System.out.println(net.name + " will be joining "+main.name);
						main.join(net);
					} else {
						System.out.println("Setting main to network "+net.name);
						main = net;
					}
				}
				main.addTransporter(trans);
				hasJoinedNetwork = true;
			}
			
		} 
		if (!hasJoinedNetwork) {
			SteamNetwork net = SteamNetworkRegistry.getInstance().getNewNetwork();
			net.addTransporter(trans);
		}
	}
	
	public void addSteam(int amount){
		this.steam += amount;
	}
	
	public void decrSteam(int amount){
		this.steam -= amount;
	}
	
	public int getSteam(){
		return this.steam;
	}
	
	public int getCapacity(){
		return this.capacity;
	}
	
	private int oneInX(float pressure, float resistance){
		return Math.max(1, (int)Math.floor((double)(500.0F  - (pressure / (1.1F + resistance) * 100)) ));
	}
	
	private boolean shouldExplode(int oneInX){
		return oneInX <= 1 ||  random.nextInt(oneInX - 1) == 0;
	}
	
	public float getPressure(){
		return (float)steam / (float)capacity;
	}

	public int getSize() {
		return transporters.size();
	}
	
	public static HashSet<ISteamTransporter> getNeighboringTransporters(ISteamTransporter trans){
		HashSet<ISteamTransporter> out = new HashSet();
		Tuple3<Integer, Integer, Integer> transCoords = trans.getCoords(); 
		for (ForgeDirection d : trans.getConnectionSides()){
			TileEntity te = trans.getWorldObj().getTileEntity(transCoords.first + d.offsetX, transCoords.second + d.offsetY, transCoords.third + d.offsetZ);
			if (te != null && te instanceof ISteamTransporter){
				ISteamTransporter t = (ISteamTransporter) te;
				if (t.getConnectionSides().contains(d.getOpposite())){
					out.add(t);
				}
			}
		}
		return out;
	}
	
	public void addTransporter(ISteamTransporter trans){
		this.capacity += trans.getCapacity();
		Tuple3<Integer, Integer, Integer> transCoords = trans.getCoords();
		transporters.put(new Tuple3(transCoords.first, transCoords.second, transCoords.third), trans);
		trans.setNetworkName(this.name);
		trans.setNetwork(this);
	}
	
	public void setTransporterCoords(Tuple3<Integer, Integer, Integer>[] coords){
		this.transporterCoords = coords;
	}
	
	public synchronized void init(World world){
		if (!this.isPopulated && this.transporterCoords != null){
			this.loadTransporters(world);
		}
	}
	
	public synchronized void loadTransporters(World world){
		for (int i = this.transporterCoords.length - 1; i >= 0; i-- ){
			Tuple3<Integer, Integer, Integer> coords = this.transporterCoords[i];
			int x = coords.first, y = coords.second, z = coords.third;
			TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof ISteamTransporter){
				this.transporters.put(this.transporterCoords[i], (ISteamTransporter)te);
			}
			
		}
	}
	
	public synchronized void split(ISteamTransporter split){
		if (this.steam >= split.getCapacity() * this.getPressure()){
			this.steam -= split.getCapacity() * this.getPressure();
		}
		this.capacity -= split.getCapacity();
		World world = split.getWorldObj();
		Tuple3<Integer, Integer, Integer> coords = split.getCoords();
		int x = coords.first, y= coords.second, z=coords.third;
		HashSet<ForgeDirection> dirs = split.getConnectionSides();
		HashSet<SteamNetwork> newNets = new HashSet();
		for (ISteamTransporter trans : this.getNeighboringTransporters(split)){
			boolean isInNetwork = false;
			if (newNets.size() > 0){
				for (SteamNetwork net : newNets){
					if (net.contains(trans)){
						isInNetwork = true;
						break;
					}
				}
			}
			if (!isInNetwork){
				SteamNetwork net = SteamNetworkRegistry.getInstance().getNewNetwork();
				net.buildFromTransporter(trans, net);
				newNets.add(net);
			}
		}
		if (newNets.size() > 1){
			for (SteamNetwork net : newNets){
				Double capacityShare = Double.valueOf((double)net.getCapacity() / (double)this.capacity);
				int steamShare = (int)Math.floor((double)capacityShare * (double)this.steam);
				this.steam -= steamShare;
				net.addSteam(steamShare);
				SteamNetworkRegistry.getInstance().add(net);
			}
			SteamNetworkRegistry.getInstance().remove(this);
		} else if (newNets.size() == 1){
			// There is only one network. Probably this one.
			transporters.remove(split.getCoords());
		} else {
			// There's nothing left.
			SteamNetworkRegistry.getInstance().remove(this);
		}
		
	}
	
	public synchronized void buildFromTransporter(ISteamTransporter trans, SteamNetwork target) {
		System.out.println("Building network!");
		HashSet<ISteamTransporter> checked = new HashSet();
		HashSet<ISteamTransporter> members = target.crawlNetwork(trans, checked);
		boolean targetIsThis = target == this;
		SteamNetwork net = targetIsThis ? this : SteamNetworkRegistry.getInstance().getNewNetwork();
		for (ISteamTransporter member : members){
			if ( !this.transporters.containsValue(member)){
				target.addTransporter(member);
			}
		}
		net.addTransporter(trans);
	}
	
	public boolean contains(ISteamTransporter trans){
		return this.transporters.containsValue(trans);
	}
	
	protected HashSet<ISteamTransporter> crawlNetwork(ISteamTransporter trans, HashSet<ISteamTransporter> checked){
		if (checked == null){
			checked = new HashSet<ISteamTransporter>();
		}
		if (!checked.contains(trans)){
			checked.add(trans);
		}
		HashSet<ISteamTransporter> neighbors = getNeighboringTransporters(trans);
		for (ISteamTransporter neighbor : neighbors){
			if (! checked.contains(neighbor)){
				checked.add(neighbor);
				crawlNetwork(neighbor, checked);
			}
		}
		return checked;
	}
	
	private HashSet<ISteamTransporter> getNeighborTransporters(ISteamTransporter trans){
		HashSet<ISteamTransporter> out  = new HashSet();
		Tuple3<Integer, Integer, Integer> coords = trans.getCoords();
		int x = coords.first, y = coords.second, z = coords.third;
		for (ForgeDirection dir : trans.getConnectionSides()){
			TileEntity te = trans.getWorldObj().getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
			if (te != null && te instanceof ISteamTransporter){
				ISteamTransporter neighbor = (ISteamTransporter) te;
				out.add(neighbor);
			}
		}
		return out;
	}

	public void join(SteamNetwork other){
		for (ISteamTransporter trans : other.transporters.values()){
			this.addTransporter(trans);
		}
		this.steam += other.getSteam();
		SteamNetworkRegistry.getInstance().remove(other);
	}
	
	
	
	
}
