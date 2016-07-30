package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.tile.SteamReactorTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntitySteamGauge extends SteamReactorTileEntity implements ITickable {
    private int lastCompOutput = 0;

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            int compOutput = getComparatorOutput();
            if (compOutput != lastCompOutput) {
                lastCompOutput = compOutput;
                markForUpdate();
            }
        }
    }

    @Override
    public float getPressure(EnumFacing dir) {
        ISteamTransporter transporter = getAdjacentTransporter(dir);
        if (transporter == null) {
            return 0F;
        }
        if (transporter instanceof TileEntitySteamCharger) {
            return ((TileEntitySteamCharger) transporter).getSteamInItem();
        }
        return transporter.getPressure();
    }

    public int getComparatorOutput() {
//        TODO
        return (int) (15 * (100 * ((double) getPressure(EnumFacing.NORTH) * 0.01D)));
    }
}
