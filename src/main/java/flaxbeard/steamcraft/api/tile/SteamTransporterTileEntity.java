package flaxbeard.steamcraft.api.tile;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import flaxbeard.steamcraft.api.util.Coord4;
import flaxbeard.steamcraft.api.util.SPLog;
import flaxbeard.steamcraft.block.BlockRuptureDisc;
import flaxbeard.steamcraft.block.BlockSteamGauge;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.HashSet;

public class SteamTransporterTileEntity extends TileEntity implements ISteamTransporter {

    public String name = "SteamTransporterTileEntity";
    public float pressureResistance = 0.8F;
    public float lastPressure = -1F;
    public float pressure;
    public int capacity;
    protected SPLog log = Steamcraft.log;
    protected String networkName;
    protected SteamNetwork network;
    protected ForgeDirection[] distributionDirections;
    protected boolean shouldJoin = false;
    private int steam = 0;
    private ArrayList<ForgeDirection> gaugeSideBlacklist = new ArrayList<ForgeDirection>();
    private boolean isInitialized = false;

    public SteamTransporterTileEntity() {
        this(ForgeDirection.VALID_DIRECTIONS);
    }


    public SteamTransporterTileEntity(ForgeDirection[] distributionDirections) {
        this.capacity = 10000;
        this.distributionDirections = distributionDirections;
    }

    public SteamTransporterTileEntity(int capacity, ForgeDirection[] distributionDirections) {
        this(distributionDirections);
        this.capacity = capacity;
    }

    @Override
    public Packet getDescriptionPacket() {
        super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        if (networkName != null) {
            access.setString("networkName", networkName);
            access.setFloat("pressure", this.getPressure());
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
    }

    public NBTTagCompound getDescriptionTag() {
        NBTTagCompound access = new NBTTagCompound();
        if (networkName != null) {
            //////Steamcraft.log.debug("Setting pressure!");
            access.setString("networkName", networkName);
            access.setFloat("pressure", this.getPressure());
        }
        return access;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.func_148857_g();
        if (access.hasKey("networkName")) {
            this.networkName = access.getString("networkName");
            this.pressure = access.getFloat("pressure");
            //////Steamcraft.log.debug("Set pressure to "+this.pressure);
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("steam")) {
            this.steam = compound.getInteger("steam");
            //log.debug("Read steam from NBT: "+this.steam);
        } else {
            //log.debug("Didn't read steam from NBT.");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        //log.debug("writing STTE to NBT with steam: "+this.steam);
        compound.setInteger("steam", this.steam);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public float getPressure() {
        return (this.network != null) ? (float) this.network.getPressure() : this.pressure;
    }

    @Override
    public void updateEntity() {
        if (!this.isInitialized || this.shouldJoin) {
            this.refresh();
        }
        if (!worldObj.isRemote) {
            if (this.steam != this.getSteamShare()) {
                this.steam = this.getSteamShare();
                markDirty();
            }
            if (this.hasGauge() && this.network != null) {
                if (Math.abs(this.getPressure() - this.lastPressure) > 0.01F) {
                    //////Steamcraft.log.debug("Updating PRESHAAA");
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    this.lastPressure = this.getPressure();
                    this.network.markDirty();
                }

            }
        }

    }

    @Override
    public void insertSteam(int amount, ForgeDirection face) {
        if (this.network != null) {
            this.network.addSteam(amount);
        }
    }

    @Override
    public void decrSteam(int i) {
        if (this.network != null) {
            this.network.decrSteam(i);
        }
    }

    @Override
    public void explode() {
        this.network.decrSteam((int) (this.network.getSteam() * 0.1F));
        this.network.split(this, true);
        this.worldObj.createExplosion(null, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 4.0F, true);
    }

    private boolean isValidSteamSide(ForgeDirection face) {
        for (ForgeDirection d : distributionDirections) {
            if (d == face) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean canInsert(ForgeDirection face) {
        return isValidSteamSide(face);
    }


    public void addSideToGaugeBlacklist(ForgeDirection face) {
        gaugeSideBlacklist.add(face);
    }

    public void addSidesToGaugeBlacklist(ForgeDirection[] faces) {
        for (ForgeDirection face : faces) {
            addSideToGaugeBlacklist(face);
        }
    }

    @Override
    public boolean doesConnect(ForgeDirection face) {
        return isValidSteamSide(face);
    }


    @Override
    public boolean acceptsGauge(ForgeDirection face) {
        return !gaugeSideBlacklist.contains(face);
    }

    public float getPressureResistance() {
        return this.pressureResistance;
    }

    protected void setPressureResistance(float resistance) {
        this.pressureResistance = resistance;
    }

    public void setDistributionDirections(ForgeDirection[] faces) {
        this.distributionDirections = faces;
    }


    @Override
    public HashSet<ForgeDirection> getConnectionSides() {
        HashSet<ForgeDirection> out = new HashSet();
        for (ForgeDirection d : distributionDirections) {
            out.add(d);
        }
        return out;
    }


    @Override
    public Coord4 getCoords() {
        return new Coord4(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
    }


    @Override
    public String getNetworkName() {
        return this.networkName;
    }

    public void setNetworkName(String name) {
        this.networkName = name;
    }

    public SteamNetwork getNetwork() {
        return this.network;
    }

    public void setNetwork(SteamNetwork network) {
        this.network = network;
    }

    @Override
    public int getSteamShare() {
        if (this.network != null) {
            int mySteam = (int) (Math.floor((double) this.getCapacity() * (double) this.network.getPressure()));
            return mySteam;
        }
        return 0;
    }

    @Override
    public int getSteam() {
        return this.steam;
    }

    public boolean hasGauge() {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (this.acceptsGauge(dir)) {
                Block block = worldObj.getBlock(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
                if (block instanceof BlockSteamGauge || block instanceof BlockRuptureDisc)
                    return true;
            }
        }
        return false;
    }


    public void refresh() {
        if (!worldObj.isRemote) {
            //FMLRelaunchLog.info("Refreshing", null);
        }
        if (this.network == null && !worldObj.isRemote) {
            if (SteamNetworkRegistry.getInstance().isInitialized(this.getDimension())) {
                //////Steamcraft.log.debug("Null network");
//				if (this.networkName != null && SteamNetworkRegistry.getInstance().isInitialized(this.getDimension())){
//					////Steamcraft.log.debug("I have a network!");
//					this.network = SteamNetworkRegistry.getInstance().getNetwork(this.networkName, this);
//					this.network.rejoin(this);
//				} else {
                ////Steamcraft.log.debug("Requesting new network or joining existing.en");
                SteamNetwork.newOrJoin(this);

//				}
                this.isInitialized = true;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
    }

    public int getDimension() {
        return this.worldObj.provider.dimensionId;
    }


    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    public void updateSteam(int amount) {
        this.steam = amount;
    }

    protected void shouldJoin() {
        this.shouldJoin = true;
    }

    public String getName() {
        return this.name;
    }

    protected void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void wasAdded() {
        //this.steam = 0;
    }
}
