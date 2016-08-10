package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.IDisguisableBlock;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.misc.FluidHelper;
import flaxbeard.steamcraft.misc.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBoiler extends SteamTransporterTileEntity implements ISidedInventory, ISteamTransporter, IWrenchable, IDisguisableBlock {
    private static final int[] slotsTop = new int[]{0, 1};
    private static final int[] slotsBottom = new int[]{0, 1};
    private static final int[] slotsSides = new int[]{0, 1};
    public FluidTank myTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 0), 10000);
    public int furnaceCookTime;
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public Block disguiseBlock = null;
    public int disguiseMeta = 0;
    private ItemStack[] furnaceItemStacks = new ItemStack[2];
    private String customName;
    private boolean wasBurning;

    public TileEntityBoiler(int capacity) {
        super(capacity, new EnumFacing[] { EnumFacing.UP });
        addSideToGaugeBlacklist(EnumFacing.UP);
        setPressureResistance(0.5F);
    }

    public TileEntityBoiler() {
        this(50000);
    }

    public static int getItemBurnTime(ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        Item item = stack.getItem();

        if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
            Block block = Block.getBlockFromItem(item);

            if (OreDictHelper.slabWoods.contains(item)) {
                return 150;
            }

            if (block.getDefaultState().getMaterial() == Material.WOOD) {
                return 300;
            }

            if (OreDictHelper.blockCoals.contains(item)) {
                return 16000;
            }
        }

        if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) {
            return 200;
        }
        if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) {
            return 200;
        }
        if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD")) {
            return 200;
        }
        if (OreDictHelper.arrayHasItem(OreDictHelper.sticks, item)) {
            return 100;
        }
        if (item == Items.COAL) {
            return 1600;
        }
        if (item == Items.LAVA_BUCKET) {
            return 20000;
        }
        if (OreDictHelper.saplings.contains(item)) {
            return 100;
        }
        if (item == Items.BLAZE_ROD) {
            return 2400;
        }
        return GameRegistry.getFuelValue(stack);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();
        access.setInteger("water", myTank.getFluidAmount());
        access.setShort("BurnTime", (short) furnaceBurnTime);
        access.setShort("CookTime", (short) furnaceCookTime);
        access.setShort("cIBT", (short) currentItemBurnTime);
        access.setInteger("disguiseBlock", Block.getIdFromBlock(disguiseBlock));
        access.setInteger("disguiseMeta", disguiseMeta);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        myTank.setFluid(new FluidStack(FluidRegistry.WATER, access.getInteger("water")));
        furnaceBurnTime = access.getShort("BurnTime");
        currentItemBurnTime = access.getShort("cIBT");
        furnaceCookTime = access.getShort("CookTime");
        disguiseBlock = Block.getBlockById(access.getInteger("disguiseBlock"));
        disguiseMeta = access.getInteger("disguiseMeta");
        super.markForResync();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList nbttaglist = (NBTTagList) nbt.getTag("Items");
        furnaceItemStacks = new ItemStack[2];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < furnaceItemStacks.length) {
                furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
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
        disguiseBlock = Block.getBlockById(nbt.getInteger("disguiseBlock"));
        disguiseMeta = nbt.getInteger("disguiseMeta");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("BurnTime", (short) furnaceBurnTime);
        nbt.setShort("water", (short) myTank.getFluidAmount());
        nbt.setShort("CookTime", (short) furnaceCookTime);
        nbt.setShort("cIBT", (short) currentItemBurnTime);
        nbt.setInteger("disguiseBlock", Block.getIdFromBlock(disguiseBlock));
        nbt.setInteger("disguiseMeta", disguiseMeta);

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
    public void update() {
        super.update();
        /*
        if (worldObj.isRemote) {
            boolean hasWrench = BlockSteamPipeRenderer.updateWrenchStatus();
            if (hasWrench != lastWrench && !(disguiseBlock == null || disguiseBlock == Blocks.AIR)) {
                super.markForResync();
            }
            lastWrench = hasWrench;
        }
        */

        ItemStack stackInInput = getStackInSlot(1);
	    if (FluidHelper.itemStackIsWaterContainer(stackInInput)) {
    		ItemStack drainedItemStack = FluidHelper.fillTankFromItem(stackInInput, myTank);
    		setInventorySlotContents(1, drainedItemStack);
        }

        boolean isBurnTimeGreaterThanZero = furnaceBurnTime > 0;
        if (furnaceBurnTime > 0) {
            //maxThisTick = Math.min(furnaceBurnTime, 10);
            furnaceBurnTime -= 1; //maxThisTick
        }


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

                //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }

            if (isBurning() && canSmelt() && getNetwork() != null) {
                ++furnaceCookTime;

                if (furnaceCookTime > 0) {
                    //int i = 0;
                    //	while (i<maxThisTick && isBurning() && canSmelt()) {
                    getNetwork().addSteam(10);
                    myTank.drain(2, true);
                    ///i++;
                    //}
                    furnaceCookTime = 0;
                }
            } else {
                furnaceCookTime = 0;
            }

            if (isBurnTimeGreaterThanZero != furnaceBurnTime > 0) {
                super.markForResync();
            }
        }

        if (wasBurning != isBurning()) {
            wasBurning = isBurning();
            markForResync();
        }
    }

    private boolean canSmelt() {
        return myTank.getFluidAmount() > 9;
    }

    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) myTank;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getSizeInventory() {
        return furnaceItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return furnaceItemStacks[slot];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        if (furnaceItemStacks[par1] != null) {
            ItemStack itemstack;

            if (furnaceItemStacks[par1].stackSize <= par2) {
                itemstack = furnaceItemStacks[par1];
                furnaceItemStacks[par1] = null;
                return itemstack;
            } else {
                itemstack = furnaceItemStacks[par1].splitStack(par2);

                if (furnaceItemStacks[par1].stackSize == 0) {
                    furnaceItemStacks[par1] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        if (furnaceItemStacks[slot] != null) {
            ItemStack itemstack = furnaceItemStacks[slot];
            furnaceItemStacks[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit()) {
            par2ItemStack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : "container.furnace";
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    @Override
    public ITextComponent getDisplayName() {
        String name = getName();
        return (hasCustomName() ? new TextComponentString(name) : new TextComponentTranslation(name));
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos) == this && player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    public int getPressureAsInt() {
        return (int) Math.floor((double) getPressure() * 1000);
    }

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
    	return slot == 0 ? getItemBurnTime(stack) > 0 : FluidHelper.itemStackIsWaterContainer(stack);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return slotsBottom;
        } else {
            return side == EnumFacing.UP ? slotsTop : slotsSides;
        }
    }

    @Override
    public boolean canInsertItem(int par1, ItemStack par2ItemStack, EnumFacing dir) {
        return isItemValidForSlot(par1, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int par1, ItemStack par2ItemStack, EnumFacing dir) {
        return par2ItemStack.getItem() == Items.BUCKET;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int p_145953_1_) {
        return furnaceCookTime * p_145953_1_ / 200;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int p_145955_1_) {
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = 200;
        }

        return furnaceBurnTime * p_145955_1_ / currentItemBurnTime;
    }

    public FluidTank getTank() {
        return myTank;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (disguiseBlock != null) {
                if (!player.capabilities.isCreativeMode) {
                    EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(disguiseBlock, 1, disguiseMeta));
                    world.spawnEntityInWorld(entityItem);
                }
                SoundType sound = disguiseBlock.getSoundType();
                world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F),
                  (double) ((float) pos.getZ() + 0.5F), sound.getBreakSound(), SoundCategory.BLOCKS,
                  (sound.getVolume() + 1F) / 2F, sound.getPitch() * 0.8F, false);
                disguiseBlock = null;

                super.markForResync();
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public Block getDisguiseBlock() {
        return disguiseBlock;
    }

    @Override
    public void setDisguiseBlock(Block block) {
        disguiseBlock = block;
    }

    @Override
    public int getDisguiseMeta() {
        return disguiseMeta;
    }

    @Override
    public void setDisguiseMeta(int meta) {
        disguiseMeta = meta;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: {
                return furnaceBurnTime;
            }
            case 1: {
                return currentItemBurnTime;
            }
            case 2: {
                return furnaceCookTime;
            }
            case 3: {
                return currentItemBurnTime;
            }
            default: {
                return 0;
            }
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: {
                furnaceBurnTime = value;
                break;
            }
            case 1: {
                currentItemBurnTime = value;
                break;
            }
            case 2: {
                furnaceCookTime = value;
                break;
            }
            case 3: {
                currentItemBurnTime = value;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        for (int i = 0; i < furnaceItemStacks.length; i++) {
            furnaceItemStacks[i] = null;
        }
    }
}
