package flaxbeard.steamcraft.tile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.block.BlockBoiler;

public class TileEntityBoiler extends SteamTransporterTileEntity implements IFluidHandler,ISidedInventory,ISteamTransporter {
	public FluidTank myTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 1),10000);
	private ItemStack[] furnaceItemStacks = new ItemStack[2];
    private String field_145958_o;
	public int furnaceCookTime;
	public int furnaceBurnTime;
	public int currentItemBurnTime;
    private static final int[] slotsTop = new int[] {0, 1};
    private static final int[] slotsBottom = new int[] {0, 1};
    private static final int[] slotsSides = new int[] {0, 1};
    
    public TileEntityBoiler(){
    	super(5000, new ForgeDirection[]{ForgeDirection.UP});
    	this.addSideToGaugeBlacklist(ForgeDirection.UP);
    	this.setPressureResistance(0.5F);
    }
    
	@Override
	public Packet getDescriptionPacket()
	{
        NBTTagCompound access = super.getDescriptionTag();
        access.setInteger("water",myTank.getFluidAmount());
        access.setShort("BurnTime", (short)this.furnaceBurnTime);
        access.setShort("CookTime", (short)this.furnaceCookTime);
        access.setShort("cIBT", (short)this.currentItemBurnTime);
        

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.myTank.setFluid(new FluidStack(FluidRegistry.WATER,access.getInteger("water")));
    	this.furnaceBurnTime = access.getShort("BurnTime");
    	this.currentItemBurnTime = access.getShort("cIBT");
      	this.furnaceCookTime = access.getShort("CookTime");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Items");
        this.furnaceItemStacks = new ItemStack[2];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.furnaceItemStacks.length)
            {
                this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.furnaceBurnTime = par1NBTTagCompound.getShort("BurnTime");
        this.furnaceCookTime = par1NBTTagCompound.getShort("CookTime");
        this.currentItemBurnTime = par1NBTTagCompound.getShort("cIBT");

        if (par1NBTTagCompound.hasKey("CustomName"))
        {
            this.field_145958_o = par1NBTTagCompound.getString("CustomName");
        }
        
        if (par1NBTTagCompound.hasKey("water"))
        {
        	this.myTank.setFluid(new FluidStack(FluidRegistry.WATER,par1NBTTagCompound.getShort("water")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
        par1NBTTagCompound.setShort("water",(short) myTank.getFluidAmount());
        par1NBTTagCompound.setShort("CookTime", (short)this.furnaceCookTime);
        par1NBTTagCompound.setShort("cIBT", (short)this.currentItemBurnTime);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; ++i)
        {
            if (this.furnaceItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName())
        {
            par1NBTTagCompound.setString("CustomName", this.field_145958_o);
        }
    }
    
    public void superUpdateOnly(){
    	super.updateEntity();
    }
    
    @Override
    public void updateEntity() {
    	super.updateEntity();
    	if (this.getStackInSlot(1) != null) {
	    	if (this.getStackInSlot(1).getItem() == Items.water_bucket || (this.getStackInSlot(1).getItem() instanceof IFluidContainerItem && ((IFluidContainerItem)this.getStackInSlot(1).getItem()).getFluid(this.getStackInSlot(1)) != null && ((IFluidContainerItem)this.getStackInSlot(1).getItem()).getFluid(this.getStackInSlot(1)).getFluid() == FluidRegistry.WATER)) {
	    		if (canDrainItem(this.getStackInSlot(1))) {
	    			if (this.getStackInSlot(1).getItem() == Items.water_bucket) {
	    				this.setInventorySlotContents(1, new ItemStack(Items.bucket));
	    				this.myTank.fill(new FluidStack(FluidRegistry.WATER, 1000),true);
	    			}
	    			if (this.getStackInSlot(1).getItem() instanceof IFluidContainerItem) {
	    				int maxDrain = this.getTankInfo(ForgeDirection.UP)[0].capacity - this.getTankInfo(ForgeDirection.UP)[0].fluid.amount;
	    				this.myTank.fill(new FluidStack(FluidRegistry.WATER, ((IFluidContainerItem)this.getStackInSlot(1).getItem()).drain(this.getStackInSlot(1), maxDrain, true).amount),true);
	    			}
	    		}
	    	}
    	}
    	
    	boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;
        int maxThisTick = 10;
        if (this.furnaceBurnTime > 0)
        {
        	//maxThisTick = Math.min(furnaceBurnTime, 10);
            this.furnaceBurnTime -= 1; //maxThisTick

        }
        

        if (!this.worldObj.isRemote)
        {  	
            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
                this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[0]);

                if (this.furnaceBurnTime > 0)
                {

                    flag1 = true;

                    if (this.furnaceItemStacks[0] != null)
                    {
                        --this.furnaceItemStacks[0].stackSize;

                        if (this.furnaceItemStacks[0].stackSize == 0)
                        {
                            this.furnaceItemStacks[0] = furnaceItemStacks[0].getItem().getContainerItem(furnaceItemStacks[0]);
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt() && this.getNetwork() != null)
            {
                ++this.furnaceCookTime;

                if (this.furnaceCookTime > 0)
                {
                	//int i = 0;
                //	while (i<maxThisTick && this.isBurning() && this.canSmelt()) {
                		this.getNetwork().addSteam(1);
                		this.myTank.drain(2, true);
                		///i++;
                	//}
            		this.furnaceCookTime = 0;

                    flag1 = true;
                }
            }
            else
            {
                this.furnaceCookTime = 0;
            }

            if (flag != this.furnaceBurnTime > 0)
            {
                flag1 = true;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
       }
    }
    
    private boolean canSmelt() {
		return myTank.getFluidAmount() > 9;
	}

	public boolean isBurning()
    {
		return this.furnaceBurnTime > 0;
    }
    
    private boolean canDrainItem(ItemStack stack) {
    	return stack.stackSize == 1;
    }

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return myTank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid == FluidRegistry.WATER;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { new FluidTankInfo(myTank) };
	}

	@Override
	public int getSizeInventory() {
        return this.furnaceItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) {
        return this.furnaceItemStacks[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
	 if (this.furnaceItemStacks[par1] != null)
	    {
	        ItemStack itemstack;
	
	        if (this.furnaceItemStacks[par1].stackSize <= par2)
	        {
	            itemstack = this.furnaceItemStacks[par1];
	            this.furnaceItemStacks[par1] = null;
	            return itemstack;
	        }
	        else
	        {
	            itemstack = this.furnaceItemStacks[par1].splitStack(par2);
	
	            if (this.furnaceItemStacks[par1].stackSize == 0)
	            {
	                this.furnaceItemStacks[par1] = null;
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
        if (this.furnaceItemStacks[par1] != null)
        {
            ItemStack itemstack = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        this.furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
	}

	@Override
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.field_145958_o : "container.furnace";
    }

	@Override
    public boolean hasCustomInventoryName()
    {
        return this.field_145958_o != null && this.field_145958_o.length() > 0;
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
	
	public int getPressureAsInt(){
		return (int)Math.floor((double)this.getPressure()*1000);
	}

	@Override
	public void closeInventory() {}
	
    public static int getItemBurnTime(ItemStack p_145952_0_)
    {
        if (p_145952_0_ == null)
        {
            return 0;
        }
        else
        {
            Item item = p_145952_0_.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(p_145952_0_);
        }
    }

	@Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return par1 == 0 ? getItemBurnTime(par2ItemStack) > 0 : (par2ItemStack.getItem() == Items.water_bucket || FluidContainerRegistry.isEmptyContainer(par2ItemStack) || par2ItemStack.getItem()  instanceof IFluidContainerItem);
    }

    public int[] getAccessibleSlotsFromSide(int par1)
    {
        return par1 == 0 ? slotsBottom : (par1 == 1 ? slotsTop : slotsSides);
    }


	@Override
    public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return this.isItemValidForSlot(par1, par2ItemStack);
    }

	@Override
    public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return par2ItemStack.getItem() == Items.bucket;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int p_145953_1_)
    {
        return this.furnaceCookTime * p_145953_1_ / 200;
    }

	@SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int p_145955_1_)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * p_145955_1_ / this.currentItemBurnTime;
    }
	
	public FluidTank getTank(){
		return myTank;
	}

}
