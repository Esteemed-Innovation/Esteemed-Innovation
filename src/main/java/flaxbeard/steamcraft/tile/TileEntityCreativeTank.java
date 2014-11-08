package flaxbeard.steamcraft.tile;

import net.minecraftforge.common.util.ForgeDirection;


public class TileEntityCreativeTank extends TileEntitySteamTank {

    public TileEntityCreativeTank() {
        super();
        this.name = "Creative Tank";
        this.capacity = 1;
    }

    @Override
    public int getSteamShare() {
        return 0;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.getNetwork() != null && this.getNetwork().getCapacity() > 100) {
            //log.debug(this.getNetwork().getName());
            //log.debug("net cap: "+this.getNetwork().getCapacity());
            if (this.getNetwork().getPressure() < 1F) {
                //log.debug("adding steam");
                float toAdd = (1F - this.getNetwork().getPressure()) * 0.1F * this.getNetwork().getCapacity();
                this.insertSteam((int) (Math.floor((double) toAdd)), ForgeDirection.UP);
            }
        }
    }
}
