package flaxbeard.steamcraft.tile;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.misc.FluidHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.HashSet;

public class TileEntityFlashBoiler extends TileEntityBoiler implements IFluidHandler, ISidedInventory, ISteamTransporter {

    private static final int[] slotsTop = new int[]{0, 1};
    private static final int[] slotsBottom = new int[]{0, 1};
    private static final int[] slotsSides = new int[]{0, 1};
    private static final int CAPACITY = 12500;
    // ====================================================
    //          All the possible configurations
    // ====================================================
    // bottom   top
    // OO       OO     Z ^
    // XO       OO     X-->
    private static int[][] bbl = new int[][]{
            new int[]{0, 0, 0}, new int[]{1, 0, 0}, new int[]{0, 0, 1}, new int[]{1, 0, 1},
            new int[]{0, 1, 0}, new int[]{1, 1, 0}, new int[]{0, 1, 1}, new int[]{1, 1, 1}
    };
    // bottom   top
    // OO       OO     Z ^
    // OO       XO     X-->
    private static int[][] tbl = new int[][]{
            new int[]{0, -1, 0}, new int[]{1, -1, 0}, new int[]{0, -1, 1}, new int[]{1, -1, 1},
            new int[]{0, 0, 0}, new int[]{1, 0, 0}, new int[]{0, 0, 1}, new int[]{1, 0, 1}
    };
    // bottom   top
    // OO       OO     Z ^
    // OX       OO     X-->
    private static int[][] bbr = new int[][]{
            new int[]{-1, 0, 0}, new int[]{0, 0, 0}, new int[]{-1, 0, 1}, new int[]{0, 0, 1},
            new int[]{-1, 1, 0}, new int[]{0, 1, 0}, new int[]{-1, 1, 1}, new int[]{0, 1, 1}
    };
    // bottom   top
    // OO       OO     Z ^
    // OO       OX     X-->
    private static int[][] tbr = new int[][]{
            new int[]{-1, -1, 0}, new int[]{0, -1, 0}, new int[]{-1, -1, 1}, new int[]{0, -1, 1},
            new int[]{-1, 0, 0}, new int[]{0, 0, 0}, new int[]{-1, 0, 1}, new int[]{0, 0, 1}
    };
    // bottom   top
    // XO       OO     Z ^
    // OO       OO     X-->
    private static int[][] btl = new int[][]{
            new int[]{0, 0, -1}, new int[]{1, 0, -1}, new int[]{0, 0, 0}, new int[]{1, 0, 0},
            new int[]{0, 1, -1}, new int[]{1, 1, -1}, new int[]{0, 1, 0}, new int[]{1, 1, 0}
    };
    // bottom   top
    // OO       XO     Z ^
    // OO       OO     X-->
    private static int[][] ttl = new int[][]{
            new int[]{0, -1, -1}, new int[]{1, -1, -1}, new int[]{0, -1, 0}, new int[]{1, -1, 0},
            new int[]{0, 0, -1}, new int[]{1, 0, -1}, new int[]{0, 0, 0}, new int[]{1, 0, 0}
    };
    // bottom   top
    // OX       OO     Z ^
    // OO       OO     X-->
    private static int[][] btr = new int[][]{
            new int[]{-1, 0, -1}, new int[]{0, 0, -1}, new int[]{-1, 0, 0}, new int[]{0, 0, 0},
            new int[]{-1, 1, -1}, new int[]{0, 1, -1}, new int[]{-1, 1, 0}, new int[]{0, 1, 0}
    };
    // bottom   top
    // OO       OX     Z ^
    // OO       OO     X-->
    private static int[][] ttr = new int[][]{
            new int[]{-1, -1, -1}, new int[]{0, -1, -1}, new int[]{-1, -1, 0}, new int[]{0, -1, 0},
            new int[]{-1, 0, -1}, new int[]{0, 0, -1}, new int[]{-1, 0, 0}, new int[]{0, 0, 0}
    };
    private static int[][][] validConfigs = new int[][][]{
            bbl, tbl, bbr, tbr, btl, ttl, btr, ttr
    };
    public int furnaceCookTime;
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public int heat;
    private ItemStack[] furnaceItemStacks = new ItemStack[2];
    private String field145958O;
    private boolean wasBurning = false;
    private boolean shouldExplode = false;
    private boolean waitOneTick = true;
    private int frontSide = -1;
    private boolean loaded = false;
    private boolean burning;

