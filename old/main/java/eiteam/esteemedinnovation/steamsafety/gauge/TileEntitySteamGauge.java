package eiteam.esteemedinnovation.steamsafety.gauge;

import eiteam.esteemedinnovation.api.SteamTransporter;
import eiteam.esteemedinnovation.api.tile.SteamReactorTileEntity;
import eiteam.esteemedinnovation.charging.TileEntitySteamCharger;
import eiteam.esteemedinnovation.steamsafety.SafetyModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class TileEntitySteamGauge extends SteamReactorTileEntity {
    private int lastCompOutput;

    @Override
    public boolean canUpdate(IBlockState target) {
        return target.getBlock() == SafetyModule.STEAM_GAUGE;
    }

    @Override
    public void safeUpdate() {
        if (!world.isRemote) {
            int compOutput = getComparatorOutput(world.getBlockState(pos).getValue(BlockSteamGauge.FACING));
            if (compOutput != lastCompOutput) {
                lastCompOutput = compOutput;
                markForUpdate();
            }
        }
    }

    @Override
    public float getPressure(EnumFacing dir) {
        SteamTransporter transporter = getAdjacentTransporter(dir);
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
