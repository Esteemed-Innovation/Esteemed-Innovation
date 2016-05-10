package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.ISteamTransporter;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitySteamGauge extends TileEntity {

    private int lastCompOutput = 0;
    private ForgeDirection[] cardinals = new ForgeDirection[]{
            ForgeDirection.NORTH,
            ForgeDirection.SOUTH,
            ForgeDirection.EAST,
            ForgeDirection.WEST
    };

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            int compOutput = getComparatorOutput();
            if (compOutput != lastCompOutput) {
                lastCompOutput = compOutput;
                markDirty();
//				for (int i = 0; i <= 3; i++){
//					if (cardinals[i] != myDir()){
//						ForgeDirection d = cardinals[i];
//						worldObj.getBlock(xCoord, yCoord, zCoord);
//						worldObj.markBlockForUpdate(xCoord + d.offsetX, yCoord, zCoord + d.offsetZ);
//					}
//				}
            }
        }
    }

    public float getPressure() {
        ForgeDirection d = myDir().getOpposite();
        ISteamTransporter source = null;
        TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord + d.offsetZ);
        if (te != null && te instanceof ISteamTransporter) {
            if (te instanceof TileEntitySteamCharger) {
                TileEntitySteamCharger charger = (TileEntitySteamCharger) te;
                return charger.getSteamInItem();
            } else {
                source = (ISteamTransporter) te;
                return source.getPressure();
            }

        }
        return 0.0F;
    }

    public int getComparatorOutput() {
        return (int) (15 * (100 * ((double) getPressure() * 0.01D)));
        ////Steamcraft.log.debug(out);
    }

    private ForgeDirection myDir() {
        return ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
    }


}
