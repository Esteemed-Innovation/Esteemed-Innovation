package eiteam.esteemedinnovation.boiler;

import eiteam.esteemedinnovation.api.block.DisguisableBlock;
import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.api.util.FluidHelper;
import eiteam.esteemedinnovation.api.wrench.Wrenchable;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class TileEntityBoiler extends SteamTransporterTileEntity implements ISidedInventory, Wrenchable, DisguisableBlock {
    private static final int[] slotsTop = {0, 1};
    private static final int[] slotsBottom = {0, 1};
    private static final int[] slotsSides = {0, 1};
    private FluidTank myTank = new FluidTank(new FluidStack(FluidHelper.getWaterFluid(), 0), 10000);
    public int cookTime;
    public int burnTime;
    public int currentItemBurnTime;
    private Block disguiseBlock;
    private int disguiseMeta;
    @Nonnull
    private NonNullList<ItemStack> itemContents = NonNullList.withSize(2, ItemStack.EMPTY);
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

    public static int getItemBurnTime(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) {
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

        if (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) {
            return 200;
        }
        if (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName())) {
            return 200;
        }
        if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName())) {
            return 200;
        }
        if (OreDictHelper.listHasItem(OreDictHelper.sticks, item)) {
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
        NBTTagCompound access = getUpdateTag();
        access.setInteger("WaterStored", myTank.getFluidAmount());
        access.setShort("BurnTime", (short) burnTime);
        access.setShort("CookTime", (short) cookTime);
        access.setShort("CurrentItemBurnTime", (short) currentItemBurnTime);
        access.setInteger("DisguiseBlock", Block.getIdFromBlock(disguiseBlock));
        access.setInteger("DisguiseMetadata", disguiseMeta);

        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        myTank.setFluid(new FluidStack(FluidHelper.getWaterFluid(), access.getInteger("WaterStored")));
        burnTime = access.getShort("BurnTime");
        currentItemBurnTime = access.getShort("CurrentItemBurnTime");
        cookTime = access.getShort("CookTime");
        disguiseBlock = Block.getBlockById(access.getInteger("DisguiseBlock"));
        disguiseMeta = access.getInteger("DisguiseMetadata");
        markForResync();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        ItemStackHelper.loadAllItems(nbt, itemContents);

        burnTime = nbt.getShort("BurnTime");
        cookTime = nbt.getShort("CookTime");
        currentItemBurnTime = nbt.getShort("CurrentItemBurnTime");

        if (nbt.hasKey("CustomName")) {
            customName = nbt.getString("CustomName");
        }

        if (nbt.hasKey("WaterStored")) {
            myTank.setFluid(new FluidStack(FluidHelper.getWaterFluid(), nbt.getShort("WaterStored")));
        }
        disguiseBlock = Block.getBlockById(nbt.getInteger("DisguiseBlock"));
        disguiseMeta = nbt.getInteger("DisguiseMetadata");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("BurnTime", (short) burnTime);
        nbt.setShort("WaterStored", (short) myTank.getFluidAmount());
        nbt.setShort("CookTime", (short) cookTime);
        nbt.setShort("CurrentItemBurnTime", (short) currentItemBurnTime);
        nbt.setInteger("DisguiseBlock", Block.getIdFromBlock(disguiseBlock));
        nbt.setInteger("DisguiseMetadata", disguiseMeta);

        ItemStackHelper.saveAllItems(nbt, itemContents);

        if (hasCustomName()) {
            nbt.setString("CustomName", customName);
        }

        return nbt;
    }

    @Override
    public boolean canUpdate(IBlockState target) {
        return target.getBlock() == BoilerModule.BOILER;
    }

    @Override
    public void safeUpdate() {
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
            ItemStack drainedItemStack = FluidHelper.fillTankFromItem(stackInInput, myTank, true);
            setInventorySlotContents(1, drainedItemStack);
        }

        boolean isBurnTimeGreaterThanZero = burnTime > 0;
        if (burnTime > 0) {
            //maxThisTick = Math.min(burnTime, 10);
            burnTime -= 1; //maxThisTick
        }


        if (!world.isRemote) {
            if (burnTime == 0 && canSmelt()) {
                ItemStack stack = itemContents.get(0);
                currentItemBurnTime = burnTime = getItemBurnTime(stack);

                if (burnTime > 0) {
                    ItemStack container = stack.getItem().getContainerItem(stack);
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        itemContents.set(0, container);
                    }
                }

                //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }

            if (isBurning() && canSmelt() && getNetwork() != null) {
                ++cookTime;

                if (cookTime > 0) {
                    //int i = 0;
                    //while (i<maxThisTick && isBurning() && canSmelt()) {
                    getNetwork().addSteam(10);
                    myTank.drain(2, true);
                    ///i++;
                    //}
                    cookTime = 0;
                }
            } else {
                cookTime = 0;
            }

            if (isBurnTimeGreaterThanZero != burnTime > 0) {
                markForResync();
            }
        }

        if (wasBurning != isBurning()) {
            wasBurning = isBurning();
            markForResync();
        }

        super.safeUpdate();
    }

    private boolean canSmelt() {
        return myTank.getFluidAmount() > 9;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) myTank;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getSizeInventory() {
        return itemContents.size();
    }

    @Override
    public boolean isEmpty() {
        // TODO: Rewrite this entirely
        int nonnulls = 0;
        for (ItemStack stack : itemContents) {
            if (stack != null) {
                nonnulls++;
            }
        }
        return nonnulls == 0;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemContents.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(itemContents, index, count);
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return ItemStackHelper.getAndRemove(itemContents, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
        if (stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        itemContents.set(slot, stack);
    }

    @Nonnull
    @Override
    public String getName() {
        return hasCustomName() ? customName : "container.furnace";
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    @Nonnull
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
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {}

    public int getPressureAsInt() {
        return (int) Math.floor((double) getPressure() * 1000);
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        return slot == 0 ? getItemBurnTime(stack) > 0 : FluidHelper.itemStackIsWaterContainer(stack);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return slotsBottom;
        } else {
            return side == EnumFacing.UP ? slotsTop : slotsSides;
        }
    }

    @Override
    public boolean canInsertItem(int par1, @Nonnull ItemStack par2ItemStack, @Nonnull EnumFacing dir) {
        return isItemValidForSlot(par1, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int par1, @Nonnull ItemStack par2ItemStack, @Nonnull EnumFacing dir) {
        return par2ItemStack.getItem() == Items.BUCKET;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int pixels) {
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = 200;
        }

        return burnTime * pixels / currentItemBurnTime;
    }

    public FluidTank getTank() {
        return myTank;
    }

    @Override
    public boolean onWrench(@Nonnull ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            Block disguiseBlock = getDisguiseBlock();
            if (disguiseBlock == null) {
                return false;
            }
            if (!player.capabilities.isCreativeMode) {
                EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(disguiseBlock, 1, getDisguiseMeta()));
                world.spawnEntity(entityItem);
            }
            SoundType sound = disguiseBlock.getSoundType(null, world, pos, player);
            world.playSound((pos.getX() + 0.5F), (pos.getY() + 0.5F),
              (pos.getZ() + 0.5F), sound.getBreakSound(), SoundCategory.BLOCKS,
              (sound.getVolume() + 1F) / 2F, sound.getPitch() * 0.8F, false);
            setDisguiseBlock(null);

            markForResync();
        }
        return true;
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
                return burnTime;
            }
            case 1: {
                return currentItemBurnTime;
            }
            case 2: {
                return cookTime;
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
                burnTime = value;
                break;
            }
            case 1: {
                currentItemBurnTime = value;
                break;
            }
            case 2: {
                cookTime = value;
                break;
            }
            case 3: {
                currentItemBurnTime = value;
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        itemContents.clear();
    }
}
