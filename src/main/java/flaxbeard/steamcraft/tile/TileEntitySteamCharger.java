package flaxbeard.steamcraft.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;

public class TileEntitySteamCharger extends TileEntity implements ISteamTransporter,IInventory {
	
	private int steam = 0;
	private ItemStack[] inventory = new ItemStack[1];
	public int randomDegrees;
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.steam = par1NBTTagCompound.getShort("steam");
        randomDegrees = (int)(Math.random()*360);
        if (par1NBTTagCompound.hasKey("inventory"))
        {
        	 this.inventory[0] = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("inventory"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("steam",(short) this.steam);
        if (this.inventory[0] != null)
        {
	        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	        this.inventory[0].writeToNBT(nbttagcompound1);
	        par1NBTTagCompound.setTag("inventory", nbttagcompound1);
        }
    }
	
	@Override
	public Packet getDescriptionPacket()
	{
    	super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("steam", steam);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.steam = access.getInteger("steam");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	
	
	@Override
	public void updateEntity() {
		ForgeDirection[] dirs = { ForgeDirection.DOWN };
		UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,dirs);
		UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
		if (!this.worldObj.isAirBlock(xCoord, yCoord+1, zCoord) && this.getStackInSlot(0) != null) {
			if (!this.worldObj.isRemote) {
				this.dropItem(this.getStackInSlot(0));
			}
			this.setInventorySlotContents(0, null);
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if (this.getStackInSlot(0) != null) {
			ISteamChargable item = (ISteamChargable) this.getStackInSlot(0).getItem();
			ItemStack stack = this.getStackInSlot(0).copy();
			if (this.steam > 0 && stack.getItemDamage() > 0) {
				this.worldObj.spawnParticle("smoke", xCoord+0.5F, yCoord+0.5F, zCoord+0.5F, (Math.random()-0.5F)/12.0F, 0.0F, (Math.random()-0.5F)/12.0F);
			}
			if (this.steam > item.steamPerDurability() && stack.getItemDamage() > 0) {
 				int i = 0;
 				while (i<9 && (this.steam > item.steamPerDurability() && stack.getItemDamage() > 0)) {
 					this.steam -= item.steamPerDurability();
 					stack.setItemDamage(stack.getItemDamage()-1);
 	 				this.setInventorySlotContents(0, stack);
 					i++;
 				}
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}
	
	@Override
	public float getPressure() {
		return this.steam/1000.0F;
	}

	@Override
	public boolean canInsert(ForgeDirection face) {
		return face == ForgeDirection.DOWN;
	}

	@Override
	public int getCapacity() {
		return 1000;
	}

	@Override
	public int getSteam() {
		return this.steam;
	}

	@Override
	public void insertSteam(int amount, ForgeDirection face) {
		this.steam+=amount;
	}

	@Override
	public void decrSteam(int i) {
		this.steam -= i;
	}
	
	@Override
	public boolean doesConnect(ForgeDirection face) {
		return face == ForgeDirection.DOWN;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	public void dropItem(ItemStack item) {
		EntityItem entityItem = new EntityItem(this.worldObj, this.xCoord+0.5F, this.yCoord + 1.25F, this.zCoord+0.5F, item);
		this.worldObj.spawnEntityInWorld(entityItem);
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return this.inventory[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (this.inventory[var1] != null)
	    {
	        ItemStack itemstack;
	
	        if (this.inventory[var1].stackSize <= var2)
	        {
	            itemstack = this.inventory[var1];
	            this.inventory[var1] = null;
	            return itemstack;
	        }
	        else
	        {
	            itemstack = this.inventory[var1].splitStack(var2);
	
	            if (this.inventory[var1].stackSize == 0)
	            {
	                this.inventory[var1] = null;
	            }
	
	            return itemstack;
	        }
	    }
	    else
	    {
	        return null;
	    }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return this.inventory[var1];
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		this.inventory[var1] = var2;
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
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return var2.getItem() instanceof ISteamChargable;
	}
	
	@Override
	public boolean acceptsGauge(ForgeDirection face) {
		return face != ForgeDirection.UP && face != ForgeDirection.DOWN;
	}

}
