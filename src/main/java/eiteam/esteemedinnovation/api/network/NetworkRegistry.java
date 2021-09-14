package eiteam.esteemedinnovation.api.network;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class NetworkRegistry {
    
    public static final NetworkRegistry INSTANCE = new NetworkRegistry();
    
    private final Map<ResourceLocation, INetworkFactory> factories = new HashMap<>();
    
    private NetworkRegistry() {
    
    }
    
    public void addFactory(ResourceLocation type, INetworkFactory factory) {
        if(factories.containsKey(type)) {
            throw new RuntimeException("Can't register duplicate network type: " + type.toString());
        }
        
        factories.put(type, factory);
    }
    
    public INetworkFactory getFactory(ResourceLocation type) {
        return factories.get(type);
    }
}
