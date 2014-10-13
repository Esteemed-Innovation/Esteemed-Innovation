package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.IEngineerable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEngineeringTable extends TileEntity implements IInventory {

    private ItemStack[] furnaceItemStacks = new ItemStack[1];


    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Items");
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(0);
        byte b0 = nbttagcompound1.getByte("Slot");

        if (b0 >= 0 && b0 < this.furnaceItemStacks.length) {
            this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        if (this.furnaceItemStacks[0] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte) 0);
            this.furnaceItemStacks[0].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }


        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

    @Override
    public int getSizeInventory() {
        return 10;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        if (var1 == 0) {
            return furnaceItemStacks[var1];
        } else {
            if (this.getStackInSlot(0) != null) {
                if (this.getStackInSlot(0).getItem() instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) this.getStackInSlot(0).getItem();
                    return item.getStackInSlot(this.getStackInSlot(0), var1);
                }
            }
            return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (var1 == 0) {
            if (this.furnaceItemStacks[var1] != null) {
                ItemStack itemstack;
                if (this.furnaceItemStacks[var1].stackSize <= var2) {
                    itemstack = this.furnaceItemStacks[var1];
                    this.furnaceItemStacks[var1] = null;
                    return itemstack;
                } else {
                    itemstack = this.furnaceItemStacks[var1].splitStack(var2);

                    if (this.furnaceItemStacks[var1].stackSize == 0) {
                        this.furnaceItemStacks[var1] = null;
                    }
                    return itemstack;
                }
            } else {
                return null;
            }
        } else {
            if (this.getStackInSlot(0) != null) {
                if (this.getStackInSlot(0).getItem() instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) this.getStackInSlot(0).getItem();
                    return item.decrStackSize(this.getStackInSlot(0), var1, var2);
                }
            }
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (var1 == 0) {
            return furnaceItemStacks[var1];
        } else {
            if (this.getStackInSlot(0) != null) {
                if (this.getStackInSlot(0).getItem() instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) this.getStackInSlot(0).getItem();
                    return item.getStackInSlot(this.getStackInSlot(0), var1);
                }
            }
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        if (var1 == 0) {
            furnaceItemStacks[var1] = var2;
        } else {
            if (this.getStackInSlot(0) != null) {
                if (this.getStackInSlot(0).getItem() instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) this.getStackInSlot(0).getItem();
                    item.setInventorySlotContents(this.getStackInSlot(0), var1, var2);
                }
            }
        }
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        if (var1 == 0) {
            return true;
        } else {
            if (this.getStackInSlot(0) != null) {
                if (this.getStackInSlot(0).getItem() instanceof IEngineerable) {
                    IEngineerable item = (IEngineerable) this.getStackInSlot(0).getItem();
                    return item.isItemValidForSlot(this.getStackInSlot(0), var1, var2);
                }
            }
            return false;
        }
    }

}
