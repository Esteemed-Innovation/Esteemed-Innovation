package flaxbeard.steamcraft.api.steamnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.util.Coord4;
import flaxbeard.steamcraft.tile.TileEntityValvePipe;

public class SteamNetwork {
	
	private static Random random = new Random();
	private String name;
	private int steam;
	private int capacity;
	private boolean isPopulated = false;
	private Coord4[] transporterCoords;
	private HashMap<Coord4,ISteamTransporter> transporters = new HashMap<Coord4,ISteamTransporter>();
	
	public SteamNetwork(){
		this.steam = 0;
		this.capacity = 0;
	}
	
	public SteamNetwork(int steam, int capacity){
		this.steam = steam;
		this.capacity = capacity;
	}
	
	public SteamNetwork(int steam, int capacity, String name, ArrayList<Coord4> coordList){
		this(steam, capacity);
		for (Coord4 c : coordList){
			this.transporters.put(c, null);
		}
		this.name = name;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		NBTTagList nbtl = new NBTTagList();
		for (Coord4 c : transporters.keySet()){
			nbtl.appendTag(c.writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("transporters", nbtl);
		nbt.setString("name", name);
		nbt.setInteger("steam", steam);
		nbt.setInteger("capacity", capacity);
		return nbt;
	}
	
	public static SteamNetwork readFromNBT(NBTTagCompound nbt){
		ArrayList<Coord4> coords = new ArrayList();
		NBTTagList nbtl = (NBTTagList)nbt.getTag("transporters");
		for (int i = 0; i < nbtl.tagCount(); i ++){
			NBTTagCompound tag = nbtl.getCompoundTagAt(i);
			coords.add(Coord4.readFromNBT(tag));
		}
		int s = nbt.getInteger("steam");
		int c = nbt.getInteger("capacity");
		String n = nbt.getString("name");
		return new SteamNetwork(s, c, n, coords);
	}
	
	public void rejoin(ISteamTransporter trans){
		if (this.transporters.containsKey(trans.getCoords())){
			this.transporters.put(trans.getCoords(), trans);
		}
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
				if (!trans.getWorld().isRemote && shouldExplode(oneInX(this.getPressure(), trans.getPressureResistance()))){
					trans.explode();
					Coord4 c = trans.getCoords();
					trans.getWorld().createExplosion(null, c.x+0.5F, c.y+0.5F, c.z+0.5F, 4.0F, true);
				}
			}
		}
		
		
	}
	
	public synchronized static SteamNetwork newOrJoin(ISteamTransporter trans){
		HashSet<ISteamTransporter> others = getNeighboringTransporters(trans);
		HashSet<SteamNetwork> nets = new HashSet();
		SteamNetwork theNetwork = null;
		boolean hasJoinedNetwork = false;
		if (others.size() > 0){
			for (ISteamTransporter t : others){
				//System.out.println("Checking other!");
				if (!isClosedValvePipe(t)){
					if (t.getNetwork() != null){
						//System.out.println(t.getNetwork().name);
						SteamNetwork net = t.getNetwork();
						if (net != null){
							nets.add(net);
						}
					}
				}
				
			}
			if (nets.size() > 0){
				//System.out.println("Other net(s) found: " + nets.size());
				SteamNetwork main = null;
				for (SteamNetwork net : nets){
					if (main != null){
						//System.out.println(net.name + " will be joining "+main.name);
						main.join(net);
					} else {
						//System.out.println("Setting main to network "+net.name);
						main = net;
					}
				}
				main.addTransporter(trans);
				hasJoinedNetwork = true;
				theNetwork = main;
			}
			
		} 
		if (!hasJoinedNetwork) {
			SteamNetwork net = SteamNetworkRegistry.getInstance().getNewNetwork();
			net.addTransporter(trans);
			theNetwork = net;
		}
		return theNetwork;
	}
	
	public synchronized void addSteam(int amount){
		this.steam += amount;
	}
	
	public synchronized void decrSteam(int amount){
		this.steam -= amount;
		if (this.steam < 0){
			this.steam = 0;
		}
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
		Coord4 transCoords = trans.getCoords(); 
		for (ForgeDirection d : trans.getConnectionSides()){
			TileEntity te = trans.getWorld().getTileEntity(transCoords.x + d.offsetX, transCoords.y + d.offsetY, transCoords.z + d.offsetZ);
			if (te != null && te instanceof ISteamTransporter){
				if (te != trans){
					ISteamTransporter t = (ISteamTransporter) te;
					if (t.getConnectionSides().contains(d.getOpposite())){
						out.add(t);
					}
				}
				
			}
		}
		return out;
	}
	
