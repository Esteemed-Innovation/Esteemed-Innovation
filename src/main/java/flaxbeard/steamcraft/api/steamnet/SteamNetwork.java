package flaxbeard.steamcraft.api.steamnet;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.util.Coord4;
import flaxbeard.steamcraft.api.util.SPLog;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import flaxbeard.steamcraft.tile.TileEntityValvePipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class SteamNetwork {

    protected static SPLog slog = Steamcraft.log;
    private static Random random = new Random();
    protected SPLog log = Steamcraft.log;
    private int refreshWaitTicks = 0;
    private int globalRefreshTicks = 300;
    private String name;
    private int steam;
    private int capacity;
    private boolean isPopulated = false;
    private boolean shouldRefresh = false;
    private Coord4[] transporterCoords;
    private int dim = 0;
    private int initializedTicks = 0;
    private HashMap<Coord4, ISteamTransporter> transporters = new HashMap<Coord4, ISteamTransporter>();

    public SteamNetwork() {
        this.steam = 0;
        this.capacity = 0;
    }

    public SteamNetwork(int capacity) {
        this.capacity = capacity;
    }

    public SteamNetwork(int capacity, String name, ArrayList<Coord4> coordList) {
        this(capacity);
        for (Coord4 c : coordList) {
            this.transporters.put(c, null);
        }
        this.name = name;
    }

    public synchronized static SteamNetwork newOrJoin(ISteamTransporter trans) {
        if (isClosedValvePipe(trans)) { return null; }
        HashSet<ISteamTransporter> others = getNeighboringTransporters(trans);
        HashSet<SteamNetwork> nets = new HashSet();
        SteamNetwork theNetwork = null;
        boolean hasJoinedNetwork = false;
        if (others.size() > 0) {
            for (ISteamTransporter t : others) {
                //////Steamcraft.log.debug("Checking other!");
                if (!isClosedValvePipe(t)) {
                    if (t.getNetwork() != null) {
                        //////Steamcraft.log.debug(t.getNetwork().name);
                        SteamNetwork net = t.getNetwork();
                        if (net != null) {
                            nets.add(net);
                        }
                    }
                }

            }
            if (nets.size() > 0) {
                //////Steamcraft.log.debug("Other net(s) found: " + nets.size());
                SteamNetwork main = null;
                for (SteamNetwork net : nets) {
                    if (main != null) {
                        //////Steamcraft.log.debug(net.name + " will be joining "+main.name);
                        main.join(net);
                    } else {
                        //////Steamcraft.log.debug("Setting main to network "+net.name);
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
            SteamNetworkRegistry.getInstance().add(net);
            theNetwork = net;
        }
        return theNetwork;
    }

    public static HashSet<ISteamTransporter> getNeighboringTransporters(ISteamTransporter trans) {
        HashSet<ISteamTransporter> out = new HashSet();
        Coord4 transCoords = trans.getCoords();
        for (ForgeDirection d : trans.getConnectionSides()) {
            TileEntity te = trans.getWorld().getTileEntity(transCoords.x + d.offsetX, transCoords.y + d.offsetY, transCoords.z + d.offsetZ);
            if (te != null && te instanceof ISteamTransporter) {
                if (te != trans) {
                    boolean isNeighbor = false;
                    ISteamTransporter t = (ISteamTransporter) te;
                    if (t.getConnectionSides().contains(d.getOpposite())) {
                        out.add(t);
                        if (t instanceof TileEntitySteamPipe) {
                            //slog.debug("Is original pipe: "+((TileEntitySteamPipe)t).isOriginalPipe);
                        }
                        isNeighbor = true;
                    } else {
                        //slog.debug("I can't connect");
                    }
                    TileEntitySteamPipe pipe = null;
                    TileEntitySteamPipe other = null;
                    if (trans instanceof TileEntitySteamPipe) {
                        pipe = (TileEntitySteamPipe) trans;
                    }
                    if (t instanceof TileEntitySteamPipe) {
                        other = (TileEntitySteamPipe) t;
                    }

                    if (pipe != null && other != null) {
                        if ((pipe.isOriginalPipe && other.isOtherPipe) || (pipe.isOtherPipe && other.isOriginalPipe)) {
                            //slog.debug("These shouldn't connect but do.");
                        }
                    }
                    //slog.debug("Side: "+d.offsetX+","+d.offsetY+","+d.offsetZ+"; isNeighbor: "+isNeighbor);
                }

            }
        }
        return out;
    }

    private static boolean isClosedValvePipe(ISteamTransporter trans) {
        return ((trans instanceof TileEntityValvePipe && !(((TileEntityValvePipe) trans).isOpen())));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList nbtl = new NBTTagList();
        //for (Coord4 c : transporters.keySet()){
        //	nbtl.appendTag(c.writeToNBT(new NBTTagCompound()));
        //}
        //nbt.setTag("transporters", nbtl);
        //nbt.setString("name", name);
        //nbt.setInteger("steam", steam);
        //nbt.setInteger("capacity", capacity);

        return nbt;
    }

    public String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected boolean tick() {
        if (this.transporters.size() == 0) {
            log.debug("No transporters (" + getShortName() + ")");
            return false;
        }
        if (shouldRefresh) {
            if (this.refreshWaitTicks > 0) {
                this.refreshWaitTicks--;
            } else {
                this.refresh();
                this.shouldRefresh = false;
            }

        }

        if (globalRefreshTicks > 0) {
            globalRefreshTicks--;
        } else {
            this.refresh();
            globalRefreshTicks = 300;
        }
        if (this.initializedTicks >= 1200) {

            if (Config.wimpMode) {
                if (this.getPressure() > 1.09F) {
                    this.steam = (int) Math.floor((double) this.capacity * 1.09D);
                }
            } else {
                if (this.transporters != null && this.transporters.keySet() != null) {
                    if (this.getPressure() > 1.2F) {
                        for (Coord4 coords : transporters.keySet()) {
                            //////Steamcraft.log.debug("Iterating!");
                            ISteamTransporter trans = transporters.get(coords);
                            if ((trans == null || ((TileEntity) trans).isInvalid())) {
                                //////Steamcraft.log.debug("Invalid TE");
                                transporters.remove(coords);
                            } else if (!trans.getWorld().isRemote && shouldExplode(oneInX(this.getPressure(), trans.getPressureResistance()))) {
                                log.debug(trans.getName() + " is exploding.  (" + this.getSteam() + " " + this.getCapacity() + " " + this.getPressure() + ")");
                                trans.explode();

                            }
                        }

                    }
                } else {
                    return false;
                }
            }
        } else {
            initializedTicks++;
        }
        return true;
    }

    public synchronized void addSteam(int amount) {
        this.steam += amount;
        this.shouldRefresh();
    }

    public synchronized void decrSteam(int amount) {
        this.steam -= amount;
        if (this.steam < 0) {
            this.steam = 0;
        }
    }

    public int getSteam() {
        return this.steam;
    }

    public int getCapacity() {
        return this.capacity;
    }

    private int oneInX(float pressure, float resistance) {
        return Math.max(1, (int) Math.floor((double) (500.0F - (pressure / (1.1F + resistance) * 100))));
    }

    private boolean shouldExplode(int oneInX) {
        return oneInX <= 1 || random.nextInt(oneInX - 1) == 0;
    }

    public float getPressure() {
        if (capacity > 0) {
            return (float) steam / (float) capacity;
        }
        return 0;

    }

    public int getSize() {
        return transporters.size();
    }

    public synchronized void addTransporter(ISteamTransporter trans) {
        if (trans != null && !this.contains(trans)) {
            this.capacity += trans.getCapacity();
            Coord4 transCoords = trans.getCoords();
            transporters.put(transCoords, trans);
            trans.setNetworkName(this.name);
            trans.setNetwork(this);
            this.addSteam(trans.getSteam());
            trans.wasAdded();
            SteamNetworkRegistry.markDirty(this);
        }
    }

    public void setTransporterCoords(Coord4[] coords) {
        this.transporterCoords = coords;
    }

    public synchronized void init(World world) {
        if (!this.isPopulated && this.transporterCoords != null) {
            this.loadTransporters(world);
        }
    }

    public synchronized void loadTransporters(World world) {
        for (int i = this.transporterCoords.length - 1; i >= 0; i--) {
            Coord4 coords = this.transporterCoords[i];
            int x = coords.x, y = coords.y, z = coords.z;
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof ISteamTransporter) {
                this.transporters.put(this.transporterCoords[i], (ISteamTransporter) te);
            }

        }
    }

    public synchronized int split(ISteamTransporter split, boolean removeCapacity) {
        //////Steamcraft.log.debug("Splitting network: "+ this.name);
        int steamRemoved = 0;
        if (removeCapacity && this.steam >= split.getCapacity() * this.getPressure()) {
            //////Steamcraft.log.debug("Subtracting "+(split.getCapacity() * this.getPressure() )+ " from the network;");
            steamRemoved = (int) Math.floor((double) split.getCapacity() * (double) this.getPressure());
            this.steam -= steamRemoved;
            this.capacity -= split.getCapacity();

        }
        for (ISteamTransporter trans : this.transporters.values()) {
            trans.updateSteam((int) (trans.getCapacity() * this.getPressure()));
        }
        //////Steamcraft.log.debug("Subtracting "+split.getCapacity() + " capacity from the network");

        //World world = split.getWorldObj();
        //Tuple3<Integer, Integer, Integer> coords = split.getCoords();
        //int x = coords.first, y= coords.second, z=coords.third;
        //HashSet<ForgeDirection> dirs = split.getConnectionSides();
        HashSet<SteamNetwork> newNets = new HashSet();
        boolean hasrun = false;
        for (ISteamTransporter trans : this.getNeighboringTransporters(split)) {
            if (!isClosedValvePipe(trans)) {
                boolean isInNetwork = false;
                if (newNets.size() > 0) {
                    //log.debug("size: "+newNets.size());
                    for (SteamNetwork net : newNets) {
                        if (net.contains(trans)) {
                            ////Steamcraft.log.debug("In network");
                            isInNetwork = true;
                            break;
                        }
                    }
                }
                if (!isInNetwork) {
                    //log.debug("Not in network!");
                    SteamNetwork net = SteamNetworkRegistry.getInstance().getNewNetwork();
                    //////Steamcraft.log.debug("Crawling!");
                    ISteamTransporter ignore = null;
                    if (removeCapacity) {
                        ignore = split;
                    }

                    net.buildFromTransporter(trans, net, ignore);
                    newNets.add(net);
                    //////Steamcraft.log.debug(net.getSize());
                    hasrun = true;
                }
            }

        }
        if (newNets.size() > 0) {
            //log.debug("More than one new network found");
            ////Steamcraft.log.debug("old s:"+this.steam+" p:"+this.getPressure() + " c:"+this.capacity);
            for (SteamNetwork net : newNets) {
                int steamShare = (int) Math.floor((double) (net.capacity * this.getPressure()));
                //log.debug("new s:"+steamShare+" c:"+net.capacity+" n: "+net.getName());
                //net.addSteam(steamShare);
                SteamNetworkRegistry.getInstance().add(net);
                net.shouldRefresh();
            }


        } else {
            // There's nothing left.
            ////Steamcraft.log.debug("No networks around");

        }
        //log.debug("New networks: "+newNets);
        this.shouldRefresh();
        return steamRemoved;

    }

    public synchronized void buildFromTransporter(ISteamTransporter trans, SteamNetwork target, ISteamTransporter ignore) {
        //////Steamcraft.log.debug("Building network!");
        HashSet<ISteamTransporter> checked = new HashSet();
        HashSet<ISteamTransporter> members = target.crawlNetwork(trans, checked, ignore);
        boolean targetIsThis = target == this;
        SteamNetwork net = targetIsThis ? this : SteamNetworkRegistry.getInstance().getNewNetwork();
        for (ISteamTransporter member : members) {
            if (!this.transporters.containsValue(member)) {
                target.addTransporter(member);
            }
        }
        net.addTransporter(trans);
    }

    public boolean contains(ISteamTransporter trans) {
        return this.transporters.containsValue(trans);
    }

    protected HashSet<ISteamTransporter> crawlNetwork(ISteamTransporter trans, HashSet<ISteamTransporter> checked, ISteamTransporter ignore) {
        if (checked == null) {
            checked = new HashSet<ISteamTransporter>();
        }
        if (!checked.contains(trans) && !isClosedValvePipe(trans)) {
            checked.add(trans);
        }
        HashSet<ISteamTransporter> neighbors = getNeighboringTransporters(trans);
        for (ISteamTransporter neighbor : neighbors) {
            //log.debug(neighbor == ignore ? "Should ignore this." : "Should not be ignored");

            if (!checked.contains(neighbor) && neighbor != ignore && !isClosedValvePipe(neighbor)) {
                //log.debug("Didn't ignore");
                checked.add(neighbor);
                crawlNetwork(neighbor, checked, ignore);
            }
        }
        return checked;
    }

    private HashSet<ISteamTransporter> getNeighborTransporters(ISteamTransporter trans) {
        HashSet<ISteamTransporter> out = new HashSet();
        Coord4 coords = trans.getCoords();
        int x = coords.x, y = coords.y, z = coords.z;
        for (ForgeDirection dir : trans.getConnectionSides()) {
            TileEntity te = trans.getWorld().getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
            if (te != null && te instanceof ISteamTransporter) {
                ISteamTransporter neighbor = (ISteamTransporter) te;
                out.add(neighbor);
            }
        }
        return out;
    }

    public void join(SteamNetwork other) {
        for (ISteamTransporter trans : other.transporters.values()) {
            this.addTransporter(trans);
        }
        //this.steam += other.getSteam();
        SteamNetworkRegistry.getInstance().remove(other);
    }

    public int getDimension() {
        if (transporters.size() > 0) {
            return transporters.keySet().iterator().next().dimension;
        } else {
            return -999;
        }

    }

    public World getWorld() {
        if (transporters.values().iterator().hasNext() && transporters.values().iterator().next() != null) {
            return transporters.values().iterator().next().getWorld();
        } else {
            return null;
        }

    }

    public void markDirty() {
        SteamNetworkRegistry.markDirty(this);
    }

    public synchronized void refresh() {
        float press = this.getPressure();
        int targetCapacity = 0;
        //log.debug("Refreshing " + this.name + "; size: "+this.transporters.size());
        if (this.transporters.size() == 0) {
            //log.debug("empty network");
            SteamNetworkRegistry.getInstance().remove(this);
            return;
        }
        try {
            HashMap<Coord4, ISteamTransporter> temp = (HashMap<Coord4, ISteamTransporter>) this.transporters.clone();
            for (Coord4 c : temp.keySet()) {
                TileEntity te = c.getTileEntity(this.getWorld());
                if (te == null || !(te instanceof ISteamTransporter)) {
                    //log.debug("illegal transporter");
                    this.transporters.remove(c);
                } else {
                    if (te instanceof ISteamTransporter) {
                        ISteamTransporter trans = (ISteamTransporter) te;
                        if (trans.getNetwork() != this) {
                            //log.debug("Different network!");
                            this.transporters.remove(c);
                            this.steam -= this.getPressure() * trans.getCapacity();
                            this.transporters.remove(c);
                        } else {
                            //trans.getWorld().setBlock(c.x, c.y+3, c.z, Blocks.brick_block);
                            targetCapacity += trans.getCapacity();
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("SteamNetwork: " + e.getMessage());
            e.printStackTrace();
        }
        if (this.capacity != targetCapacity) {
            log.debug("target: " + targetCapacity + "; curent: " + this.capacity);
            log.debug("steam: " + this.steam + "; pressure: " + this.getPressure());
            int idealSteam = (int) (targetCapacity * press);
            log.debug("idealSteam: " + idealSteam + "; ideal steam: " + (targetCapacity * this.getPressure()));
            this.steam = idealSteam;
            this.capacity = targetCapacity;
        }


    }

    public void shouldRefresh() {
        //log.debug(this.name+": I should refresh");
        this.shouldRefresh = true;
        this.refreshWaitTicks = 40;
    }

    public String getShortName() {
        return this.name.subSequence(0, 5).toString();
    }

}
