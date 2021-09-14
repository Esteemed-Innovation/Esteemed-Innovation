package eiteam.esteemedinnovation.api.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface INetworkFactory {
    Network create(World world);
    
    Network create(World world, CompoundNBT tag);
}