	public void addTransporter(ISteamTransporter trans){
		this.capacity += trans.getCapacity();
		Coord4 transCoords = trans.getCoords();
		transporters.put(transCoords, trans);
		trans.setNetworkName(this.name);
		trans.setNetwork(this);
		SteamNetworkRegistry.markDirty(this);
	}
	
	public void setTransporterCoords(Coord4[] coords){
		this.transporterCoords = coords;
	}
	
	public synchronized void init(World world){
		if (!this.isPopulated && this.transporterCoords != null){
			this.loadTransporters(world);
		}
	}
	
	public synchronized void loadTransporters(World world){
		for (int i = this.transporterCoords.length - 1; i >= 0; i-- ){
			Coord4 coords = this.transporterCoords[i];
			int x = coords.x, y = coords.y, z = coords.z;
			TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof ISteamTransporter){
				this.transporters.put(this.transporterCoords[i], (ISteamTransporter)te);
			}
			
		}
	}
	
	public synchronized void split(ISteamTransporter split){
		//System.out.println("Splitting network: "+ this.name);
		if (this.steam >= split.getCapacity() * this.getPressure()){
			//System.out.println("Subtracting "+(split.getCapacity() * this.getPressure() )+ " from the network;");
			this.steam -= split.getCapacity() * this.getPressure();
			
		}
		//System.out.println("Subtracting "+split.getCapacity() + " capacity from the network");
		this.capacity -= split.getCapacity();
		//World world = split.getWorldObj();
		//Tuple3<Integer, Integer, Integer> coords = split.getCoords();
		//int x = coords.first, y= coords.second, z=coords.third;
		//HashSet<ForgeDirection> dirs = split.getConnectionSides();
		HashSet<SteamNetwork> newNets = new HashSet();
		for (ISteamTransporter trans : this.getNeighboringTransporters(split)){
			if (!isClosedValvePipe(trans)){
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
					//System.out.println("Not in network!");
					SteamNetwork net = SteamNetworkRegistry.getInstance().getNewNetwork();
					//System.out.println("Crawling!");
					net.buildFromTransporter(trans, net, split);
					newNets.add(net);
					//System.out.println(net.getSize());
				}
			}
			
		}
		if (newNets.size() > 0){
			//System.out.println("More than one new network found");
			for (SteamNetwork net : newNets){
				int steamShare = (int)Math.floor((double)(net.capacity * this.getPressure()));
				net.addSteam(steamShare);
				SteamNetworkRegistry.getInstance().add(net);
			}
			SteamNetworkRegistry.getInstance().remove(this);
		} else {
			// There's nothing left.
			//System.out.println("No networks around");
			SteamNetworkRegistry.getInstance().remove(this);
		}
		
	}
	
	public synchronized void buildFromTransporter(ISteamTransporter trans, SteamNetwork target, ISteamTransporter ignore) {
		//System.out.println("Building network!");
		HashSet<ISteamTransporter> checked = new HashSet();
		HashSet<ISteamTransporter> members = target.crawlNetwork(trans, checked, ignore);
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
	
	protected HashSet<ISteamTransporter> crawlNetwork(ISteamTransporter trans, HashSet<ISteamTransporter> checked, ISteamTransporter ignore){
		if (checked == null){
			checked = new HashSet<ISteamTransporter>();
		}
		if (!checked.contains(trans) && !isClosedValvePipe(trans)){
			checked.add(trans);
		}
		HashSet<ISteamTransporter> neighbors = getNeighboringTransporters(trans);
		for (ISteamTransporter neighbor : neighbors){
			//System.out.println(neighbor == ignore ? "Should ignore this." : "Should not be ignored");
			if (! checked.contains(neighbor) && neighbor != ignore && !isClosedValvePipe(neighbor)){
				//System.out.println("Didn't ignore");
				checked.add(neighbor);
				crawlNetwork(neighbor, checked, ignore);
			}
		}
		return checked;
	}
	
	private static boolean isClosedValvePipe(ISteamTransporter trans){
		return ((trans instanceof TileEntityValvePipe && !( ((TileEntityValvePipe)trans).isOpen() )));
	}
	
	private HashSet<ISteamTransporter> getNeighborTransporters(ISteamTransporter trans){
		HashSet<ISteamTransporter> out  = new HashSet();
		Coord4 coords = trans.getCoords();
		int x = coords.x, y = coords.y, z = coords.z;
		for (ForgeDirection dir : trans.getConnectionSides()){
			TileEntity te = trans.getWorld().getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
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

	public int getDimension() {
		return transporters.keySet().iterator().next().dimension;
	}
	
	public World getWorld(){
		if (transporters.values().iterator().next() != null){
			return transporters.values().iterator().next().getWorld();
		} else {
			return null;
		}
		
	}
	
	public void markDirty(){
		SteamNetworkRegistry.markDirty(this);
	}
	
	
	
	
}
