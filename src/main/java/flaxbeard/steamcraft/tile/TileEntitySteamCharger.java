package flaxbeard.steamcraft.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntitySteamCharger extends SteamTransporterTileEntity implements ISteamTransporter,IInventory {
	
	private boolean isCharging = false;
	private boolean hadItem = false;
	
	private ItemStack[] inventory = new ItemStack[1];
	public int randomDegrees;
	
	public TileEntitySteamCharger(){
		super(new ForgeDirection[]{ForgeDirection.DOWN});
		this.addSidesToGaugeBlacklist(new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.DOWN});
	}
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
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
        NBTTagCompound access = super.getDescriptionTag();

        if (this.inventory[0] != null)
        {
	        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	        this.inventory[0].writeToNBT(nbttagcompound1);
	        access.setTag("inventory", nbttagcompound1);
        }
        access.setBoolean("isCharging", this.isCharging);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	if (access.hasKey("inventory"))
        {
        	 this.inventory[0] = ItemStack.loadItemStackFromNBT(access.getCompoundTag("inventory"));
        }
        else
        {
        	this.inventory[0] = null;
        }
    	this.isCharging = access.getBoolean("isCharging");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.worldObj.isRemote){
			if (this.getStackInSlot(0) != null) {
				ISteamChargable item = (ISteamChargable) this.getStackInSlot(0).getItem();
				ItemStack stack = this.getStackInSlot(0).copy();
				if (this.isCharging) {
					this.worldObj.spawnParticle("smoke", xCoord+0.5F, yCoord+0.5F, zCoord+0.5F, (Math.random()-0.5F)/12.0F, 0.0F, (Math.random()-0.5F)/12.0F);
				}
			}
		} else {
			if (this.getStackInSlot(0) != null) {
				if (!this.hadItem){
					this.hadItem = true;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
				ISteamChargable item = (ISteamChargable) this.getStackInSlot(0).getItem();
				ItemStack stack = this.getStackInSlot(0).copy();
				if (this.getSteam() > 0 && stack.getItemDamage() > 0) {
					if (!this.isCharging){
						System.out.println("Charging");
						this.isCharging = true;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
				} else {
					if (this.isCharging){
						System.out.println("Not charging");
						this.isCharging = false;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
				}
				if (this.getSteam() > item.steamPerDurability() && stack.getItemDamage() > 0) {
	 				int i = 0;
	 				while (i<9 && (this.getSteam() > item.steamPerDurability() && stack.getItemDamage() > 0)) {
	 					this.decrSteam(item.steamPerDurability());
	 					stack.setItemDamage(stack.getItemDamage()-1);
	 	 				this.setInventorySlotContents(0, stack);
	 					i++;
	 				}
				}
			} else {
				if (this.hadItem){
					System.out.println("No item");
					this.hadItem = false;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
			//this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		
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

}
