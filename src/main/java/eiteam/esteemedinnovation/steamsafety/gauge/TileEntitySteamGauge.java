package eiteam.esteemedinnovation.steamsafety.gauge;

import eiteam.esteemedinnovation.api.ISteamTransporter;
import eiteam.esteemedinnovation.api.tile.SteamReactorTileEntity;
import eiteam.esteemedinnovation.charging.TileEntitySteamCharger;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntitySteamGauge extends SteamReactorTileEntity implements ITickable {
    private int lastCompOutput;

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            int compOutput = getComparatorOutput(worldObj.getBlockState(pos).getValue(BlockSteamGauge.FACING));
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

    public int getComparatorOutput(EnumFacing dir) {
        return (int) (15 * (100 * (getPressure(dir) * 0.01D)));
    }
}
