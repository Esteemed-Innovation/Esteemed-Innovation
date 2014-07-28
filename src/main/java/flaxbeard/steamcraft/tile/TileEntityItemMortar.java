package flaxbeard.steamcraft.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.entity.EntityMortarItem;

public class TileEntityItemMortar extends SteamTransporterTileEntity implements ISteamTransporter,IInventory {
	
	private ItemStack[] inventory = new ItemStack[1];
	public int xT;
	public int zT;
	public boolean hasTarget = false;
	public int fireTicks = 0;
	
	public TileEntityItemMortar(){
		super(new ForgeDirection[] { ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST });
		this.addSidesToGaugeBlacklist(ForgeDirection.VALID_DIRECTIONS);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.xT = par1NBTTagCompound.getShort("xT");
        this.zT = par1NBTTagCompound.getShort("zT");
        this.fireTicks = par1NBTTagCompound.getShort("fireTicks");

        this.hasTarget = par1NBTTagCompound.getBoolean("hasTarget");

        if (par1NBTTagCompound.hasKey("inventory"))
        {
        	 this.inventory[0] = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("inventory"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("xT",(short) this.xT);
        par1NBTTagCompound.setShort("zT",(short) this.zT);
        par1NBTTagCompound.setShort("fireTicks",(short) this.fireTicks);

        par1NBTTagCompound.setBoolean("hasTarget", hasTarget);

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

        access.setInteger("fireTicks", fireTicks);
        access.setInteger("xT", this.xT);
        access.setInteger("zT", this.zT);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.xT = access.getInteger("xT");
    	this.zT = access.getInteger("zT");
    	this.fireTicks = access.getInteger("fireTicks");
    }
	
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!this.worldObj.isRemote) {
			if ((this.getStackInSlot(0) != null && this.worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord)) || this.fireTicks >= 60) {
				ItemStack stack = null;
				if (this.fireTicks < 60) {
					stack = this.getStackInSlot(0).copy();
				}
				if (this.getSteam() > 200 && hasTarget) {
					if (fireTicks == 0) {
						this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
					this.fireTicks++;
					if (this.fireTicks == 10) {
						this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:hiss", Block.soundTypeAnvil.getVolume(), 0.9F);
					}
					if (this.fireTicks == 60) {
						this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "random.explode", 1.0F, 0.8F);
						this.decrSteam(200);
						ItemStack stack2 = stack.copy();
						stack2.stackSize = 1;
						EntityMortarItem entityItem = new EntityMortarItem(this.worldObj, this.xCoord+0.5F, this.yCoord + 1.25F, this.zCoord+0.5F, stack2, xT, zT);
						this.worldObj.spawnEntityInWorld(entityItem);
						entityItem.motionY = 1.0F;
						if (stack.stackSize > 1) {
							stack.stackSize--;
							this.setInventorySlotContents(0, stack);
						}
						else
						{
							this.setInventorySlotContents(0, null);
						}
					}
					if (this.fireTicks == 80) {
						this.fireTicks = 0;
					}
				}
				else
				{
					this.fireTicks = 0;
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
			else
			{
				this.fireTicks = 0;
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
		else
		{
			if (fireTicks > 0) {
				fireTicks++;
				if (fireTicks == 80) {
					fireTicks = 0;
				}
			}
		}
		//this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
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
		return true;
	}
}
