package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.block.BlockPipe;
import flaxbeard.steamcraft.network.ConnectPacket;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.Cuboid6;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TileEntitySteamPipe extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable {
    //protected FluidTank dummyFluidTank = FluidRegistry.isFluidRegistered("steam") ? new FluidTank(new FluidStack(FluidRegistry.getFluid("steam"), 0),10000) : null;
    public ArrayList<Integer> blacklistedSides = new ArrayList<>();
    public Block disguiseBlock = null;
    public int disguiseMeta = 0;
    public boolean isOriginalPipe = false;
    public boolean isOtherPipe = false;
    protected boolean isLeaking = false;
    private boolean lastWrench = false;

    public TileEntitySteamPipe() {
        super(EnumFacing.VALUES);
        name = "Pipe";
    }

    public TileEntitySteamPipe(int capacity) {
        this();
        this.capacity = capacity;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getDescriptionTag();
        access.setBoolean("isLeaking", isLeaking);
        NBTTagCompound list = new NBTTagCompound();
        int g = 0;
        for (int i : blacklistedSides) {
            list.setInteger(Integer.toString(g), i);
            g++;
        }
        list.setInteger("size", g);
        access.setTag("blacklistedSides", list);
        access.setInteger("disguiseBlock", Block.getIdFromBlock(disguiseBlock));
        access.setInteger("disguiseMeta", disguiseMeta);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        isLeaking = access.getBoolean("isLeaking");
        NBTTagCompound sidesList = access.getCompoundTag("blacklistedSides");
        int length = sidesList.getInteger("size");
        Integer[] sidesInt = new Integer[length];
        for (int i = 0; i < length; i++) {
            sidesInt[i] = sidesList.getInteger(Integer.toString(i));
        }
        blacklistedSides = new ArrayList<>(Arrays.asList(sidesInt));
        disguiseBlock = Block.getBlockById(access.getInteger("disguiseBlock"));
        disguiseMeta = access.getInteger("disguiseMeta");
        markDirty();
    }


    @Override
    public void readFromNBT(NBTTagCompound access) {
        super.readFromNBT(access);
        NBTTagCompound sidesList = access.getCompoundTag("blacklistedSides");
        int length = sidesList.getInteger("size");
        Integer[] sidesInt = new Integer[length];
        for (int i = 0; i < length; i++) {
            sidesInt[i] = sidesList.getInteger(Integer.toString(i));
        }
        blacklistedSides = new ArrayList<>(Arrays.asList(sidesInt));
        disguiseBlock = Block.getBlockById(access.getInteger("disguiseBlock"));
        disguiseMeta = access.getInteger("disguiseMeta");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound access) {
        super.writeToNBT(access);
        NBTTagCompound list = new NBTTagCompound();
        int g = 0;
        for (int i : blacklistedSides) {
            list.setInteger(Integer.toString(g), i);
            g++;
        }
        list.setInteger("size", g);
        access.setTag("blacklistedSides", list);
        access.setInteger("disguiseBlock", Block.getIdFromBlock(disguiseBlock));
        access.setInteger("disguiseMeta", disguiseMeta);

        return access;
    }

    /**
     * Calls the superclass of this class #update without doing all of our own updating. See: Valve Pipe.
     */
    public void superUpdate() {
        super.update();
    }

    public ArrayList<EnumFacing> getMyDirections() {
        ArrayList<EnumFacing> myDirections = new ArrayList<>();
        for (EnumFacing direction : EnumFacing.VALUES) {
            TileEntity tile = worldObj.getTileEntity(getOffsetPos(direction));
            if (this.doesConnect(direction) && tile != null) {
                if (tile instanceof ISteamTransporter) {
                    ISteamTransporter target = (ISteamTransporter) tile;
                    if (target.doesConnect(direction.getOpposite())) {
                        myDirections.add(direction);
                    }
                } else if (tile instanceof IFluidHandler && Steamcraft.steamRegistered) {
                    IFluidHandler target = (IFluidHandler) tile;
                    if (target.canDrain(direction.getOpposite(), FluidRegistry.getFluid("steam")) ||
                      target.canFill(direction.getOpposite(), FluidRegistry.getFluid("steam"))) {
                        myDirections.add(direction);
                    }
                }
            }
        }

        return myDirections;
    }

    @Override
    public void update() {
        super.update();
        /*
        if (worldObj.isRemote) {
            boolean hasWrench = BlockSteamPipeRenderer.updateWrenchStatus();
            if (hasWrench != lastWrench && !(disguiseBlock == null || disguiseBlock == Blocks.AIR)) {
                markDirty();
            }
            lastWrench = hasWrench;
        }
        */

        ArrayList<EnumFacing> myDirections = getMyDirections();
        int i = 0;
        if (myDirections.size() > 0) {
            EnumFacing direction = myDirections.get(0).getOpposite();
            while (!doesConnect(direction) || direction == myDirections.get(0)) {
                direction = EnumFacing.getHorizontal((direction.ordinal() + 1) % 5);
            }
            BlockPos dirPos = getOffsetPos(direction);
            if (!worldObj.isRemote) {
                if (myDirections.size() == 2 && getSteamShare() > 0 && (worldObj.isAirBlock(dirPos) ||
                  !worldObj.isSideSolid(dirPos, direction.getOpposite()))) {
                    worldObj.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, Steamcraft.SOUND_LEAK,
                      SoundCategory.BLOCKS, 2F, 0.9F, false);
                    if (!isLeaking) {
                        isLeaking = true;
                        markDirty();
                    }

                } else {
                    if (isLeaking) {
                        isLeaking = false;
                        markDirty();
                    }
                }
                while (myDirections.size() == 2 && getPressure() > 0 && i < 10 &&
                  (worldObj.isAirBlock(dirPos) || !worldObj.isSideSolid(dirPos, direction.getOpposite()))) {
                    decrSteam(10);
                    i++;
                }
            }
            if (worldObj.isRemote && isLeaking) {
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5F, pos.getY() + 0.5F,
                  pos.getZ() + 0.5F, direction.getFrontOffsetX() * 0.1F, direction.getFrontOffsetY() * 0.1F,
                  direction.getFrontOffsetZ() * 0.1F);
            }
        }
    }

    @Override
    public HashSet<EnumFacing> getConnectionSides() {
        HashSet<EnumFacing> out = new HashSet<>();
        HashSet<EnumFacing> blacklist = new HashSet<>();
        for (int i : blacklistedSides) {
            blacklist.add(EnumFacing.getFront(i));
        }
        for (EnumFacing d : distributionDirections) {
            if (!blacklist.contains(d)) {
                out.add(d);
            }

        }
        return out;
    }

    @Override
    public boolean doesConnect(EnumFacing face) {
        for (int i : blacklistedSides) {
            if (EnumFacing.getFront(i) == face) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getSteamShare() {
        SteamNetwork network = getNetwork();
        if (network == null) {
            this.network = null;
            networkName = null;
            return 0;
        } else {
            return (int) Math.floor((double) network.getPressure() * (double) capacity);
        }

    }

    private int canConnectSide(int side) {
        EnumFacing direction = EnumFacing.getFront(side);
        BlockPos pos = getOffsetPos(direction);
        TileEntity tile = worldObj.getTileEntity(pos);
        if (tile != null && tile instanceof ISteamTransporter) {
            ISteamTransporter target = (ISteamTransporter) tile;
            if (target.doesConnect(direction.getOpposite())) {
                return target instanceof TileEntitySteamPipe ? 2 : 1;
            }
            if (target instanceof TileEntitySteamPipe && ((TileEntitySteamPipe) target).blacklistedSides.contains(direction.getOpposite().ordinal())) {
                return 2;
            }
        }
        return 0;
    }

    public void addTraceableCuboids(List<IndexedCuboid6> cuboids) {
        float min = 4F / 16F;
        float max = 12F / 16F;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (canConnectSide(0) > 0) {
            float bottom = canConnectSide(0) == 2 ? -5F / 16F : 0.0F;
            cuboids.add(new IndexedCuboid6(0, new Cuboid6(x + min, y + bottom, z + min, x + max, y + 5F / 16F, z + max)));
        }
        if (canConnectSide(1) > 0) {
            float top = canConnectSide(1) == 2 ? 21F / 16F : 1.0F;
            cuboids.add(new IndexedCuboid6(1, new Cuboid6(x + min, y + 11F / 16F, z + min, x + max, y + top, z + max)));
        }
        if (canConnectSide(2) > 0) {
            float bottom = canConnectSide(2) == 2 ? -5F / 16F : 0.0F;
            cuboids.add(new IndexedCuboid6(2, new Cuboid6(x + min, y + min, z + bottom, x + max, y + max, z + 5F / 16F)));
        }
        if (canConnectSide(3) > 0) {
            float top = canConnectSide(3) == 2 ? 21F / 16F : 1.0F;
            cuboids.add(new IndexedCuboid6(3, new Cuboid6(x + min, y + min, z + 11F / 16F, x + max, y + max, z + top)));
        }
        if (canConnectSide(4) > 0) {
            float bottom = canConnectSide(4) == 2 ? -5F / 16F : 0.0F;
            cuboids.add(new IndexedCuboid6(4, new Cuboid6(x + bottom, y + min, z + min, x + 5F / 16F, y + max, z + max)));
        }
        if (canConnectSide(5) > 0) {
            float top = canConnectSide(5) == 2 ? 21F / 16F : 1.0F;
            cuboids.add(new IndexedCuboid6(5, new Cuboid6(x + 11F / 16F, y + min, z + min,x + top, y + max, z + max)));
        }
        cuboids.add(new IndexedCuboid6(6, new Cuboid6(x + 5F / 16F, y + 5F / 16F, z + 5F / 16F, x + 11F / 16F, y + 11F / 16F, z + 11F / 16F)));
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (this.disguiseBlock != null) {
                if (!player.capabilities.isCreativeMode) {
                    EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(disguiseBlock, 1, disguiseMeta));
                    world.spawnEntityInWorld(entityItem);
                }
                SoundType sound = disguiseBlock.getSoundType();
                world.playSound((double) pos.getX() + 0.5F, (double) (pos.getY() + 0.5F),
                  (double) (pos.getZ() + 0.5F), sound.getBreakSound(), SoundCategory.BLOCKS,
                  (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F, false);
                disguiseBlock = null;
                markDirty();
                return true;
            }
        } else {
            if (this.worldObj.isRemote) {
                RayTraceResult hit = RayTracer.retraceBlock(world, player, pos);
                // Use ryatracer to get the subpart that was hit. The # corresponds with a forge direction.
                if (hit == null) {
                    return false;
                }

                ConnectPacket packet = new ConnectPacket(pos, hit.subHit);
                Steamcraft.channel.sendToServer(packet);
            }
        }
        return false;
    }

    public void connectDisconnect(World world, BlockPos pos, int subHit) {
        // Use raytracer to get the subpart that was hit. The # corresponds with a forge direction.
        // If hit a part from 0 to 5 (direction) and hit me

        if ((subHit >= 0) && (subHit < 6) && world.getBlockState(pos).getBlock() instanceof BlockPipe) {
            //Make sure that you can't make an 'end cap' by allowing less than 2 directions to connect
            int sidesConnect = 0;
            for (int i = 0; i < 6; i++) {
                if (doesConnect(EnumFacing.getFront(i))) {
                    sidesConnect++;
                }
            }
            boolean netChange = false;
            //If does connect on this side, and has adequate sides left
            EnumFacing direction = EnumFacing.getFront(subHit);
            if (doesConnect(direction)) {
                TileEntity tile = worldObj.getTileEntity(getOffsetPos(direction));
                if (tile instanceof TileEntitySteamPipe && ((TileEntitySteamPipe) tile).blacklistedSides.contains(direction.getOpposite().ordinal())) {
                    TileEntitySteamPipe pipe = (TileEntitySteamPipe) tile;
                    pipe.blacklistedSides.remove((Integer) direction.getOpposite().ordinal());

                    //network stuff

                    pipe.shouldJoin();
                    pipe.isOtherPipe = true;
                    //pipe.getNetwork().addSteam(steam);
                    markForUpdate();
                } else if (sidesConnect > 2) {
                    //add to blacklist
                    blacklistedSides.add(subHit);
                    {
                        if (tile != null && tile instanceof ISteamTransporter) {
                            ISteamTransporter p = (ISteamTransporter) tile;
                            SteamNetwork network = p.getNetwork();
                            if (network != null) {
                                network.shouldRefresh();
                            }
                        }
                    }
                    isOriginalPipe = true;
                    //bad network stuff
                    int steam = getNetwork().split(this, false);
                    shouldJoin();
                    //this.getNetwork().addSteam(steam);

                    refreshNeighbors();
                    network.shouldRefresh();
                    markForUpdate();
                }
            }
            //else if doesn't connect
            else if (!doesConnect(direction)) {
                if (blacklistedSides.contains(subHit)) {
                    //remove from whitelist
                    blacklistedSides.remove((Integer) subHit);
                    //network stuff
                    int steam = getNetwork().split(this, false);
                    shouldJoin();
                    //this.getNetwork().addSteam(steam);
                    ////Steamcraft.log.debug("C");
                    ////Steamcraft.log.debug(this.getNetworkName());
                    ////Steamcraft.log.debug("steam: "+steam+"; nw steam: "+this.getNetwork().getSteam());
                    markForUpdate();
                }
            }
//            if (getSteamShare() > 0) {
                //world.playSoundEffect(x+0.5F, y+0.5F, z+0.5F, "steamcraft:leaking", 2.0F, 0.9F);
//            }
            world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, Steamcraft.SOUND_WRENCH,
              SoundCategory.BLOCKS, 2F, 0.9F, false);
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        isOriginalPipe = false;
        isOtherPipe = false;
    }

    private void refreshNeighbors() {
        //log.debug("Refreshing neighbors");
        for (EnumFacing dir : EnumFacing.VALUES) {
            TileEntity te = worldObj.getTileEntity(getOffsetPos(dir));
            if (te != null && te instanceof ISteamTransporter) {
                ISteamTransporter trans = (ISteamTransporter) te;
                SteamNetwork transNetwork = trans.getNetwork();
                if (transNetwork != null && transNetwork != getNetwork()) {
                    transNetwork.shouldRefresh();
                }
            }
        }
    }
}
