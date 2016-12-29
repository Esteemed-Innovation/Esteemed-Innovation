package eiteam.esteemedinnovation.api.steamnet;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.SteamTransporter;
import eiteam.esteemedinnovation.api.util.Coord4;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;

public class SteamNetwork {
    private static Random random = new Random();
    private int refreshWaitTicks = 0;
    private int globalRefreshTicks = 300;
    private String name;
    private int steam;
    private int capacity;
    private boolean shouldRefresh = false;
    private Coord4[] transporterCoords;
    private int initializedTicks = 0;
    private HashMap<Coord4, SteamTransporter> transporters = new HashMap<>();

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

    public synchronized static SteamNetwork newOrJoin(SteamTransporter trans) {
        if (!trans.canSteamPassThrough()) {
            return null;
        }

        HashSet<SteamTransporter> others = getNeighboringTransporters(trans);
        HashSet<SteamNetwork> nets = new HashSet<>();
        SteamNetwork theNetwork = null;
        boolean hasJoinedNetwork = false;
        if (others.size() > 0) {
            others.stream()
              .filter(t -> t.canSteamPassThrough() && t.getNetwork() != null)
              .forEach(t -> nets.add(t.getNetwork()));

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

    public static HashSet<SteamTransporter> getNeighboringTransporters(SteamTransporter trans) {
        HashSet<SteamTransporter> out = new HashSet<>();
        Coord4 transCoords = trans.getCoords();
        for (EnumFacing d : trans.getConnectionSides()) {
            TileEntity te = trans.getWorldObj().getTileEntity(transCoords.getPos().offset(d));
            if (te != null && te instanceof SteamTransporter && te != trans) {
                SteamTransporter t = (SteamTransporter) te;
                if (t.getConnectionSides().contains(d.getOpposite())) {
                    out.add(t);
                }
            }
        }
        return out;
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

            if (Constants.wimpMode) {
                if (getPressure() > 1.09F) {
                    steam = (int) Math.floor((double) capacity * 1.09D);
                }
            } else {
                if (transporters != null) {
                    if (getPressure() > 1.2F) {
                        Iterator<Map.Entry<Coord4, SteamTransporter>> iter = transporters.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<Coord4, SteamTransporter> entry = iter.next();
                            SteamTransporter trans = entry.getValue();
                            if ((trans == null || ((TileEntity) trans).isInvalid())) {
                                iter.remove();
                            } else if (!trans.getWorldObj().isRemote && shouldExplode(oneInX(getPressure(), trans.getPressureResistance()))) {
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
                    totalY += coord.getPos().getY();
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

    public synchronized void addTransporter(SteamTransporter trans) {
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
            TileEntity te = world.getTileEntity(coords.getPos());
            if (te instanceof SteamTransporter) {
                transporters.put(transporterCoords[i], (SteamTransporter) te);
            }

        }
    }

    public synchronized int split(SteamTransporter split, boolean removeCapacity) {
        int steamRemoved = 0;
        if (removeCapacity && getSteam() >= split.getCapacity() * this.getPressure()) {
            steamRemoved = (int) Math.floor((double) split.getCapacity() * (double) this.getPressure());
            steam -= steamRemoved;
            capacity -= split.getCapacity();
        }
        for (SteamTransporter trans : transporters.values()) {
            trans.updateSteam((int) (trans.getCapacity() * getPressure()));
        }

        //World world = split.getWorldObj();
        //Tuple3<Integer, Integer, Integer> coords = split.getCoords();
        //int x = coords.first, y= coords.second, z=coords.third;
        //HashSet<EnumFacing> dirs = split.getConnectionSides();
        HashSet<SteamNetwork> newNets = new HashSet<>();
        for (SteamTransporter trans : getNeighboringTransporters(split)) {
            if (trans.canSteamPassThrough()) {
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
                    SteamTransporter ignore = null;
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

    public synchronized void buildFromTransporter(SteamTransporter trans, SteamNetwork target, SteamTransporter ignore) {
        //////EsteemedInnovation.log.debug("Building network!");
        HashSet<SteamTransporter> checked = new HashSet<>();
        HashSet<SteamTransporter> members = target.crawlNetwork(trans, checked, ignore);
        boolean targetIsThis = target == this;
        SteamNetwork net = targetIsThis ? this : SteamNetworkRegistry.getInstance().getNewNetwork();
        for (SteamTransporter member : members) {
            if (!transporters.containsValue(member)) {
                target.addTransporter(member);
            }
        }
        net.addTransporter(trans);
    }

    public boolean contains(SteamTransporter trans) {
        return transporters.containsValue(trans);
    }

    protected HashSet<SteamTransporter> crawlNetwork(SteamTransporter trans, HashSet<SteamTransporter> checked, SteamTransporter ignore) {
        if (checked == null) {
            checked = new HashSet<>();
        }
        if (!checked.contains(trans) && trans.canSteamPassThrough()) {
            checked.add(trans);
        }
        HashSet<SteamTransporter> neighbors = getNeighboringTransporters(trans);
        for (SteamTransporter neighbor : neighbors) {
            //log.debug(neighbor == ignore ? "Should ignore this." : "Should not be ignored");

            if (!checked.contains(neighbor) && neighbor != ignore && trans.canSteamPassThrough()) {
                //log.debug("Didn't ignore");
                checked.add(neighbor);
                crawlNetwork(neighbor, checked, ignore);
            }
        }
        return checked;
    }

    private HashSet<SteamTransporter> getNeighborTransporters(SteamTransporter trans) {
        HashSet<SteamTransporter> out = new HashSet<>();
        Coord4 coords = trans.getCoords();
        for (EnumFacing dir : trans.getConnectionSides()) {
            TileEntity te = trans.getWorldObj().getTileEntity(coords.getPos().offset(dir));
            if (te != null && te instanceof SteamTransporter) {
                SteamTransporter neighbor = (SteamTransporter) te;
                out.add(neighbor);
            }
        }
        return out;
    }

    public void join(SteamNetwork other) {
        for (SteamTransporter trans : other.transporters.values()) {
            addTransporter(trans);
        }
        //this.steam += other.getSteam();
        SteamNetworkRegistry.getInstance().remove(other);
    }

    public int getDimension() {
        if (transporters.size() > 0) {
            return transporters.keySet().iterator().next().getDimension();
        } else {
            return -999;
        }

    }

    public World getWorld() {
        if (transporters.values().iterator().hasNext() && transporters.values().iterator().next() != null) {
            return transporters.values().iterator().next().getWorldObj();
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
        HashMap<Coord4, SteamTransporter> temp = (HashMap<Coord4, SteamTransporter>) transporters.clone();
        for (Coord4 c : temp.keySet()) {
            TileEntity te = c.getTileEntity(this.getWorld());
            if (te == null || !(te instanceof SteamTransporter)) {
                this.transporters.remove(c);
            } else {
                SteamTransporter trans = (SteamTransporter) te;
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
            int idealSteam = (int) (targetCapacity * press);
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
