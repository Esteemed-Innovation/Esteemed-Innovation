package flaxbeard.steamcraft.api.util;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class UtilSteamTransport {
    
	private UtilSteamTransport() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	private static String[] boom = new String[]{
            "It can't withstand that kind of pressure!",
            "She's holding all she can, cap'n!",
            "Your pipes asplode",
            "Boom!"
    };

    public static void generalPressureEvent(World world, int x, int y, int z, float pressure, int capacity) {
        if (pressure > 1.2F) {
            ISteamTransporter trans = (ISteamTransporter) world.getTileEntity(x, y, z);
            float resistance = trans.getPressureResistance();
            int steam = trans.getSteamShare();
            int oneInX = Math.max(1, (int) Math.floor((double) (500.0F - (pressure / (1.1F + resistance) * 100))));
            ////Steamcraft.log.debug(steam + "/" + capacity +" = " +(((float)steam) / ((float)capacity)));
            ////Steamcraft.log.debug("100 - (" + pressure + " / (1.1F + "+resistance+") *100 )" + "chance of explosion: 1 in "+oneInX);
            if (oneInX <= 1 || world.rand.nextInt(oneInX - 1) == 0) {

                //Steamcraft.log.debug("FSP: "+boom[world.rand.nextInt(boom.length)]);
                trans.explode();
                world.createExplosion(null, x + 0.5F, y + 0.5F, z + 0.5F, 4.0F, true);
            }
        }
    }

    public static void generalDistributionEvent(World worldObj, int xCoord, int yCoord, int zCoord, ForgeDirection[] values) {
        if (!worldObj.isRemote) {
            ISteamTransporter trans = (ISteamTransporter) worldObj.getTileEntity(xCoord, yCoord, zCoord);
            for (ForgeDirection direction : values) {
                if (worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ) != null) {
                    TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
                    if (tile instanceof ISteamTransporter) {
                        ISteamTransporter target = (ISteamTransporter) tile;
                        if (trans.getPressure() > target.getPressure() && target.canInsert(direction.getOpposite())) {
                            int change = (int) (Math.floor(trans.getSteamShare() * target.getCapacity() - target.getSteamShare() * trans.getCapacity()) / (trans.getCapacity() + target.getCapacity()));
                            if (change > 0 && change <= trans.getSteamShare()) {
                                trans.decrSteam(change);
                                target.insertSteam(change, direction.getOpposite());
                            }
                        }
                    } else if (tile instanceof IFluidHandler && Steamcraft.steamRegistered && trans instanceof TileEntitySteamPipe) {
                        IFluidHandler target = (IFluidHandler) tile;
                        float cap = 0;
                        float steam = 0;
                        if (target.getTankInfo(direction.getOpposite()) != null) {
                            for (FluidTankInfo info : target.getTankInfo(direction.getOpposite())) {
                                if (info.fluid == null) {
                                    cap += info.capacity / 10.0F;
                                } else if (info.fluid.getFluid() == FluidRegistry.getFluid("steam")) {
                                    steam += info.fluid.amount / 10.0F;
                                    cap += info.capacity / 10.0F;
                                }
                            }
                        }
                        float pressure = (float) steam / (float) cap;
                        if (target.canFill(direction.getOpposite(), FluidRegistry.getFluid("steam")) && trans.getPressure() > pressure) {
                            int change = (int) (Math.floor(trans.getSteamShare() * cap - steam * trans.getCapacity()) / (trans.getCapacity() + cap));
                            if (change > 0 && change <= trans.getSteamShare()) {
                                trans.decrSteam(change - target.fill(direction.getOpposite(), new FluidStack(FluidRegistry.getFluid("steam"), change * 10), true) / 10);
                            }
                        }
                    }
                }
            }
        }

    }

    public static void preExplosion(World worldObj, int xCoord, int yCoord, int zCoord, ForgeDirection[] values) {
        ISteamTransporter trans = (ISteamTransporter) worldObj.getTileEntity(xCoord, yCoord, zCoord);
        for (ForgeDirection direction : values) {
            if (worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ) != null) {
                TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
                if (tile instanceof ISteamTransporter) {
                    ISteamTransporter target = (ISteamTransporter) tile;
                    int change = (int) (-10.0F * ((float) target.getCapacity() / 100.0F));
                    trans.decrSteam(change);
                }
            }
        }
    }
}
