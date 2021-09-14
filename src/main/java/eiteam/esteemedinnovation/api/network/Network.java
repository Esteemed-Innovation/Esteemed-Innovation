package eiteam.esteemedinnovation.api.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class Network {
    
    protected World world;
    protected HashMap<BlockPos, ITransporter> transporters = new HashMap<>();
    
    public Network(World world) {
       this.world = world;
    }
    
    public abstract ResourceLocation getType();
    
    public abstract void update();
    
    public abstract void onTransporterAdded(ITransporter transporter);
    
    public abstract void onTransporterRemoved(ITransporter transporter);
    
    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        return nbt;
    }
    
    public World getWorld() {
        return world;
    }
    
    public final void addTransporter(ITransporter transporter) {
        if(transporter != null && !contains(transporter)) {
            transporters.put(transporter.getPos(), transporter);
            //transporter.setNetworkName(getName());
            transporter.setNetwork(this);
            onTransporterAdded(transporter);
        }
    }
    
    public void join(Network other) {
        if(other.getType() != this.getType()) {
            throw new IllegalArgumentException("Can not merge two networks of different types. Expected: " + this.getType());
        }
        for(ITransporter transporter : other.transporters.values()) {
            addTransporter(transporter);
        }
        NetworkManager.get(world).getNetworkData().remove(other);
    }
    
    public void split(ITransporter split) {
        HashSet<Network> newNets = new HashSet<>();
        for(ITransporter transporter : getNeighboringTransporters(split)) {
            if(!transporter.isBlocked()) {
                boolean isInNetwork = false;
                if(newNets.size() > 0) {
                    for (Network net : newNets) {
                        if(net.contains(transporter)) {
                            isInNetwork = true;
                            break;
                        }
                    }
                }
                if(!isInNetwork) {
                    INetworkFactory factory = NetworkRegistry.INSTANCE.getFactory(split.getNetworkType());
                    if(factory == null) {
                        throw new RuntimeException("Could not find network factory of type: " + split.getNetworkType());
                    }
                    Network net = factory.create(world);
                    net.buildFromTransporter(transporter, null);
                    NetworkManager.get(world).getNetworkData().add(net);
                    newNets.add(net);
                }
            }
        }
    }
    
    public void buildFromTransporter(ITransporter transporter, ITransporter ignore) {
        Set<ITransporter> members = crawlNetwork(transporter, ignore);
        for(ITransporter member : members) {
            if(!contains(member)) {
                addTransporter(member);
            }
        }
    }
    
    protected Set<ITransporter> crawlNetwork(ITransporter transporter, ITransporter ignore) {
        return crawlNetwork(transporter, new HashSet<>(), ignore);
    }
    
    protected Set<ITransporter> crawlNetwork(ITransporter transporter, Set<ITransporter> checked, ITransporter ignore) {
        if(checked == null) {
            checked = new HashSet<>();
        }
        if(!checked.contains(transporter) && !transporter.isBlocked()) {
            checked.add(transporter);
        }
        for(ITransporter neighbor : getNeighboringTransporters(transporter)) {
            if(!checked.contains(neighbor) && neighbor != ignore && !neighbor.isBlocked()) {
                checked.add(neighbor);
                crawlNetwork(neighbor, checked, ignore);
            }
        }
        return checked;
    }
    
    public boolean contains(ITransporter transporter) {
        return transporters.containsValue(transporter);
    }
    
    public static Set<ITransporter> getNeighboringTransporters(ITransporter transporter) {
        Set<ITransporter> result = new HashSet<>();
        BlockPos pos = transporter.getPos();
        for(Direction dir : transporter.getConnectionSides()) {
            TileEntity te = transporter.getWorld().getTileEntity(pos.offset(dir));
            if(te instanceof ITransporter && te != transporter) {
                ITransporter t = (ITransporter) te;
                if(t.getNetwork() != null && transporter.getNetwork() != null && t.getNetwork().getType() == transporter.getNetwork().getType()) {
                    if (t.getConnectionSides().contains(dir.getOpposite())) {
                        result.add(t);
                    }
                }
            }
        }
        return result;
    }
    
    public static Network newOrJoin(ITransporter transporter) {
        if(transporter.isBlocked()) {
            return null;
        }
        
        Set<ITransporter> neighbors = getNeighboringTransporters(transporter);
        Set<Network> nets = new HashSet<>();
        Network network = null;
        boolean hasJoinedNetwork = false;
        if(!neighbors.isEmpty()) {
            neighbors.stream()
              .filter(t -> !t.isBlocked() && t.getNetwork() != null)
              .forEach(t -> nets.add(t.getNetwork()));
            
            if(!nets.isEmpty()) {
                Network main = null;
                for(Network net : nets) {
                    if(main != null) {
                        main.join(net);
                    } else {
                        main = net;
                    }
                }
                if(main != null) {
                    main.addTransporter(transporter);
                    hasJoinedNetwork = true;
                    network = main;
                }
            }
        }
        
        if(!hasJoinedNetwork) {
            INetworkFactory factory = NetworkRegistry.INSTANCE.getFactory(transporter.getNetworkType());
            if(factory == null) {
                return null;
            }
            network = factory.create(transporter.getWorld());
            network.addTransporter(transporter);
            NetworkManager.get(transporter.getWorld()).getNetworkData().add(network);
        }
        
        return network;
    }
}
