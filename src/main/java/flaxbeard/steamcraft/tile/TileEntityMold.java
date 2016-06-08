package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.ICrucibleMold;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

public class TileEntityMold extends TileEntity implements ISidedInventory, ITickable {
    private static final int[] moldSlots = new int[] {0};
    public boolean isOpen = true;
    public ItemStack mold;
    public int changeTicks = 0;
    private ItemStack inventory;

    @Override
    public Packet getDescriptionPacket() {
        super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setBoolean("isOpen", isOpen);
        access.setInteger("changeTicks", changeTicks);
        NBTTagCompound nbttagcompound1;


        if (mold != null) {
            nbttagcompound1 = new NBTTagCompound();
            mold.writeToNBT(nbttagcompound1);
            access.setTag("mold", nbttagcompound1);
        }

        if (inventory != null) {
            nbttagcompound1 = new NBTTagCompound();
            inventory.writeToNBT(nbttagcompound1);
            access.setTag("inventory", nbttagcompound1);
        }
        return new SPacketUpdateTileEntity(pos, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        isOpen = access.getBoolean("isOpen");
        if (changeTicks == 0) {
            changeTicks = access.getInteger("changeTicks");
        }

        if (access.hasKey("inventory")) {
            inventory = ItemStack.loadItemStackFromNBT(access.getCompoundTag("inventory"));
        }

        mold = access.hasKey("mold") ? ItemStack.loadItemStackFromNBT(access.getCompoundTag("mold")) : null;
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        isOpen = nbt.getBoolean("isOpen");

        if (nbt.hasKey("inventory")) {
            inventory = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("inventory"));
        }

        mold = nbt.hasKey("mold") ? ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("mold")) : null;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("isOpen", isOpen);

        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        if (mold != null) {
            mold.writeToNBT(nbttagcompound1);
            nbt.setTag("mold", nbttagcompound1);
        }

        if (inventory != null) {
            inventory.writeToNBT(nbttagcompound1);
            nbt.setTag("inventory", nbttagcompound1);
        }
    }

    public boolean canPour() {
        return !isOpen && mold != null && inventory == null;
    }

    public void pour(CrucibleLiquid liquid) {
        inventory = ((ICrucibleMold) mold.getItem()).getItemFromLiquid(liquid);
        inventory.stackSize = 1;
        markDirty();
    }

    public void dropItem(ItemStack item) {
        EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5F, pos.getY() + 1.5F, pos.getZ() + 0.5F, item);
        worldObj.spawnEntityInWorld(entityItem);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return inventory;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        markDirty();
        if (inventory.stackSize <= amt) {
            ItemStack itemstack = inventory;
            inventory = null;
            return itemstack;
        }
        ItemStack itemstack = inventory.splitStack(amt);
        if (inventory.stackSize == 0) {
            inventory = null;
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        if (inventory != null) {
            ItemStack itemstack = inventory;
            inventory = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory = stack;
    }

    @Override
    public String getName() {
        return "container.mold";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.isOpen;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return changeTicks;
    }

    @Override
    public void setField(int id, int value) {
        changeTicks = value;
    }

    @Override
    public int getFieldCount() {
        return 1;
    }

    @Override
    public void clear() {
        inventory = null;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.UP) {
            return moldSlots;
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, EnumFacing side) {
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
        return true;
    }

    @Override
    public void update() {
        if (changeTicks > 0) {
            changeTicks--;
        }
        if (isOpen && inventory != null && changeTicks < 10) {
            if (!worldObj.isRemote) {
                dropItem(inventory);
            }
            inventory = null;
        }
    }
}
