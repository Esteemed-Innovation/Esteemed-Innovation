package eiteam.esteemedinnovation.api.network.steam;

import eiteam.esteemedinnovation.api.network.ITransporter;

public interface ISteamTransporter extends ITransporter {
    
    void shouldExplode();
    
    float getPressureResistance();
    
    int getCapacity();
}
