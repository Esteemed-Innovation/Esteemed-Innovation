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
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SteamTransporterTileEntity extends TileEntity implements ISteamTransporter, ITickable {
    public String name = "SteamTransporterTileEntity";
    public float pressureResistance = 0.8F;
    public float lastPressure = -1F;
    public float pressure;
    public int capacity;
    protected SPLog log = Steamcraft.log;
    protected String networkName;
    protected SteamNetwork network;
    protected EnumFacing[] distributionDirections;
    protected boolean shouldJoin = false;
    private int steam = 0;
    private ArrayList<EnumFacing> gaugeSideBlacklist = new ArrayList<>();
    private boolean isInitialized = false;

    public SteamTransporterTileEntity() {
        this(EnumFacing.VALUES);
    }

    public SteamTransporterTileEntity(EnumFacing[] distributionDirections) {
        this.capacity = 10000;
        this.distributionDirections = distributionDirections;
    }

    public SteamTransporterTileEntity(int capacity, EnumFacing[] distributionDirections) {
        this(distributionDirections);
        this.capacity = capacity;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        super.getUpdatePacket();
        NBTTagCompound access = new NBTTagCompound();
        if (networkName != null) {
            access.setString("networkName", networkName);
            access.setFloat("pressure", this.getPressure());
        }
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    public NBTTagCompound getDescriptionTag() {
        NBTTagCompound access = new NBTTagCompound();
        if (networkName != null) {
//            Steamcraft.log.debug("Setting pressure!");
            access.setString("networkName", networkName);
            access.setFloat("pressure", this.getPressure());
        }
        return access;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        if (access.hasKey("networkName")) {
            this.networkName = access.getString("networkName");
            this.pressure = access.getFloat("pressure");
//            Steamcraft.log.debug("Set pressure to "+this.pressure);
        }
        this.markForUpdate();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("steam")) {
            this.steam = compound.getInteger("steam");
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
        compound.setInteger("steam", this.steam);

        return compound;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public float getPressure() {
        return (this.network != null) ? this.network.getPressure() : this.pressure;
    }

    @Override
    public void update() {
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
                    //Steamcraft.log.debug("Updating PRESHAAA");
                    this.markForUpdate();
                    this.lastPressure = this.getPressure();
                    this.network.markDirty();
                }
            }
        }
    }

    @Override
    public void insertSteam(int amount, EnumFacing face) {
        if (this.network != null) {
            this.network.addSteam(amount);
        }
    }

    @Override
    public void decrSteam(int i) {
        if (this.network != null && this.network.getSteam() != 0) {
            this.network.decrSteam(i);
        }
    }

    @Override
    public void explode() {
        this.network.decrSteam((int) (this.network.getSteam() * 0.1F));
        this.network.split(this, true);
        this.worldObj.createExplosion(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 4F, true);
    }

    private boolean isValidSteamSide(EnumFacing face) {
        for (EnumFacing d : distributionDirections) {
            if (d == face) {
                return true;
            }
        }
        return false;
    }

    public void addSideToGaugeBlacklist(EnumFacing face) {
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

    public float getPressureResistance() {
        return this.pressureResistance;
    }

    protected void setPressureResistance(float resistance) {
        this.pressureResistance = resistance;
    }

    public void setDistributionDirections(EnumFacing[] faces) {
        this.distributionDirections = faces;
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
            return (int) (Math.floor((double) this.getCapacity() * (double) this.network.getPressure()));
        }
        return 0;
    }

    @Override
    public int getSteam() {
        return this.steam;
    }

    public boolean hasGauge() {
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (this.acceptsGauge(dir)) {
                BlockPos offsetPos = pos.offset(dir);
                Block block = worldObj.getBlockState(offsetPos).getBlock();
                if (block instanceof BlockSteamGauge || block instanceof BlockRuptureDisc)
                    return true;
            }
        }
        return false;
    }

    public void refresh() {
        /*
        if (!worldObj.isRemote) {
            FMLRelaunchLog.info("Refreshing", null);
        }
        */
        if (this.network == null && !worldObj.isRemote) {
            if (SteamNetworkRegistry.getInstance().isInitialized(this.getDimension())) {
                /*
                Steamcraft.log.debug("Null network");
				if (this.networkName != null && SteamNetworkRegistry.getInstance().isInitialized(this.getDimension())){
					Steamcraft.log.debug("I have a network!");
					this.network = SteamNetworkRegistry.getInstance().getNetwork(this.networkName, this);
					this.network.rejoin(this);
				} else {
                    Steamcraft.log.debug("Requesting new network or joining existing.en");
				}
				*/
                SteamNetwork.newOrJoin(this);
                this.isInitialized = true;
                this.markForUpdate();
            }
        }
    }

    public int getDimension() {
        return this.worldObj.provider.getDimension();
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
        this.markDirty();
    }

    @Override
    public void wasAdded() {}

    /**
     * Gets the current BlockPos offset by the EnumFacing.
     * @param facing The direction
     */
    public BlockPos getOffsetPos(EnumFacing facing) {
        return new BlockPos(pos.getX() + facing.getFrontOffsetX(), pos.getY() + facing.getFrontOffsetY(),
          pos.getZ() + facing.getFrontOffsetZ());
    }
}
