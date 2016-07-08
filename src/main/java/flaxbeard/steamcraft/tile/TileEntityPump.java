package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;

public class TileEntityPump extends SteamTransporterTileEntity implements IFluidHandler, ISteamTransporter {
    public FluidTank myTank = new FluidTank(1000);
    public int progress = 0;
    public int rotateTicks = 0;
    private boolean running = false;
    private static final int STEAM_CONSUMPTION = Config.screwConsumption;

    public TileEntityPump() {
        super(EnumFacing.VALUES);
        this.addSidesToGaugeBlacklist(EnumFacing.VALUES);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getDescriptionTag();
        access.setShort("progress", (short) progress);
        if (myTank.getFluid() != null) {
            access.setString("fluid", myTank.getFluid().getFluid().getName());
        }
        access.setBoolean("running", running);
        access.setInteger("fluidQuantity", myTank.getFluidAmount());
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        progress = access.getShort("progress");
        if (access.hasKey("fluid")) {
            int amt = access.getInteger("fluidQuantity");
            myTank.setFluid(new FluidStack(FluidRegistry.getFluid(access.getString("fluid")), amt));
        }
        running = access.getBoolean("running");
        markForUpdate();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        progress = nbt.getShort("progress");

        if (nbt.hasKey("fluid")) {
            int amt = nbt.getInteger("fluidQuantity");
            myTank.setFluid(new FluidStack(FluidRegistry.getFluid(nbt.getString("fluid")), amt));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("progress", (short) progress);

        nbt.setShort("water", (short) myTank.getFluidAmount());
        if (myTank.getFluid() != null) {
            nbt.setString("fluid", myTank.getFluid().getFluid().getName());
        }
        nbt.setInteger("fluidQuantity", myTank.getFluidAmount());
        return nbt;
    }

    private EnumFacing getOutputDirection() {
        return getInputDirection().getOpposite();
    }

    private EnumFacing getInputDirection() {
        return EnumFacing.getFront(getBlockMetadata());
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (from != this.getOutputDirection()) {
            return null;
        }
        if (resource == null || !resource.isFluidEqual(myTank.getFluid())) {
            return null;
        }
        return myTank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        if (from == this.getOutputDirection()) {
            return myTank.drain(maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return (from == this.getOutputDirection());
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{myTank.getInfo()};
    }

    @Override
    public void update() {
        super.update();
        if (worldObj.isRemote) {
            if (running && progress < 100) {
                progress++;
                rotateTicks++;
            }
        } else {
            EnumFacing inputDir = getInputDirection();
            BlockPos offsetPos = getOffsetPos(inputDir);
            Fluid fluid = FluidRegistry.lookupFluidForBlock(worldObj.getBlockState(offsetPos).getBlock());
            if (getSteamShare() >= STEAM_CONSUMPTION && myTank.getFluidAmount() == 0 && getBlockMetadata() == 0 &&
              fluid != null && myTank.getFluidAmount() < 1000) {
                myTank.fill(new FluidStack(fluid, 1000), true);
                worldObj.setBlockToAir(offsetPos);
                progress = 0;
                decrSteam(STEAM_CONSUMPTION);
                running = true;
                markDirty();
            }
            if (myTank.getFluidAmount() > 0 && myTank.getFluid() != null && progress < 100) {
                progress++;
                rotateTicks++;
            }
            EnumFacing outputDir = getOutputDirection();
            offsetPos = getOffsetPos(outputDir);
            if (myTank.getFluidAmount() > 0 && progress == 100) {
                TileEntity tile = worldObj.getTileEntity(offsetPos);
                if (tile != null && tile instanceof IFluidHandler) {
                    IFluidHandler fluidHandler = (IFluidHandler) tile;
                    if (fluidHandler.canFill(inputDir, myTank.getFluid().getFluid())) {
                        int amnt = fluidHandler.fill(inputDir, myTank.getFluid(), true);
                        if (amnt > 0) {
                            myTank.drain(amnt, true);
                            if (myTank.getFluidAmount() == 0) {
                                running = false;
                                progress = 0;

                            }
                        } else if (running) {
                            running = false;
                        }
                    } else if (running) {
                        running = false;
                    }
                } else if (running) {
                    running = false;
                }
                markDirty();
            }
        }
    }
}
