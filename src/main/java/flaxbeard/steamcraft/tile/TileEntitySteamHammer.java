package flaxbeard.steamcraft.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

public class TileEntitySteamHammer extends SteamTransporterTileEntity implements IInventory,ISteamTransporter {
	public int hammerTicks = 0;
	private boolean isInitialized = false;
	private boolean isWorking = false;
	private boolean hadItem = false;
	private ItemStack[] inventory = new ItemStack[3];
	public String itemName = "";
	public int cost = 0;
	public int progress = 0;
	
	public TileEntitySteamHammer(){
		super(ForgeDirection.VALID_DIRECTIONS);
		this.addSidesToGaugeBlacklist(ForgeDirection.VALID_DIRECTIONS);
	}
	
	@Override
	public void updateEntity() {
		ForgeDirection dir = myDir();
		if (!isInitialized){
			
			ForgeDirection[] dirs = { dir.getOpposite() };
			this.setDistributionDirections(dirs);
			this.isInitialized = true;
		}
		super.updateEntity();
		if (worldObj.isRemote){
			if (this.isWorking){
				if (cost > 0 && progress < cost && hammerTicks == 355){
					progress++;
				}
				hammerTicks = (hammerTicks+5)%360;
				if (hammerTicks == 20) {
					for (int i = 0; i<5; i++) {
						Steamcraft.instance.proxy.spawnBreakParticles(worldObj, xCoord+0.5F+0.25F*dir.offsetX, yCoord, zCoord+0.5F+0.25F*dir.offsetZ, Blocks.anvil, (float)(Math.random()-0.5F)/12.0F, 0.0F, (float)(Math.random()-0.5F)/12.0F);
					}
				}
			} else {
				hammerTicks = 0;
				progress = 0;
			}
		} else {
			if (this.getStackInSlot(0) != null){
				if (!this.hadItem){
					this.hadItem = true;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			} else if (this.hadItem){
				this.hadItem = false;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			if (cost > 0 && progress < cost && hammerTicks == 355) {
				progress++;
			}
			if (cost > 0 && progress < cost && this.getStackInSlot(2) != null) {
				if (hammerTicks == 0) {
					if (this.getSteam() > 400) {
						this.decrSteam(400);
						if (!this.isWorking){
							this.isWorking = true;
							worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						}
						
					}
					else
					{
						if (this.isWorking){
							this.isWorking = false;
							worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
						}
						return;
					}
				}
				hammerTicks = (hammerTicks+5)%360;
				if (hammerTicks == 15) {
					this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "random.anvil_land", 0.2F, (float) (0.75F+(Math.random()*0.1F)));
				}
				
				if (hammerTicks == 40) {
					this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "random.anvil_land", 0.075F, (float) (0.75F+(Math.random()*0.1F)));
				}
				if (hammerTicks == 170) {
					this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:hiss", Block.soundTypeAnvil.getVolume(), 0.9F);
				}
			}
			else
			{
				if (this.isWorking){
					this.isWorking = false;
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
				if (hammerTicks > 0) {
					hammerTicks = 0;
				}
			}
		}
		
		//this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

	}
	
	private ForgeDirection myDir(){
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		if (meta == 0) {
			dir = ForgeDirection.SOUTH;
		}
		if (meta == 1) {
			dir = ForgeDirection.WEST;
		}
		if (meta == 2) {
			dir = ForgeDirection.NORTH;
		}
		if (meta == 3) {
			dir = ForgeDirection.EAST;
		}
		return dir;
	}
	
//	@Override
//	public void markDirty()
//	{
//		super.markDirty();
//		ContainerSteamAnvil.this.onCraftMatrixChanged(this);
//	}

	@Override
	public int getSizeInventory() {
        return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) {
        return this.inventory[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
	 if (this.inventory[par1] != null)
	    {
	        ItemStack itemstack;
	
	        if (this.inventory[par1].stackSize <= par2)
	        {
	            itemstack = this.inventory[par1];
	            this.inventory[par1] = null;
	            return itemstack;
	        }
	        else
	        {
	            itemstack = this.inventory[par1].splitStack(par2);
	
	            if (this.inventory[par1].stackSize == 0)
	            {
	                this.inventory[par1] = null;
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
	public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.inventory[par1] != null)
        {
            ItemStack itemstack = this.inventory[par1];
            this.inventory[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        this.inventory[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
	}

	@Override
    public String getInventoryName()
    {
        return "";
    }

	@Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		  return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
	

	@Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }
	
	@Override
	public Packet getDescriptionPacket()
	{
        NBTTagCompound access = super.getDescriptionTag();

        access.setInteger("cost", cost);
        access.setInteger("progress",progress);
        access.setInteger("hammerTicks",hammerTicks);
        access.setBoolean("isWorking", this.isWorking);
        
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.cost = access.getInteger("cost");
    	this.isWorking = access.getBoolean("isWorking");
    	this.progress = access.getInteger("progress");
    	this.hammerTicks = access.getInteger("hammerTicks");
    	
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.length)
            {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

    	this.cost = par1NBTTagCompound.getInteger("cost");
    	this.progress = par1NBTTagCompound.getInteger("progress");
    	this.hammerTicks = par1NBTTagCompound.getInteger("hammerTicks");
        
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("cost", cost);
        par1NBTTagCompound.setInteger("progress",progress);
        par1NBTTagCompound.setInteger("hammerTicks",hammerTicks);
        
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);

    }

}
