package eiteam.esteemedinnovation.api.steamnet;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.ISteamTransporter;
import eiteam.esteemedinnovation.api.util.Coord4;
import eiteam.esteemedinnovation.api.util.SPLog;
import eiteam.esteemedinnovation.tile.pipe.TileEntityValvePipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class SteamNetwork {
    private static Random random = new Random();
    protected SPLog log = EsteemedInnovation.log;
    private int refreshWaitTicks = 0;
    private int globalRefreshTicks = 300;
    private String name;
    private int steam;
    private int capacity;
    private boolean shouldRefresh = false;
    private Coord4[] transporterCoords;
    private int initializedTicks = 0;
    private HashMap<Coord4, ISteamTransporter> transporters = new HashMap<>();

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
            transporters.put(c, null);
        }
        this.name = name;
    }

    public synchronized static SteamNetwork newOrJoin(ISteamTransporter trans) {
        if (isClosedValvePipe(trans)) {
            return null;
        }

        HashSet<ISteamTransporter> others = getNeighboringTransporters(trans);
        HashSet<SteamNetwork> nets = new HashSet<>();
        SteamNetwork theNetwork = null;
        boolean hasJoinedNetwork = false;
        if (others.size() > 0) {
            for (ISteamTransporter t : others) {
                if (!isClosedValvePipe(t)) {
                    if (t.getNetwork() != null) {
                        SteamNetwork net = t.getNetwork();
                        if (net != null) {
                            nets.add(net);
                        }
                    }
                }
            }

            if (nets.size() > 0) {
                SteamNetwork main = null;
                for (SteamNetwork net : nets) {
                    if (main != null) {
                        main.join(net);
                    } else {
                        main = net;
                    }
                }

                if (main != null) {
                    main.addTransporter(trans);
                }
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
        HashSet<ISteamTransporter> out = new HashSet<>();
        Coord4 transCoords = trans.getCoords();
        for (EnumFacing d : trans.getConnectionSides()) {
            TileEntity te = trans.getWorld().getTileEntity(transCoords.pos.offset(d));
            if (te != null && te instanceof ISteamTransporter && te != trans) {
                ISteamTransporter t = (ISteamTransporter) te;
                if (t.getConnectionSides().contains(d.getOpposite())) {
                    out.add(t);
                }
            }
        }
        return out;
    }

    private static boolean isClosedValvePipe(ISteamTransporter trans) {
        return ((trans instanceof TileEntityValvePipe && !(((TileEntityValvePipe) trans).isOpen())));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
//        NBTTagList nbtl = new NBTTagList();
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
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected boolean tick() {
        if (transporters.size() == 0) {
            log.debug("No transporters (" + getShortName() + ")");
            return false;
        }
        if (shouldRefresh) {
            if (refreshWaitTicks > 0) {
                refreshWaitTicks--;
            } else {
                refresh();
                shouldRefresh = false;
            }

        }

        if (globalRefreshTicks > 0) {
            globalRefreshTicks--;
        } else {
            refresh();
            globalRefreshTicks = 300;
        }
        if (initializedTicks >= 1200) {

            if (Config.wimpMode) {
                if (getPressure() > 1.09F) {
                    steam = (int) Math.floor((double) capacity * 1.09D);
                }
            } else {
                if (transporters != null) {
                    if (getPressure() > 1.2F) {
                        for (Coord4 coords : transporters.keySet()) {
                            ISteamTransporter trans = transporters.get(coords);
                            if ((trans == null || ((TileEntity) trans).isInvalid())) {
                                transporters.remove(coords);
                            } else if (!trans.getWorld().isRemote && shouldExplode(oneInX(getPressure(), trans.getPressureResistance()))) {
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
        steam += amount;
        shouldRefresh();
    }

    public synchronized void decrSteam(int amount) {
        steam -= amount;
        if (steam < 0) {
            steam = 0;
        }
    }

    public int getSteam() {
        return steam;
    }

    public int getCapacity() {
        return capacity;
    }

    private int oneInX(float pressure, float resistance) {
        return Math.max(1, (int) Math.floor((double) (500.0F - (pressure / (1.1F + resistance) * 100))));
    }

    private boolean shouldExplode(int oneInX) {
        return oneInX <= 1 || random.nextInt(oneInX - 1) == 0;
    }

    public float getPressure() {
        if (capacity > 0) {
            float altitudePressureModifier = 1F;
            if (transporters != null) {
                int totalY = 0;
                for (Coord4 coord : transporters.keySet()) {
                    totalY += coord.pos.getY();
                }
                int averageY = MathHelper.ceiling_float_int((float) totalY / (float) transporters.size());
                int networkAltitude = 64 - averageY;
                altitudePressureModifier = (float) -(Math.sqrt(Math.abs((float) networkAltitude / 100F))) + 1;
            }
            return ((float) getSteam() / (float) getCapacity()) * altitudePressureModifier;
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
            trans.setNetworkName(getName());
            trans.setNetwork(this);
            addSteam(trans.getSteam());
            trans.wasAdded();
            SteamNetworkRegistry.markDirty(this);
        }
    }

    public void setTransporterCoords(Coord4[] coords) {
        transporterCoords = coords;
    }

    public synchronized void init(World world) {
        if (transporterCoords != null) {
            this.loadTransporters(world);
        }
    }

    public synchronized void loadTransporters(World world) {
        for (int i = transporterCoords.length - 1; i >= 0; i--) {
            Coord4 coords = transporterCoords[i];
            TileEntity te = world.getTileEntity(coords.pos);
            if (te instanceof ISteamTransporter) {
                transporters.put(transporterCoords[i], (ISteamTransporter) te);
            }

        }
    }

    public synchronized int split(ISteamTransporter split, boolean removeCapacity) {
        int steamRemoved = 0;
        if (removeCapacity && getSteam() >= split.getCapacity() * this.getPressure()) {
            steamRemoved = (int) Math.floor((double) split.getCapacity() * (double) this.getPressure());
            steam -= steamRemoved;
            capacity -= split.getCapacity();
        }
        for (ISteamTransporter trans : transporters.values()) {
            trans.updateSteam((int) (trans.getCapacity() * getPressure()));
        }

        //World world = split.getWorldObj();
        //Tuple3<Integer, Integer, Integer> coords = split.getCoords();
        //int x = coords.first, y= coords.second, z=coords.third;
        //HashSet<EnumFacing> dirs = split.getConnectionSides();
        HashSet<SteamNetwork> newNets = new HashSet<>();
        for (ISteamTransporter trans : getNeighboringTransporters(split)) {
            if (!isClosedValvePipe(trans)) {
                boolean isInNetwork = false;
                if (newNets.size() > 0) {
                    for (SteamNetwork net : newNets) {
                        if (net.contains(trans)) {
                            isInNetwork = true;
                            break;
                        }
                    }
                }
                if (!isInNetwork) {
                    SteamNetwork net = SteamNetworkRegistry.getInstance().getNewNetwork();
                    ISteamTransporter ignore = null;
                    if (removeCapacity) {
                        ignore = split;
                    }

                    net.buildFromTransporter(trans, net, ignore);
                    newNets.add(net);
                }
            }

        }
        if (newNets.size() > 0) {
            for (SteamNetwork net : newNets) {
                SteamNetworkRegistry.getInstance().add(net);
                net.shouldRefresh();
            }
        }

        shouldRefresh();
        return steamRemoved;
    }

    public synchronized void buildFromTransporter(ISteamTransporter trans, SteamNetwork target, ISteamTransporter ignore) {
        //////EsteemedInnovation.log.debug("Building network!");
        HashSet<ISteamTransporter> checked = new HashSet<>();
        HashSet<ISteamTransporter> members = target.crawlNetwork(trans, checked, ignore);
        boolean targetIsThis = target == this;
        SteamNetwork net = targetIsThis ? this : SteamNetworkRegistry.getInstance().getNewNetwork();
        for (ISteamTransporter member : members) {
            if (!transporters.containsValue(member)) {
                target.addTransporter(member);
            }
        }
        net.addTransporter(trans);
    }

    public boolean contains(ISteamTransporter trans) {
        return transporters.containsValue(trans);
    }

    protected HashSet<ISteamTransporter> crawlNetwork(ISteamTransporter trans, HashSet<ISteamTransporter> checked, ISteamTransporter ignore) {
        if (checked == null) {
            checked = new HashSet<>();
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
        HashSet<ISteamTransporter> out = new HashSet<>();
        Coord4 coords = trans.getCoords();
        for (EnumFacing dir : trans.getConnectionSides()) {
            TileEntity te = trans.getWorld().getTileEntity(coords.pos.offset(dir));
            if (te != null && te instanceof ISteamTransporter) {
                ISteamTransporter neighbor = (ISteamTransporter) te;
                out.add(neighbor);
            }
        }
        return out;
    }

    public void join(SteamNetwork other) {
        for (ISteamTransporter trans : other.transporters.values()) {
            addTransporter(trans);
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
        float press = getPressure();
        int targetCapacity = 0;
        if (transporters.size() == 0) {
            SteamNetworkRegistry.getInstance().remove(this);
            return;
        }
        HashMap<Coord4, ISteamTransporter> temp = (HashMap<Coord4, ISteamTransporter>) transporters.clone();
        for (Coord4 c : temp.keySet()) {
            TileEntity te = c.getTileEntity(this.getWorld());
            if (te == null || !(te instanceof ISteamTransporter)) {
                this.transporters.remove(c);
            } else {
                ISteamTransporter trans = (ISteamTransporter) te;
                if (trans.getNetwork() != this) {
                    this.transporters.remove(c);
                    this.steam -= this.getPressure() * trans.getCapacity();
                    this.transporters.remove(c);
                } else {
                    targetCapacity += trans.getCapacity();
                }
            }
        }
        int currentCapacity = getCapacity();
        float currentPressure = getPressure();
        if (currentCapacity != targetCapacity) {
            log.debug("target: " + targetCapacity + "; curent: " + currentCapacity);
            log.debug("steam: " + this.steam + "; pressure: " + currentPressure);
            int idealSteam = (int) (targetCapacity * press);
            log.debug("idealSteam: " + idealSteam + "; ideal steam: " + (targetCapacity * currentPressure));
            steam = idealSteam;
            capacity = targetCapacity;
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
