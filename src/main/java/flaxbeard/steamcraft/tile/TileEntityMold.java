package flaxbeard.steamcraft.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.ICrucibleMold;

public class TileEntityMold extends TileEntity implements ISidedInventory {
	public boolean open = true;
	public CrucibleLiquid myLiquid;
	private ItemStack[] inventory = new ItemStack[1];
	public ItemStack mold = null;
	public int changeTicks = 0;
	
  @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        
        this.open = par1NBTTagCompound.getBoolean("open");

        if (par1NBTTagCompound.hasKey("inventory"))
        {
        	 this.inventory[0] = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("inventory"));
        }
        
        if (par1NBTTagCompound.hasKey("mold"))
        {
        	 mold = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("mold"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        
        par1NBTTagCompound.setBoolean("open", this.open);
        
        if (mold != null)
        {
	        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	        mold.writeToNBT(nbttagcompound1);
	        par1NBTTagCompound.setTag("mold", nbttagcompound1);
        }

        if (this.inventory[0] != null)
        {
	        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	        this.inventory[0].writeToNBT(nbttagcompound1);
	        par1NBTTagCompound.setTag("inventory", nbttagcompound1);
        }
    }
	
	public boolean canPour() {
		return !this.open && myLiquid == null && mold != null && inventory[0] == null;
	}
	
	public void pour(CrucibleLiquid liquid) {
		this.inventory[0] = ((ICrucibleMold)mold.getItem()).getItemFromLiquid(liquid);
		this.inventory[0].stackSize = 1;
	}
	
	public void dropItem(ItemStack item) {
		EntityItem entityItem = new EntityItem(this.worldObj, this.xCoord+0.5F, this.yCoord + 1.5F, this.zCoord+0.5F, item);
		this.worldObj.spawnEntityInWorld(entityItem);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inventory[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (open) {
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			if (this.inventory[var1].stackSize <= var2) {
				ItemStack itemstack = this.inventory[var1];
				this.inventory[var1] = null;
		        return itemstack;
			}
			ItemStack itemstack = this.inventory[var1].splitStack(var2);
			if (this.inventory[var1].stackSize == 0) {
				this.inventory[var1] = null;
			}
			return itemstack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		if (this.inventory[var1] != null)
	    {
			ItemStack itemstack = this.inventory[var1];
			this.inventory[var1] = null;
			return itemstack;
	    }
	    return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
	}

	@Override
	public String getInventoryName() {
	    return "container.mold";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return this.open;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		return false;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3) {
		return this.open;
	}
	
	@Override
	public void updateEntity() {
		if (this.changeTicks > 0) {
			changeTicks--;
		}
		if (open &&  inventory[0] != null && changeTicks < 10) {
			if (!this.worldObj.isRemote ) {
				dropItem(inventory[0]);
			}
			this.inventory[0] = null;
		}
	}

}
