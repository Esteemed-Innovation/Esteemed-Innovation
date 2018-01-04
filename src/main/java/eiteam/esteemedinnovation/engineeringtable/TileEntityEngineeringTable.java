package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.api.Engineerable;
import eiteam.esteemedinnovation.api.tile.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class TileEntityEngineeringTable extends TileEntityBase implements IInventory {
    private NonNullList<ItemStack> contents = NonNullList.withSize(1, ItemStack.EMPTY);
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        contents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        if (compound.hasKey("Items")) {
            NBTTagList nbttaglist = (NBTTagList) compound.getTag("Items");
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(0);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < contents.size()) {
                contents.set(b0, new ItemStack(nbttagcompound1));
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();

        if (!contents.get(0).isEmpty()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte) 0);
            contents.get(0).writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }

        compound.setTag("Items", nbttaglist);

        return compound;
    }

    @Override
    public int getSizeInventory() {
        return 10;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        if (index == 0) {
            return contents.get(0);
        } else {
            ItemStack stackInSlotZero = getStackInSlot(0);
            Item itemInSlotZero = stackInSlotZero.getItem();
            if (itemInSlotZero instanceof Engineerable) {
                Engineerable item = (Engineerable) itemInSlotZero;
                return item.getStackInSlot(stackInSlotZero, index);
            }
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index == 0) {
            return contents.get(0).isEmpty() ? ItemStack.EMPTY : contents.get(index).splitStack(count);
        } else {
            ItemStack stackInSlotZero = getStackInSlot(0);
            Item itemInSlotZero = stackInSlotZero.getItem();
            if (itemInSlotZero instanceof Engineerable) {
                Engineerable item = (Engineerable) itemInSlotZero;
                return item.decrStackSize(stackInSlotZero, index, count);
            }
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        // For some reason these two methods did exactly the same thing.
        return getStackInSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        if (index == 0) {
            contents.set(index, stack);
        } else {
            ItemStack stackInSlotZero = getStackInSlot(0);
            Item itemInSlotZero = stackInSlotZero.getItem();
            if (itemInSlotZero instanceof Engineerable) {
                Engineerable item = (Engineerable) itemInSlotZero;
                item.setInventorySlotContents(stackInSlotZero, index, stack);
            }
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {}

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        if (index == 0) {
            return true;
        } else {
            ItemStack stackInSlotZero = getStackInSlot(0);
            Item itemInSlotZero = stackInSlotZero.getItem();
            if (itemInSlotZero instanceof Engineerable) {
                Engineerable item = (Engineerable) itemInSlotZero;
                return item.isItemValidForSlot(stackInSlotZero, index, stack);
            }
            return false;
        }
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("");
    }

    @Nonnull
    @Override
    public String getName() {
        return "";
    }

    @Override
    public void clear() {
        contents.clear();
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty() || contents.stream().allMatch(ItemStack::isEmpty);
    }
}
