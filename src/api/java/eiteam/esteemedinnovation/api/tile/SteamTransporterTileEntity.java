package eiteam.esteemedinnovation.api.tile;

import eiteam.esteemedinnovation.api.SteamTransporter;
import eiteam.esteemedinnovation.api.steamnet.SteamNetwork;
import eiteam.esteemedinnovation.api.steamnet.SteamNetworkRegistry;
import eiteam.esteemedinnovation.api.util.Coord4;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SteamTransporterTileEntity extends TileEntityBase implements SteamTransporter, ITickable {
    public String name = "SteamTransporterTileEntity";
    private float pressureResistance = 0.8F;
    private float lastPressure = -1F;
    private float pressure;
    protected int capacity;
    protected String networkName;
    protected SteamNetwork network;
    protected EnumFacing[] distributionDirections;
    private boolean shouldJoin = false;
    private int steam = 0;
    private ArrayList<EnumFacing> gaugeSideBlacklist = new ArrayList<>();
    private boolean isInitialized = false;
    private boolean shouldExplode;
    private boolean hasExploded;

    public SteamTransporterTileEntity() {
        this(EnumFacing.VALUES);
    }

    public SteamTransporterTileEntity(EnumFacing[] distributionDirections) {
        this(10_000, distributionDirections);
    }

    public SteamTransporterTileEntity(int capacity, EnumFacing[] distributionDirections) {
        this.distributionDirections = distributionDirections;
        this.capacity = capacity;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        super.getUpdatePacket();
        NBTTagCompound access = new NBTTagCompound();
        if (networkName != null) {
            access.setString("networkName", networkName);
            access.setFloat("pressure", getPressure());
        }
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound access = new NBTTagCompound();
        if (networkName != null) {
//            EsteemedInnovation.log.debug("Setting pressure!");
            access.setString("networkName", networkName);
            access.setFloat("pressure", getPressure());
        }
        return access;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        if (access.hasKey("networkName")) {
            networkName = access.getString("networkName");
            pressure = access.getFloat("pressure");
//            EsteemedInnovation.log.debug("Set pressure to "+this.pressure);
        }
        markForResync();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("steam")) {
            steam = compound.getInteger("steam");
            /*
            log.debug("Read steam from NBT: "+this.steam);
        } else {
            log.debug("Didn't read steam from NBT.");
            */
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        //log.debug("writing STTE to NBT with steam: "+this.steam);
        compound.setInteger("steam", steam);

        return compound;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public float getPressure() {
        SteamNetwork net = getNetwork();
        return net == null ? pressure : net.getPressure();
    }

    @Override
    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    @Override
    public void update() {
        if (!isInitialized || shouldJoin) {
            refresh();
        }
        if (!worldObj.isRemote) {
            if (steam != getSteamShare()) {
                steam = getSteamShare();
                markDirty();
            }
            SteamNetwork net = getNetwork();
            if (hasGauge() && net != null) {
                if (Math.abs(getPressure() - lastPressure) > 0.01F) {
                    //EsteemedInnovation.log.debug("Updating PRESHAAA");
                    markForResync();
                    lastPressure = getPressure();
                    net.markDirty();
                }
            }
            if (shouldExplode) {
                shouldExplode = false;
                explode();
            }
        }
    }

    @Override
    public void insertSteam(int amount, EnumFacing face) {
        SteamNetwork net = getNetwork();
        if (net != null) {
            net.addSteam(amount);
        }
    }

    @Override
    public void decrSteam(int i) {
        SteamNetwork net = getNetwork();
        if (net != null && net.getSteam() != 0) {
            net.decrSteam(i);
        }
    }

    @Override
    public void explode() {
        SteamNetwork net = getNetwork();
        net.decrSteam((int) (net.getSteam() * 0.1F));
        net.split(this, true);
        worldObj.createExplosion(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 4F, true);
    }

    @Override
    public void shouldExplode() {
        shouldExplode = true;
    }

    private boolean isValidSteamSide(EnumFacing face) {
        for (EnumFacing d : distributionDirections) {
            if (d == face) {
                return true;
            }
        }
        return false;
    }

    protected void addSideToGaugeBlacklist(EnumFacing face) {
        gaugeSideBlacklist.add(face);
    }

    public void addSidesToGaugeBlacklist(EnumFacing[] faces) {
        for (EnumFacing face : faces) {
            addSideToGaugeBlacklist(face);
        }
    }

    @Override
    public boolean doesConnect(EnumFacing face) {
        return isValidSteamSide(face);
    }

    @Override
    public boolean acceptsGauge(EnumFacing face) {
        return !gaugeSideBlacklist.contains(face);
    }

    @Override
    public float getPressureResistance() {
        return pressureResistance;
    }

    protected void setPressureResistance(float resistance) {
        pressureResistance = resistance;
    }

    /**
     * Sets the distribution directions to everything except the provided directions.
     * @param exclusions The directions to exclude form the distribution directions.
     */
    protected void setValidDistributionDirectionsExcluding(EnumFacing... exclusions) {
        EnumFacing[] validDirs = new EnumFacing[6 - exclusions.length];
        int i = 0;
        List<EnumFacing> exclusionList = Arrays.asList(exclusions);
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (!exclusionList.contains(dir)) {
                validDirs[i] = dir;
                i++;
            }
        }
        setDistributionDirections(validDirs);
    }

    protected void setDistributionDirections(EnumFacing[] faces) {
        distributionDirections = faces;
    }

    @Override
    public HashSet<EnumFacing> getConnectionSides() {
        HashSet<EnumFacing> out = new HashSet<>();
        out.addAll(Arrays.asList(distributionDirections));
        return out;
    }

    @Override
    public Coord4 getCoords() {
        return new Coord4(pos, worldObj.provider.getDimension());
    }

    @Override
    public void setNetworkName(String name) {
        networkName = name;
    }

    @Override
    public SteamNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(SteamNetwork network) {
        this.network = network;
    }

    @Override
    public int getSteamShare() {
        SteamNetwork net = getNetwork();
        if (net != null) {
            return (int) (Math.floor((double) getCapacity() * (double) net.getPressure()));
        }
        return 0;
    }

    @Override
    public int getSteam() {
        return steam;
    }

    public boolean hasGauge() {
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (acceptsGauge(dir)) {
                BlockPos offsetPos = pos.offset(dir);
                TileEntity tile = worldObj.getTileEntity(offsetPos);
                if (tile instanceof SteamReactorTileEntity) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void refresh() {
        /*
        if (!worldObj.isRemote) {
            FMLRelaunchLog.info("Refreshing", null);
        }
        */
        if (getNetwork() == null && !worldObj.isRemote) {
            if (SteamNetworkRegistry.getInstance().isInitialized(getDimension())) {
                /*
                EsteemedInnovation.log.debug("Null network");
				if (this.networkName != null && SteamNetworkRegistry.getInstance().isInitialized(this.getDimension())){
					EsteemedInnovation.log.debug("I have a network!");
					this.network = SteamNetworkRegistry.getInstance().getNetwork(this.networkName, this);
					this.network.rejoin(this);
				} else {
                    EsteemedInnovation.log.debug("Requesting new network or joining existing.en");
				}
				*/
                SteamNetwork.newOrJoin(this);
                isInitialized = true;
                markForResync();
            }
        }
    }

    @Override
    public int getDimension() {
        return worldObj.provider.getDimension();
    }

    @Override
    public World getWorldObj() {
        return worldObj;
    }

    @Override
    public void updateSteam(int amount) {
        steam = amount;
    }

    protected void shouldJoin() {
        shouldJoin = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void wasAdded() {}

    /**
     * Gets the current BlockPos offset by the EnumFacing.
     * @param facing The direction
     */
    protected BlockPos getOffsetPos(EnumFacing facing) {
        return pos.offset(facing);
    }
}
