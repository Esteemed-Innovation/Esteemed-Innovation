package flaxbeard.steamcraft.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.SteamcraftRegistry;

public class TileEntityMold extends TileEntity implements ISidedInventory {
	public boolean open = true;
	private ItemStack[] inventory = new ItemStack[1];
	public ItemStack[] mold = new ItemStack[1];;
	public int changeTicks = 0;
	public boolean needsToUpdate=true;
	
	
	@Override
	public Packet getDescriptionPacket()
	{
    	super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setBoolean("open", this.open);
        access.setInteger("changeTicks",this.changeTicks);
        NBTTagCompound nbttagcompound1;
        

		if (this.mold[0] != null)
        {
	        nbttagcompound1 = new NBTTagCompound();
	        this.mold[0].writeToNBT(nbttagcompound1);
	        access.setTag("mold", nbttagcompound1);
        }

        if (this.inventory[0] != null)
        {
	        nbttagcompound1 = new NBTTagCompound();
	        this.inventory[0].writeToNBT(nbttagcompound1);
	        access.setTag("inventory", nbttagcompound1);
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
        this.open = access.getBoolean("open");
        if (this.changeTicks == 0) {
        	this.changeTicks = access.getInteger("changeTicks");
        }

        if (access.hasKey("inventory"))
        {
        	 this.inventory[0] = ItemStack.loadItemStackFromNBT(access.getCompoundTag("inventory"));
        }

        if (access.hasKey("mold"))
        {

        	 this.mold[0] = ItemStack.loadItemStackFromNBT(access.getCompoundTag("mold"));
        }
        else
        {
        	this.mold[0] = null;
        }
        

    }
	

    
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

        	 this.mold[0] = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("mold"));
        }
        else
        {
        	this.mold[0] = null;
        }
        //System.out.println(this.worldObj == null);
        //this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("open", this.open);
        

        NBTTagCompound nbttagcompound1;
		if (this.mold[0] != null)
        {
	        nbttagcompound1 = new NBTTagCompound();
	        this.mold[0].writeToNBT(nbttagcompound1);
	        par1NBTTagCompound.setTag("mold", nbttagcompound1);
        }

        if (this.inventory[0] != null)
        {
	        nbttagcompound1 = new NBTTagCompound();
	        this.inventory[0].writeToNBT(nbttagcompound1);
	        par1NBTTagCompound.setTag("inventory", nbttagcompound1);
        }
    }
	
	public boolean canPour() {
		return (!this.open) && mold[0] != null && inventory[0] == null;
	}
	
	public void pour(CrucibleLiquid liquid) {
		this.inventory[0] = ((ICrucibleMold)mold[0].getItem()).getItemFromLiquid(liquid);
		this.inventory[0].stackSize = 1;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

	}

}
