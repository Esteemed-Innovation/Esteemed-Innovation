package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.block.BlockFlashBoiler;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.misc.FluidHelper;
import flaxbeard.steamcraft.misc.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.HashSet;

public class TileEntityFlashBoiler extends TileEntityBoiler implements IFluidHandler, ISidedInventory, ISteamTransporter {
    private static final int[] SLOTS_TOP = new int[] { 0, 1 };
    private static final int[] SLOTS_BOTTOM = new int[] { 0, 1 };
    private static final int[] SLOTS_SIDES = new int[] { 0, 1 };
    private static final int CAPACITY = 12500;
    // ====================================================
    //          All the possible configurations
    // ====================================================
    // bottom   top
    // OO       OO     Z ^
    // XO       OO     X-->
    private static final int[][] BBL = new int[][] {
      new int[] { 0, 0, 0 }, new int[] { 1, 0, 0 }, new int[] { 0, 0, 1 }, new int[] { 1, 0, 1 },
      new int[] { 0, 1, 0 }, new int[] { 1, 1, 0 }, new int[] { 0, 1, 1 }, new int[] { 1, 1, 1 }
    };
    // bottom   top
    // OO       OO     Z ^
    // OO       XO     X-->
    private static final int[][] TBL = new int[][] {
      new int[] { 0, -1, 0 }, new int[] { 1, -1, 0 }, new int[] { 0, -1, 1 }, new int[] { 1, -1, 1 },
      new int[] { 0, 0, 0 }, new int[] { 1, 0, 0 }, new int[] { 0, 0, 1 }, new int[] { 1, 0, 1 }
    };
    // bottom   top
    // OO       OO     Z ^
    // OX       OO     X-->
    private static final int[][] BBR = new int[][] {
      new int[] { -1, 0, 0 }, new int[] { 0, 0, 0 }, new int[] { -1, 0, 1 }, new int[] { 0, 0, 1 },
      new int[] { -1, 1, 0 }, new int[] { 0, 1, 0 }, new int[] { -1, 1, 1 }, new int[] { 0, 1, 1 }
    };
    // bottom   top
    // OO       OO     Z ^
    // OO       OX     X-->
    private static final int[][] tbr = new int[][] {
      new int[] { -1, -1, 0 }, new int[] { 0, -1, 0 }, new int[] { -1, -1, 1 }, new int[] { 0, -1, 1 },
      new int[] { -1, 0, 0 }, new int[] { 0, 0, 0 }, new int[] { -1, 0, 1 }, new int[] { 0, 0, 1 }
    };
    // bottom   top
    // XO       OO     Z ^
    // OO       OO     X-->
    private static final int[][] BTL = new int[][] {
      new int[] { 0, 0, -1 }, new int[] { 1, 0, -1 }, new int[] { 0, 0, 0 }, new int[] { 1, 0, 0 },
      new int[] { 0, 1, -1 }, new int[] { 1, 1, -1 }, new int[] { 0, 1, 0 }, new int[] { 1, 1, 0 }
    };
    // bottom   top
    // OO       XO     Z ^
    // OO       OO     X-->
    private static final int[][] TTL = new int[][] {
      new int[] { 0, -1, -1 }, new int[] { 1, -1, -1 }, new int[] { 0, -1, 0}, new int[] { 1, -1, 0},
      new int[] { 0, 0, -1 }, new int[] { 1, 0, -1 }, new int[] { 0, 0, 0 }, new int[] { 1, 0, 0 }
    };
    // bottom   top
    // OX       OO     Z ^
    // OO       OO     X-->
    private static final int[][] BTR = new int[][] {
      new int[] { -1, 0, -1 }, new int[] { 0, 0, -1 }, new int[] { -1, 0, 0 }, new int[] { 0, 0, 0 },
      new int[] { -1, 1, -1 }, new int[] { 0, 1, -1 }, new int[] { -1, 1, 0 }, new int[] { 0, 1, 0 }
    };
    // bottom   top
    // OO       OX     Z ^
    // OO       OO     X-->
    private static final int[][] TTR = new int[][] {
      new int[] { -1, -1, -1 }, new int[] { 0, -1, -1 }, new int[] { -1, -1, 0 }, new int[] { 0, -1, 0 },
      new int[] { -1, 0, -1 }, new int[] { 0, 0, -1 }, new int[] { -1, 0, 0 }, new int[] { 0, 0, 0 }
    };