    public TileEntityFlashBoiler() {
        super(CAPACITY);
        super.myTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 1), 80000);
        this.setPressureResistance(0.5F);
    }

    public static int getItemBurnTime(ItemStack stack) {
        if (stack == null) {
            return 0;
        } else {
            Item item = stack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab) {
                    return 150;
                }

                if (block.getMaterial() == Material.wood) {
                    return 300;
                }

                if (block == Blocks.coal_block) {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(stack);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound access) {
        super.readFromNBT(access);
        this.frontSide = access.getInteger("frontSide");
        NBTTagList nbttaglist = (NBTTagList) access.getTag("Items");
        this.furnaceItemStacks = new ItemStack[2];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound compound = nbttaglist.getCompoundTagAt(i);
            byte b0 = compound.getByte("Slot");

            if (b0 >= 0 && b0 < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(compound);
            }
        }

        this.furnaceBurnTime = access.getShort("BurnTime");
        this.furnaceCookTime = access.getShort("CookTime");
        this.currentItemBurnTime = access.getShort("cIBT");

        if (access.hasKey("CustomName")) {
            this.field145958O = access.getString("CustomName");
        }

        if (access.hasKey("water")) {
            this.myTank.setFluid(new FluidStack(FluidRegistry.WATER, access.getShort("water")));
        }

        if (access.hasKey("heat")) {
            this.heat = access.getShort("heat");
        }
        // worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

    }

    @Override
    public void writeToNBT(NBTTagCompound access) {
        super.writeToNBT(access);
        access.setInteger("frontSide", this.frontSide);
        access.setShort("BurnTime", (short) this.furnaceBurnTime);
        access.setShort("water", (short) myTank.getFluidAmount());
        access.setShort("heat", (short) this.heat);

        access.setShort("CookTime", (short) this.furnaceCookTime);
        access.setShort("cIBT", (short) this.currentItemBurnTime);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
            if (this.furnaceItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        access.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName()) {
            access.setString("CustomName", this.field145958O);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound access = super.getDescriptionTag();
        access.setInteger("frontSide", this.frontSide);
        access.setInteger("water", myTank.getFluidAmount());
        access.setShort("BurnTime", (short) this.furnaceBurnTime);
        access.setShort("CookTime", (short) this.furnaceCookTime);
        access.setShort("cIBT", (short) this.currentItemBurnTime);
        access.setBoolean("burning", burning);


        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.func_148857_g();
        this.frontSide = access.getInteger("frontSide");
        this.myTank.setFluid(new FluidStack(FluidRegistry.WATER, access.getInteger("water")));
        this.furnaceBurnTime = access.getShort("BurnTime");
        this.currentItemBurnTime = access.getShort("cIBT");
        this.furnaceCookTime = access.getShort("CookTime");
        this.burning = access.getBoolean("burning");

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void checkMultiblock(boolean isBreaking, int frontSide) {
        ////Steamcraft.log.debug(frontSideIn);
        int x = xCoord, y = yCoord, z = zCoord;

        ////Steamcraft.log.debug("Checking multiblock; frontSide: "+this.frontSide);

        boolean isMultiblock = false;
        boolean isTooManyBlocks = false;


        if (!worldObj.isRemote) {
            if (!isBreaking) {
                ////Steamcraft.log.debug(this.frontSide);
                int[] validClusters = getValidClusters();
                ////Steamcraft.log.debug("valid configs found: " +validClusters.length);

                if (validClusters.length == 1) {
                    updateMultiblock(validClusters[0], true, frontSide);
                }
            }
        }
    }

    public void destroyMultiblock() {
        updateMultiblock(this.getValidClusterFromMetadata(), false, -1);
    }

    private int getValidClusterFromMetadata() {
        int validCluster = -1;
        // Because the clusters at the top are doofy and not in the right order =\
        switch (worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
            case 1:
                validCluster = 0;
                break;
            case 2:
                validCluster = 2;
                break;
            case 3:
                validCluster = 4;
                break;
            case 4:
                validCluster = 6;
                break;
            case 5:
                validCluster = 1;
                break;
            case 6:
                validCluster = 3;
                break;
            case 7:
                validCluster = 5;
                break;
            case 8:
                validCluster = 7;
                break;
        }

        return validCluster;
    }

    private int checkCluster(int[][] cluster) {
        int count = 0;
        for (int pos = 0; pos < 8; pos++) {
            int x = cluster[pos][0] + xCoord, y = cluster[pos][1] + yCoord, z = cluster[pos][2] + zCoord;
            Block b = worldObj.getBlock(x, y, z);
            if (b == SteamcraftBlocks.flashBoiler) {
                TileEntityFlashBoiler fb = (TileEntityFlashBoiler) worldObj.getTileEntity(x, y, z);
                if (!(worldObj.getBlockMetadata(x, y, z) > 0)) {
                    count++;
                }

            }

        }

        return count;
    }

    private int[] getValidClusters() {
        int[] valid = new int[8];
        int[] out;
        int count = 0;
        for (int clusterIndex = 0; clusterIndex < 8; clusterIndex++) {
            ////Steamcraft.log.debug("Checking cluster "+clusterIndex);
            boolean isValid = false;
            if (checkCluster(validConfigs[clusterIndex]) == 8) {
                valid[count] = clusterIndex;
                count++;
                isValid = true;
            }
        }
        out = new int[count];
        for (int i = 0; i < count; i++) {
            out[i] = valid[i];
        }
        return out;
    }

    private int[][] getClusterCoords(int clusterIndex) {
        int[][] cluster = validConfigs[clusterIndex];
        int[][] out = new int[8][3];
        for (int pos = 0; pos < 8; pos++) {
            out[pos] = new int[]{cluster[pos][0] + xCoord, cluster[pos][1] + yCoord, cluster[pos][2] + zCoord};
        }
        return out;
    }

    private void updateMultiblock(int clusterIndex, boolean isMultiblock, int frontSide) {
        int[][] cluster = getClusterCoords(clusterIndex);
        HashSet<TileEntityFlashBoiler> boilers = new HashSet();
        for (int pos = 7; pos >= 0; pos--) {
            int x = cluster[pos][0], y = cluster[pos][1], z = cluster[pos][2];
            if (worldObj.getBlock(x, y, z) == SteamcraftBlocks.flashBoiler) {
                worldObj.setBlockMetadataWithNotify(
                        cluster[pos][0], cluster[pos][1], cluster[pos][2],
                        isMultiblock ? pos + 1 : 0,
                        2
                );
                TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) worldObj.getTileEntity(cluster[pos][0], cluster[pos][1], cluster[pos][2]);
                boiler.setFront(frontSide, false);
                boilers.add(boiler);

            } else {
                ////Steamcraft.log.debug("ERROR! ("+x+","+y+","+z+") is not a flashBoiler!");
            }

        }
        for (TileEntityFlashBoiler boiler : boilers) {
            if (isMultiblock) {
                SteamNetwork.newOrJoin(boiler);
            } else {
                if (this.getNetwork() != null) {
                    this.getNetwork().split(boiler, true);
                }
            }
        }
    }

    public TileEntityFlashBoiler getPrimaryTileEntity() {
        int[][] cluster = getClusterCoords(getValidClusterFromMetadata());
        int x = cluster[0][0], y = cluster[0][1], z = cluster[0][2];
        if (worldObj.getBlock(x, y, z) == SteamcraftBlocks.flashBoiler &&
          worldObj.getBlockMetadata(x, y, z) == 1) {
            return (TileEntityFlashBoiler) worldObj.getTileEntity(x, y, z);
        }

        return null;
    }

    public void setFront(int frontSide, boolean print) {
        //if (print) //Steamcraft.log.debug("Setting front side to "+frontSide);
        if (!worldObj.isRemote)
            this.frontSide = frontSide;
    }

    public int getFront() {
        return this.frontSide;
    }

    @Override
    public void updateEntity() {
        super.superUpdateOnly();
        // fixes existing capacity and prevents explosions
        if (this.capacity != CAPACITY) {
            int steamToLose = Math.abs(this.capacity - CAPACITY);
            this.decrSteam(steamToLose);
            this.capacity = CAPACITY;

        }
        if (this.shouldExplode) {
            this.getNetwork().split(this, true);
            worldObj.createExplosion(null, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 4.0F, true);
            return;
        }
        ////Steamcraft.log.debug(this.getFront());
       if (waitOneTick) {
           waitOneTick = false;
       } else {
            if (!worldObj.isRemote && worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) {
            	ItemStack stackInInput = this.getStackInSlot(1);
        		if (FluidHelper.itemStackIsWaterContainer(stackInInput)) {
            		ItemStack drainedItemStack = FluidHelper.fillTankFromItem(stackInInput, myTank);
            		setInventorySlotContents(1, drainedItemStack);
                }

                boolean flag = this.furnaceBurnTime > 0;
                boolean flag1 = false;
                int maxThisTick = 10;
//                if (this.furnaceBurnTime > 0) {
//                    maxThisTick = Math.min(furnaceBurnTime, 10);
//                    this.furnaceBurnTime -= maxThisTick;

//                }


                if (!this.worldObj.isRemote) {
                    if (this.furnaceBurnTime == 0 && this.canSmelt()) {
                        this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[0]);

                        if (this.furnaceBurnTime > 0) {

                            flag1 = true;

                            if (this.furnaceItemStacks[0] != null) {
                                --this.furnaceItemStacks[0].stackSize;

                                if (this.furnaceItemStacks[0].stackSize == 0) {
                                    this.furnaceItemStacks[0] = furnaceItemStacks[0].getItem().getContainerItem(furnaceItemStacks[0]);
                                }
                            }
                        }
                        // worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }

                    if (!this.isBurning() && this.heat > 0) {
                        this.heat -= Math.min(this.heat, 10);
                    }

                    if (this.isBurning() && this.heat < 1600) {
                        this.heat++;
                    }

                    if (this.isBurning() && this.canSmelt()) {
                        ++this.furnaceCookTime;

                        if (this.furnaceCookTime > 0) {
                            int i = 0;
                            //HEAT COMMENTED OUT
                            int maxSteamThisTick = (int) (((float) maxThisTick) * 0.7F + (maxThisTick * 0.3F * (
                              1600.0F / 1600.0F)));
//                            System.out.println("HEAT IS: " + heat + " MAX STEAM IS: " + maxSteamThisTick);
                            while (i < maxSteamThisTick && this.isBurning() && this.canSmelt()) {
                                this.insertSteam(10);
                                this.myTank.drain(2, true);
                                i++;
                            }
                            this.furnaceCookTime = 0;

                            flag1 = true;
                        }
                    } else {
                        this.furnaceCookTime = 0;
                    }

                    if (flag != this.furnaceBurnTime > 0) {
                        flag1 = true;
                        //BlockBoiler.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                    }
                }

                if (this.isBurning() != this.wasBurning) {
                    this.wasBurning = this.isBurning();
                    this.burning = this.isBurning();
                    this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
        }

        if (this.furnaceBurnTime > 0) {
            this.furnaceBurnTime -= 1;
        }

        if (!this.worldObj.isRemote) {
            ////Steamcraft.log.debug(this.furnaceBurnTime);
        }
        //this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void insertSteam(int i) {
        if (this.getNetwork() != null) {
            this.getNetwork().addSteam(i);
        }

    }

    private boolean canSmelt() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 ? myTank.getFluidAmount() > 9 : worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().canSmelt() : false;
    }

    private boolean canDrainItem(ItemStack stack) {
        return stack.stackSize == 1;
    }

    @Override
    public boolean isBurning() {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) {
            return this.furnaceBurnTime > 0;
        } else if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0) {
            if (getPrimaryTileEntity() != null) {
                return getPrimaryTileEntity().isBurning();
            } else return false;

        } else return false;

    }

    public boolean hasPrimary() {
        return getPrimaryTileEntity() != null;
    }

    @Override
    public int getSizeInventory() {

        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 ? this.furnaceItemStacks.length : (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().getSizeInventory() : 0);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 ? this.furnaceItemStacks[slot] : (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().getStackInSlot(slot) : null);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) {
            if (this.furnaceItemStacks[par1] != null) {
                ItemStack itemstack;

                if (this.furnaceItemStacks[par1].stackSize <= par2) {
                    itemstack = this.furnaceItemStacks[par1];
                    this.furnaceItemStacks[par1] = null;
                    return itemstack;
                } else {
                    itemstack = this.furnaceItemStacks[par1].splitStack(par2);

                    if (this.furnaceItemStacks[par1].stackSize == 0) {
                        this.furnaceItemStacks[par1] = null;
                    }

                    return itemstack;
                }
            } else {
                return null;
            }
        } else {
            return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().decrStackSize(par1, par2) : null;
        }

    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1) {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) {
            if (this.furnaceItemStacks[par1] != null) {
                ItemStack itemstack = this.furnaceItemStacks[par1];
                this.furnaceItemStacks[par1] = null;
                return itemstack;
            } else {
                return null;
            }
        } else {
            return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().getStackInSlotOnClosing(par1) : null;
        }

    }

    public boolean isInCluster(int x, int y, int z) {
        int[][] cluster = this.getClusterCoords(this.getValidClusterFromMetadata());
        for (int pos = 0; pos < cluster.length; pos++) {
            if (x == cluster[pos][0] && y == cluster[pos][1] && z == cluster[pos][1]) {
                return worldObj.getBlock(x, y, z) == SteamcraftBlocks.flashBoiler && worldObj.getBlockMetadata(x, y, z) > 0;
            }
        }
        return false;
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) {
            this.furnaceItemStacks[par1] = par2ItemStack;

            if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
                par2ItemStack.stackSize = this.getInventoryStackLimit();
            }
        } else if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0) {
            getPrimaryTileEntity().setInventorySlotContents(par1, par2ItemStack);
        }
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.field145958O : "container.furnace";
    }

    @Override
    public boolean hasCustomInventoryName() {

        return this.field145958O != null && this.field145958O.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
    	return slot == 0 ? getItemBurnTime(stack) > 0 : FluidHelper.itemStackIsWaterContainer(stack);
    }

    @Override
    public float getPressure() {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0) {

            if (worldObj.isRemote) {
                ////Steamcraft.log.debug("Returning "+this.pressure);
                return (pressure);
            } else {
                if (this.getNetwork() != null)
                    return (super.getPressure());
                else
                    return 0;
            }
        } else {
            return 0F;
        }

    }

    @Override
    public boolean canInsert(ForgeDirection face) {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 4 && face != myDir();
    }

    @Override
    public int getCapacity() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? this.capacity : 0;
    }

    @Override
    public int getSteamShare() {
        if (this.getBlockMetadata() > 0) {
            return super.getSteamShare();
        } else {
            return 0;
        }
        //if (this.getNetwork() != null)
        //	return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1 ? this.getNetwork().getSteam() : (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().getSteamShare() : 0);
        //else return 0;
    }

    @Override
    public int getSteam() {
        int steamOut = super.getSteam();
        log.debug("Getting FB steam: " + steamOut);
        return steamOut;
    }

    @Override
    public void insertSteam(int amount, ForgeDirection face) {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 && this.getNetwork() != null) {
            this.getNetwork().addSteam(amount);
        } else if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0) {
            getPrimaryTileEntity().insertSteam(amount, face);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBurnTimeRemainingScaled(int scale) {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) {
            if (this.currentItemBurnTime == 0) {
                this.currentItemBurnTime = 200;
            }

            return this.furnaceBurnTime * scale / this.currentItemBurnTime;
        } else {
            return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().getBurnTimeRemainingScaled(scale) : 0;
        }

    }

    @Override
    public void decrSteam(int i) {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 && this.getNetwork() != null) {
            this.getNetwork().decrSteam(i);
        } else if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0) {
            getPrimaryTileEntity().decrSteam(i);
        }

    }

    @Override
    public boolean doesConnect(ForgeDirection face) {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 4;
    }

    @Override
    public boolean acceptsGauge(ForgeDirection face) {
        if (face != ForgeDirection.UP && worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0) {
            if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 4 && face != ForgeDirection.UP) {
                return true;
            } else if (face != myDir()) {
                return true;
            }
        }
        return false;
    }

    public ForgeDirection myDir() {
        int meta = this.frontSide;
        switch (meta) {
            case 2:
                return ForgeDirection.NORTH;
            case 3:
                return ForgeDirection.SOUTH;
            case 4:
                return ForgeDirection.WEST;
            case 5:
                return ForgeDirection.EAST;
        }
        return ForgeDirection.NORTH;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        int[] accessibleSlots = getAccessibleSlotsFromSide(side);
        boolean isAccessibleSlot = false;
        for (int i = 0; i < accessibleSlots.length; i++) {
            if (accessibleSlots[i] == slot) isAccessibleSlot = true;
        }
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? this.isItemValidForSlot(slot, stack) && isAccessibleSlot : false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) {
            return stack.getItem() == Items.bucket;
        } else {
            return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().canExtractItem(slot, stack, side) : false;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getCookProgressScaled(int scale) {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 ? this.furnaceCookTime * scale / 200 : (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().getCookProgressScaled(scale) : 0);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        if (meta == 1) {
            ////Steamcraft.log.debug("Filling master");
            return myTank.fill(resource, doFill);
        } else if (meta > 0 && hasPrimary()) {
            ////Steamcraft.log.debug("Deferring fill to master");
            return getPrimaryTileEntity().fill(from, resource, doFill);
        } else {
            return 0;
        }
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid == FluidRegistry.WATER;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 ? new FluidTankInfo[]{new FluidTankInfo(myTank)} : worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 0 && hasPrimary() ? getPrimaryTileEntity().getTankInfo(from) : new FluidTankInfo[]{new FluidTankInfo(new FluidTank(0))};
    }

    @Override
    public void explode() {
        TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) worldObj.getTileEntity(xCoord, yCoord, zCoord);
        if (boiler != null) {
            int clusterIndex = boiler.getValidClusterFromMetadata();
            if (clusterIndex >= 0) {
                int[][] cluster = (boiler.getClusterCoords(boiler.getValidClusterFromMetadata()));
                for (int pos = 0; pos < cluster.length; pos++) {
                    int x = cluster[pos][0], y = cluster[pos][1], z = cluster[pos][2];
                    if (!(x == xCoord && y == yCoord && z == zCoord)) {
                        TileEntityFlashBoiler otherBoiler = (TileEntityFlashBoiler) worldObj.getTileEntity(x, y, z);
                        if (otherBoiler != null) otherBoiler.secondaryExplosion();
                    }
                }
            }

        }
        super.explode();


    }

    @Override
    public HashSet<ForgeDirection> getConnectionSides() {
        int meta = this.getBlockMetadata();
        HashSet<ForgeDirection> sides = new HashSet();
        if (meta > 0) {
            if (meta > 4) {
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    sides.add(side);
                }
            } else {
                sides.add(ForgeDirection.UP);
            }
        }
        return sides;
    }

    public void secondaryExplosion() {
        this.shouldExplode = true;
    }

    public boolean getBurning() {
        int meta = getBlockMetadata();
        if (meta > 0) {
            if (meta == 1) {
                return this.burning;
            } else {
                if (this.hasPrimary()) {
                    return this.getPrimaryTileEntity().isBurning();
                }
            }
        }
        return false;
    }

    @Override
    public FluidTank getTank() {
        ////Steamcraft.log.debug("Flash boiler tank get!");
        if (this.getBlockMetadata() > 0) {
            if (this.getBlockMetadata() == 1) {
                ////Steamcraft.log.debug("Master returning tank");
                ////Steamcraft.log.debug("Fill = "+myTank.getFluidAmount());
                return this.myTank;
            } else {
                if (this.hasPrimary()) {
                    ////Steamcraft.log.debug("Asking master to return tank");
                    return this.getPrimaryTileEntity().getTank();
                }
            }
        }
        return null;
    }
}