package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.api.IEngineerable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public class TileEntityEngineeringTable extends TileEntity implements IInventory {
    private ItemStack[] contents = new ItemStack[1];
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = (NBTTagList) compound.getTag("Items");
        contents = new ItemStack[getSizeInventory()];

        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(0);
        byte b0 = nbttagcompound1.getByte("Slot");

        if (b0 >= 0 && b0 < contents.length) {
            contents[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();

        if (contents[0] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte) 0);
            contents[0].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }

        compound.setTag("Items", nbttaglist);

        return compound;
    }

    @Override
    public int getSizeInventory() {
        return 10;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index == 0) {
            return contents[index];
        } else {
            ItemStack stackInSlotZero = getStackInSlot(0);
            if (stackInSlotZero != null) {
                Item itemInSlotZero = stackInSlotZero.getItem();
                if (itemInSlotZero instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) itemInSlotZero;
                    return item.getStackInSlot(stackInSlotZero, index);
                }
            }
            return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index == 0) {
            if (contents[index] != null) {
                ItemStack itemstack;
                if (contents[index].stackSize <= count) {
                    itemstack = contents[index];
                    contents[index] = null;
                    return itemstack;
                } else {
                    itemstack = contents[index].splitStack(count);

                    if (contents[index].stackSize == 0) {
                        contents[index] = null;
                    }
                    return itemstack;
                }
            } else {
                return null;
            }
        } else {
            ItemStack stackInSlotZero = getStackInSlot(0);
            if (stackInSlotZero != null) {
                Item itemInSlotZero = stackInSlotZero.getItem();
                if (itemInSlotZero instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) itemInSlotZero;
                    return item.decrStackSize(stackInSlotZero, index, count);
                }
            }
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        // For some reason these two methods did exactly the same thing.
        return getStackInSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) {
            contents[index] = stack;
        } else {
            ItemStack stackInSlotZero = getStackInSlot(0);
            if (stackInSlotZero != null) {
                Item itemInSlotZero = stackInSlotZero.getItem();
                if (itemInSlotZero instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) itemInSlotZero;
                    item.setInventorySlotContents(stackInSlotZero, index, stack);
                }
            }
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return true;
        } else {
            ItemStack stackInSlotZero = getStackInSlot(0);
            if (stackInSlotZero != null) {
                Item itemInSlotZero = stackInSlotZero.getItem();
                if (itemInSlotZero instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) itemInSlotZero;
                    return item.isItemValidForSlot(stackInSlotZero, index, stack);
                }
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

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < contents.length; i++) {
            contents[i] = null;
        }
    }
}
