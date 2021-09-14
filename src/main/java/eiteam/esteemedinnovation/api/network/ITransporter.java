package eiteam.esteemedinnovation.api.network;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public interface ITransporter {
    
    World getWorld();
    
    ResourceLocation getNetworkType();
    
    Network getNetwork();
    
    void setNetwork(Network network);
    
    void refresh();
    
    HashSet<Direction> getConnectionSides();
    
    BlockPos getPos();
    
    String getName();
    
    void wasAdded();
    
    boolean isBlocked();
}
