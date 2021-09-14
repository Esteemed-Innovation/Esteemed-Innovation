package eiteam.esteemedinnovation.modules.transport.steam;

import eiteam.esteemedinnovation.api.network.INetworkFactory;
import eiteam.esteemedinnovation.api.network.Network;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class SteamNetworkFactory implements INetworkFactory {
    
    @Override
    public Network create(World world, CompoundNBT tag) {
        SteamNetwork network = new SteamNetwork(world);
    
        if(tag.contains("tank")) {
            network.getFluidTank().readFromNBT(tag.getCompound("tank"));
        }
        if(tag.contains("capacity")) {
            network.getFluidTank().setCapacity(tag.getInt("capacity"));
        }
        
        return network;
    }
    @Override
    public Network create(World world) {
        return new SteamNetwork(world);
    }
}