    private static final int[][][] VALID_CONFIGS = new int[][][] {
      BBL, TBL, BBR, tbr, BTL, TTL, BTR, TTR
    };

    private int furnaceCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int heat;
    private ItemStack[] furnaceItemStacks = new ItemStack[2];
    private String customName;
    private boolean wasBurning = false;
    private boolean shouldExplode = false;
    private boolean waitOneTick = true;
    private int frontSide = -1;
    private boolean burning;

    public TileEntityFlashBoiler() {
        super(CAPACITY);
        super.myTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 1), 80000);
        setPressureResistance(0.5F);
    }

    private BlockFlashBoiler.Corners getMyCorner() {
        IBlockState state = worldObj.getBlockState(pos);
        return state.getValue(BlockFlashBoiler.CORNER);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        frontSide = nbt.getInteger("frontSide");
        NBTTagList nbttaglist = (NBTTagList) nbt.getTag("Items");
        furnaceItemStacks = new ItemStack[2];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound compound = nbttaglist.getCompoundTagAt(i);
            byte b0 = compound.getByte("Slot");

            if (b0 >= 0 && b0 < furnaceItemStacks.length) {
                furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(compound);
            }
        }

        furnaceBurnTime = nbt.getShort("BurnTime");
        furnaceCookTime = nbt.getShort("CookTime");
        currentItemBurnTime = nbt.getShort("cIBT");

        if (nbt.hasKey("CustomName")) {
            customName = nbt.getString("CustomName");
        }

        if (nbt.hasKey("water")) {
            myTank.setFluid(new FluidStack(FluidRegistry.WATER, nbt.getShort("water")));
        }

        if (nbt.hasKey("heat")) {
            heat = nbt.getShort("heat");
        }
        // worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("frontSide", frontSide);
        nbt.setShort("BurnTime", (short) furnaceBurnTime);
        nbt.setShort("water", (short) myTank.getFluidAmount());
        nbt.setShort("heat", (short) heat);

        nbt.setShort("CookTime", (short) furnaceCookTime);
        nbt.setShort("cIBT", (short) currentItemBurnTime);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < furnaceItemStacks.length; ++i) {
            if (furnaceItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);

        if (hasCustomName()) {
            nbt.setString("CustomName", customName);
        }

        return nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();
        access.setInteger("frontSide", frontSide);
        access.setInteger("water", myTank.getFluidAmount());
        access.setShort("BurnTime", (short) furnaceBurnTime);
        access.setShort("CookTime", (short) furnaceCookTime);
        access.setShort("cIBT", (short) currentItemBurnTime);
        access.setBoolean("burning", burning);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        frontSide = access.getInteger("frontSide");
        myTank.setFluid(new FluidStack(FluidRegistry.WATER, access.getInteger("water")));
        furnaceBurnTime = access.getShort("BurnTime");
        currentItemBurnTime = access.getShort("cIBT");
        furnaceCookTime = access.getShort("CookTime");
        burning = access.getBoolean("burning");

        super.markForUpdate();
    }

    public void checkMultiblock(boolean isBreaking, int frontSide) {
        if (!worldObj.isRemote) {
            if (!isBreaking) {
                int[] validClusters = getValidClusters();

                if (validClusters.length == 1) {
                    updateMultiblock(validClusters[0], true, frontSide);
                }
            }
        }
    }

    public void destroyMultiblock() {
        updateMultiblock(getValidClusterFromMetadata(), false, -1);
    }

    private int getValidClusterFromMetadata() {
        int validCluster = -1;
        // Because the clusters at the top are doofy and not in the right order =\
        switch (getBlockMetadata()) {
            case 1: {
                validCluster = 0;
                break;
            }
            case 2: {
                validCluster = 2;
                break;
            }
            case 3: {
                validCluster = 4;
                break;
            }
            case 4: {
                validCluster = 6;
                break;
            }
            case 5: {
                validCluster = 1;
                break;
            }
            case 6: {
                validCluster = 3;
                break;
            }
            case 7: {
                validCluster = 5;
                break;
            }
            case 8: {
                validCluster = 7;
                break;
            }
        }

        return validCluster;
    }

    private int checkCluster(int[][] cluster) {
        int count = 0;
        for (int pos = 0; pos < 8; pos++) {
            int x = cluster[pos][0] + this.pos.getX();
            int y = cluster[pos][1] + this.pos.getY();
            int z = cluster[pos][2] + this.pos.getZ();
            BlockPos blockPos = new BlockPos(x, y, z);
            IBlockState state = worldObj.getBlockState(blockPos);
            Block block = state.getBlock();
            if (block == SteamNetworkBlocks.Blocks.FLASH_BOILER.getBlock() &&
              state.getValue(BlockFlashBoiler.CORNER) == BlockFlashBoiler.Corners.NONE) {
                count++;
            }
        }

        return count;
    }

    private int[] getValidClusters() {
        int[] valid = new int[8];
        int[] out;
        int count = 0;
        for (int clusterIndex = 0; clusterIndex < 8; clusterIndex++) {
            if (checkCluster(VALID_CONFIGS[clusterIndex]) == 8) {
                valid[count] = clusterIndex;
                count++;
            }
        }
        out = new int[count];
        System.arraycopy(valid, 0, out, 0, count);
        return out;
    }

    private int[][] getClusterCoords(int clusterIndex) {
        int[][] cluster = VALID_CONFIGS[clusterIndex];
        int[][] out = new int[8][3];
        for (int pos = 0; pos < 8; pos++) {
            out[pos] = new int[] {
              cluster[pos][0] + this.pos.getX(),
              cluster[pos][1] + this.pos.getY(),
              cluster[pos][2] + this.pos.getZ()
            };
        }
        return out;
    }

    private void updateMultiblock(int clusterIndex, boolean isMultiblock, int frontSide) {
        int[][] cluster = getClusterCoords(clusterIndex);
        HashSet<TileEntityFlashBoiler> boilers = new HashSet<>();
        for (int pos = 7; pos >= 0; pos--) {
            int x = cluster[pos][0], y = cluster[pos][1], z = cluster[pos][2];
            BlockPos blockPos = new BlockPos(x, y, z);
            IBlockState state = worldObj.getBlockState(blockPos);
            if (state.getBlock() == SteamNetworkBlocks.Blocks.FLASH_BOILER.getBlock()) {
                BlockFlashBoiler.Corners corner = isMultiblock ? BlockFlashBoiler.Corners.LOOKUP[pos + 1] : BlockFlashBoiler.Corners.NONE;
                worldObj.setBlockState(blockPos, state.withProperty(BlockFlashBoiler.CORNER, corner));

                TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) worldObj.getTileEntity(blockPos);
                if (boiler == null) {
                    return;
                }
                boiler.setFront(frontSide);
                boilers.add(boiler);
            }
        }
        for (TileEntityFlashBoiler boiler : boilers) {
            if (isMultiblock) {
                SteamNetwork.newOrJoin(boiler);
            } else {
                SteamNetwork net = getNetwork();
                if (net != null) {
                    net.split(boiler, true);
                }
            }
        }
    }

    private TileEntityFlashBoiler getPrimaryTileEntity() {
        int[][] cluster = getClusterCoords(getValidClusterFromMetadata());
        int x = cluster[0][0], y = cluster[0][1], z = cluster[0][2];
        BlockPos blockPos = new BlockPos(x, y, z);
        if (isPrimary(worldObj, blockPos)) {
            return (TileEntityFlashBoiler) worldObj.getTileEntity(blockPos);
        }

        return null;
    }

    private void setFront(int frontSide) {
        if (!worldObj.isRemote) {
            this.frontSide = frontSide;
        }
    }

    public int getFront() {
        return frontSide;
    }

    private static boolean isPrimary(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == SteamNetworkBlocks.Blocks.FLASH_BOILER.getBlock() &&
          state.getValue(BlockFlashBoiler.CORNER) == BlockFlashBoiler.Corners.TOP_LEFT_BACK;
    }

    @Override
    public void update() {
        super.update();
        // fixes existing capacity and prevents explosions
        if (capacity != CAPACITY) {
            int steamToLose = Math.abs(capacity - CAPACITY);
            decrSteam(steamToLose);
            capacity = CAPACITY;
        }

        if (shouldExplode) {
            getNetwork().split(this, true);
            worldObj.createExplosion(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 4.0F, true);
            return;
        }
        ////Steamcraft.log.debug(this.getFront());
       if (waitOneTick) {
           waitOneTick = false;
       } else {
            if (!worldObj.isRemote && isPrimary(worldObj, pos)) {
            	ItemStack stackInInput = getStackInSlot(1);
        		if (FluidHelper.itemStackIsWaterContainer(stackInInput)) {
            		ItemStack drainedItemStack = FluidHelper.fillTankFromItem(stackInInput, myTank);
            		setInventorySlotContents(1, drainedItemStack);
                }

                int maxThisTick = 10;

                if (!worldObj.isRemote) {
                    if (furnaceBurnTime == 0 && canSmelt()) {
                        currentItemBurnTime = furnaceBurnTime = getItemBurnTime(furnaceItemStacks[0]);

                        if (furnaceBurnTime > 0) {
                            if (furnaceItemStacks[0] != null) {
                                --furnaceItemStacks[0].stackSize;

                                if (furnaceItemStacks[0].stackSize == 0) {
                                    furnaceItemStacks[0] = furnaceItemStacks[0].getItem().getContainerItem(furnaceItemStacks[0]);
                                }
                            }
                        }
                        // worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }

                    if (!isBurning() && heat > 0) {
                        heat -= Math.min(heat, 10);
                    }

                    if (isBurning() && heat < 1600) {
                        heat++;
                    }

                    if (isBurning() && canSmelt()) {
                        ++furnaceCookTime;

                        if (furnaceCookTime > 0) {
                            int i = 0;
                            //HEAT COMMENTED OUT
                            int maxSteamThisTick = (int) (((float) maxThisTick) * 0.7F + (maxThisTick * 0.3F * (
                              1600.0F / 1600.0F)));
//                            System.out.println("HEAT IS: " + heat + " MAX STEAM IS: " + maxSteamThisTick);
                            while (i < maxSteamThisTick && isBurning() && canSmelt()) {
                                insertSteam(10);
                                myTank.drain(2, true);
                                i++;
                            }
                            furnaceCookTime = 0;
                        }
                    } else {
                        furnaceCookTime = 0;
                    }
                }

                if (isBurning() != wasBurning) {
                    wasBurning = isBurning();
                    burning = isBurning();
                    markForUpdate();
                }
            }
        }

        if (furnaceBurnTime > 0) {
            furnaceBurnTime -= 1;
        }
    }

    private void insertSteam(int i) {
        SteamNetwork net = getNetwork();
        if (net != null) {
            net.addSteam(i);
        }

    }

    private boolean canSmelt() {
        if (isPrimary(worldObj, pos)) {
            return myTank.getFluidAmount() > 0;
        }
        if (getMyCorner() != BlockFlashBoiler.Corners.NONE && hasPrimary()) {
            TileEntityFlashBoiler primary = getPrimaryTileEntity();
            return primary != null && primary.canSmelt();
        }
        return false;
    }

    @Override
    public boolean isBurning() {
        if (isPrimary(worldObj, pos)) {
            return furnaceBurnTime > 0;
        } else {
            TileEntityFlashBoiler primary = getPrimaryTileEntity();
            return primary != null && primary.isBurning();
        }
    }

    private boolean hasPrimary() {
        return getPrimaryTileEntity() != null;
    }

    @Override
    public int getSizeInventory() {
        if (isPrimary(worldObj, pos)) {
            return furnaceItemStacks.length;
        } else {
            if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.getSizeInventory();
                }
            }
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (isPrimary(worldObj, pos)) {
            return furnaceItemStacks[slot];
        } else {
            if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.getStackInSlot(slot);
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (isPrimary(worldObj, pos)) {
            if (furnaceItemStacks[index] != null) {
                ItemStack itemstack;
                if (furnaceItemStacks[index].stackSize <= count) {
                    itemstack = furnaceItemStacks[index];
                    furnaceItemStacks[index] = null;
                } else {
                    itemstack = furnaceItemStacks[index].splitStack(count);
                    if (furnaceItemStacks[index].stackSize == 0) {
                        furnaceItemStacks[index] = null;
                    }
                }
                return itemstack;
            }
        } else {
            if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.decrStackSize(index, count);
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        if (isPrimary(worldObj, pos)) {
            ItemStack itemstack = null;
            if (furnaceItemStacks[slot] != null) {
                itemstack = furnaceItemStacks[slot];
                furnaceItemStacks[slot] = null;
            }
            return itemstack;
        } else {
            if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.removeStackFromSlot(slot);
                }
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (isPrimary(worldObj, pos)) {
            furnaceItemStacks[index] = stack;

            int stackLimit = getInventoryStackLimit();
            if (stack != null && stack.stackSize > stackLimit) {
                stack.stackSize = stackLimit;
            }
        } else {
            if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    primary.setInventorySlotContents(index, stack);
                }
            }
        }
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : "container.furnace";
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
    	return slot == 0 ? getItemBurnTime(stack) > 0 : FluidHelper.itemStackIsWaterContainer(stack);
    }

    @Override
    public float getPressure() {
        if (getMyCorner() != BlockFlashBoiler.Corners.NONE && (worldObj.isRemote || getNetwork() != null)) {
            return super.getPressure();
        }
        return 0F;
    }

    @Override
    public int getCapacity() {
        return getMyCorner() == BlockFlashBoiler.Corners.NONE ? 0 : capacity;
    }

    @Override
    public int getSteamShare() {
        return getMyCorner() == BlockFlashBoiler.Corners.NONE ? 0 : super.getSteamShare();
    }

    @Override
    public int getSteam() {
        int steamOut = super.getSteam();
        log.debug("Getting FB steam: " + steamOut);
        return steamOut;
    }

    @Override
    public void insertSteam(int amount, EnumFacing face) {
        SteamNetwork net = getNetwork();
        if (isPrimary(worldObj, pos) && net != null) {
            net.addSteam(amount);
        } else if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
            TileEntityFlashBoiler primary = getPrimaryTileEntity();
            if (primary != null) {
                primary.insertSteam(amount, face);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBurnTimeRemainingScaled(int scale) {
        if (isPrimary(worldObj, pos)) {
            if (currentItemBurnTime == 0) {
                currentItemBurnTime = 200;
            }

            return furnaceBurnTime * scale / currentItemBurnTime;
        } else {
            if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.getBurnTimeRemainingScaled(scale);
                }
            }
        }
        return 0;
    }

    @Override
    public void decrSteam(int i) {
        SteamNetwork net = getNetwork();
        if (isPrimary(worldObj, pos) && net != null) {
            net.decrSteam(i);
        } else if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
            TileEntityFlashBoiler primary = getPrimaryTileEntity();
            if (primary != null) {
                primary.decrSteam(i);
            }
        }
    }

    @Override
    public boolean doesConnect(EnumFacing face) {
        return getMyCorner().getVertical() == BlockFlashBoiler.Corners.Vertical.TOP;
    }

    @Override
    public boolean acceptsGauge(EnumFacing face) {
        BlockFlashBoiler.Corners corner = getMyCorner();
        if (face != EnumFacing.UP && corner != BlockFlashBoiler.Corners.NONE) {
            if (corner.getVertical() == BlockFlashBoiler.Corners.Vertical.TOP && face != EnumFacing.UP) {
                return true;
            } else if (face != myDir()) {
                return true;
            }
        }
        return false;
    }

    private EnumFacing myDir() {
        int front = frontSide;
        switch (front) {
            case 2: {
                return EnumFacing.NORTH;
            }
            case 3: {
                return EnumFacing.SOUTH;
            }
            case 4: {
                return EnumFacing.WEST;
            }
            case 5: {
                return EnumFacing.EAST;
            }
        }
        return EnumFacing.NORTH;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing face) {
        if (face == EnumFacing.UP) {
            return SLOTS_TOP;
        } else if (face == EnumFacing.DOWN) {
            return SLOTS_BOTTOM;
        }
        return SLOTS_SIDES;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
        int[] accessibleSlots = getSlotsForFace(side);
        boolean isAccessibleSlot = false;
        for (int accessibleSlot : accessibleSlots) {
            if (accessibleSlot == slot) {
                isAccessibleSlot = true;
            }
        }
        return getMyCorner() != BlockFlashBoiler.Corners.NONE && isItemValidForSlot(slot, stack) && isAccessibleSlot;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
        if (isPrimary(worldObj, pos)) {
            return stack.getItem() == Items.BUCKET;
        } else {
            if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.canExtractItem(slot, stack, side);
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getCookProgressScaled(int scale) {
        if (isPrimary(worldObj, pos)) {
            return furnaceCookTime * scale / 200;
        } else if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
            TileEntityFlashBoiler primary = getPrimaryTileEntity();
            if (primary != null) {
                return primary.getCookProgressScaled(scale);
            }
        }
        return 0;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (isPrimary(worldObj, pos)) {
            return myTank.fill(resource, doFill);
        } else if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
            TileEntityFlashBoiler primary = getPrimaryTileEntity();
            if (primary != null) {
                return primary.fill(from, resource, doFill);
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid == FluidRegistry.WATER;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (isPrimary(worldObj, pos)) {
            return new FluidTankInfo[] { new FluidTankInfo(myTank) };
        } else {
            if (getMyCorner() != BlockFlashBoiler.Corners.NONE) {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.getTankInfo(from);
                }
            }
        }
        return new FluidTankInfo[] { new FluidTankInfo(new FluidTank(0)) };
    }

    @Override
    public void explode() {
        TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) worldObj.getTileEntity(pos);
        if (boiler != null) {
            int clusterIndex = boiler.getValidClusterFromMetadata();
            if (clusterIndex >= 0) {
                int[][] cluster = (boiler.getClusterCoords(boiler.getValidClusterFromMetadata()));
                for (int[] aCluster : cluster) {
                    int x = aCluster[0], y = aCluster[1], z = aCluster[2];
                    BlockPos blockPos = new BlockPos(x, y, z);
                    if (!WorldHelper.areBlockPosEqual(blockPos, this.pos)) {
                        TileEntityFlashBoiler otherBoiler = (TileEntityFlashBoiler) worldObj.getTileEntity(blockPos);
                        if (otherBoiler != null) {
                            otherBoiler.secondaryExplosion();
                        }
                    }
                }
            }

        }
        super.explode();
    }

    @Override
    public HashSet<EnumFacing> getConnectionSides() {
        BlockFlashBoiler.Corners corner = getMyCorner();
        HashSet<EnumFacing> sides = new HashSet<>();
        if (corner != BlockFlashBoiler.Corners.NONE) {
            if (corner.getVertical() == BlockFlashBoiler.Corners.Vertical.TOP) {
                Collections.addAll(sides, EnumFacing.VALUES);
            } else {
                sides.add(EnumFacing.UP);
            }
        }
        return sides;
    }

    public void secondaryExplosion() {
        shouldExplode = true;
    }

    public boolean getBurning() {
        BlockFlashBoiler.Corners corner = getMyCorner();
        if (corner != BlockFlashBoiler.Corners.NONE) {
            if (isPrimary(worldObj, pos)) {
                return burning;
            } else {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.isBurning();
                }
            }
        }
        return false;
    }

    @Override
    public FluidTank getTank() {
        BlockFlashBoiler.Corners corner = getMyCorner();
        if (corner != BlockFlashBoiler.Corners.NONE) {
            if (isPrimary(worldObj, pos)) {
                return myTank;
            } else {
                TileEntityFlashBoiler primary = getPrimaryTileEntity();
                if (primary != null) {
                    return primary.getTank();
                }
            }
        }
        return null;
    }
}