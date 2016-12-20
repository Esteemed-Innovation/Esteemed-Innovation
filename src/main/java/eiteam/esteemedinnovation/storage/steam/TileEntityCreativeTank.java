package eiteam.esteemedinnovation.storage.steam;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import eiteam.esteemedinnovation.api.steamnet.SteamNetwork;

public class TileEntityCreativeTank extends TileEntitySteamTank implements ITickable {
    public TileEntityCreativeTank() {
        super();
        name = "Creative Tank";
        capacity = 1;
    }

    @Override
    public int getSteamShare() {
        return 0;
    }

    @Override
    public void update() {
        super.update();
        SteamNetwork net = getNetwork();
        if (net != null && net.getCapacity() > 100 && net.getPressure() < 1F) {
            int capacity = net.getCapacity();
            float pressure = net.getPressure();
            if (capacity > 100 && pressure < 1F) {
                float toAdd = (1F - pressure) * 0.1F * capacity;
                insertSteam((int) (Math.floor((double) toAdd)), EnumFacing.UP);
            }
        }
    }
}
