package flaxbeard.steamcraft.tile;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;
import flaxbeard.steamcraft.block.BlockBoiler;

public class TileEntityFlashBoiler extends TileEntityBoiler implements IFluidHandler,ISidedInventory,ISteamTransporter{
	
	private FluidTank myTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 1),10000);
	public int steam;
    private ItemStack[] furnaceItemStacks = new ItemStack[2];
    private String field_145958_o;
	public int furnaceCookTime;
	public int furnaceBurnTime;
	public int currentItemBurnTime;
    private static final int[] slotsTop = new int[] {0, 1};
    private static final int[] slotsBottom = new int[] {0, 1};
    private static final int[] slotsSides = new int[] {0, 1};
    
    private boolean waitOneTick = true;
    
	private int frontSide = -1;
	
	private boolean loaded = false;
	
	// ====================================================
	//          All the possible configurations
	// ====================================================
	// bottom   top
	// OO       OO     Z ^
	// XO       OO     X--> 
	private static int[][] bbl = new int[][]{
		new int[]{0, 0, 0}, new int[]{1, 0, 0}, new int[]{0, 0, 1}, new int[]{1, 0, 1},
		new int[]{0, 1, 0}, new int[]{1, 1, 0}, new int[]{0, 1, 1}, new int[]{1, 1, 1}
	};
	
	// bottom   top
	// OO       OO     Z ^
	// OO       XO     X--> 
	private static int[][] tbl = new int[][]{
		new int[]{0, -1, 0}, new int[]{1, -1, 0}, new int[]{0, -1, 1}, new int[]{1, -1, 1},
		new int[]{0,  0, 0}, new int[]{1,  0, 0}, new int[]{0,  0, 1}, new int[]{1,  0, 1}
	};
	
	// bottom   top
	// OO       OO     Z ^
	// OX       OO     X--> 
	private static int [][]bbr = new int[][]{
		new int[]{-1, 0, 0}, new int[]{0, 0, 0}, new int[]{-1, 0, 1}, new int[]{0, 0, 1},
		new int[]{-1, 1, 0}, new int[]{0, 1, 0}, new int[]{-1, 1, 1}, new int[]{0, 1, 1}
	};
	
	// bottom   top
	// OO       OO     Z ^
	// OO       OX     X--> 
	private static int [][]tbr = new int[][]{
		new int[]{-1, -1, 0}, new int[]{0, -1, 0}, new int[]{-1, -1, 1}, new int[]{0, -1, 1},
		new int[]{-1,  0, 0}, new int[]{0,  0, 0}, new int[]{-1,  0, 1}, new int[]{0,  0, 1}
	};
	
	// bottom   top
	// XO       OO     Z ^
	// OO       OO     X--> 
	private static int [][]btl = new int[][]{
		new int[]{0, 0, -1}, new int[]{1, 0, -1}, new int[]{0, 0, 0}, new int[]{1, 0, 0},
		new int[]{0, 1, -1}, new int[]{1, 1, -1}, new int[]{0, 1, 0}, new int[]{1, 1, 0}
	};
	
	// bottom   top
	// OO       XO     Z ^
	// OO       OO     X--> 
	private static int [][]ttl = new int[][]{
		new int[]{0, -1, -1}, new int[]{1, -1, -1}, new int[]{0, -1, 0}, new int[]{1, -1, 0},
		new int[]{0,  0, -1}, new int[]{1,  0, -1}, new int[]{0,  0, 0}, new int[]{1,  0, 0}
	};
	
	// bottom   top
	// OX       OO     Z ^
	// OO       OO     X--> 
	private static int [][]btr = new int[][]{
		new int[]{-1, 0, -1}, new int[]{0, 0, -1}, new int[]{-1, 0, 0}, new int[]{0, 0, 0},
		new int[]{-1, 1, -1}, new int[]{0, 1, -1}, new int[]{-1, 1, 0}, new int[]{0, 1, 0}
	};
	
	// bottom   top
	// OO       OX     Z ^
	// OO       OO     X--> 
	private static int [][]ttr = new int[][]{
		new int[]{-1, -1, -1}, new int[]{0, -1, -1}, new int[]{-1, -1, 0}, new int[]{0, -1, 0},
		new int[]{-1,  0, -1}, new int[]{0,  0, -1}, new int[]{-1,  0, 0}, new int[]{0,  0, 0}
	};
	
	private static int[][][] validConfigs = new int[][][]{
		bbl, tbl, bbr, tbr, btl, ttl, btr, ttr
	};
	
	public void readFromNBT(NBTTagCompound access)
    {
        super.readFromNBT(access);
        this.frontSide = access.getInteger("frontSide");
        NBTTagList nbttaglist = (NBTTagList) access.getTag("Items");
        this.furnaceItemStacks = new ItemStack[2];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound compound = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            byte b0 = compound.getByte("Slot");

            if (b0 >= 0 && b0 < this.furnaceItemStacks.length)
            {
                this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(compound);
            }
        }

        this.furnaceBurnTime = access.getShort("BurnTime");
        this.furnaceCookTime = access.getShort("CookTime");
        this.currentItemBurnTime = access.getShort("cIBT");

        if (access.hasKey("CustomName"))
        {
            this.field_145958_o = access.getString("CustomName");
        }
        
        if (access.hasKey("water"))
        {
        	this.myTank.setFluid(new FluidStack(FluidRegistry.WATER,access.getShort("water")));
        }
        
        if (access.hasKey("steam"))
        {
        	this.steam = access.getShort("steam");
        }
       // worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	
    }

    @Override
    public void writeToNBT(NBTTagCompound access)
    {
        super.writeToNBT(access);
        access.setInteger("frontSide", this.frontSide);
        access.setShort("BurnTime", (short)this.furnaceBurnTime);
        access.setShort("water",(short) myTank.getFluidAmount());
        access.setShort("steam",(short) this.steam);
        access.setShort("CookTime", (short)this.furnaceCookTime);
        access.setShort("cIBT", (short)this.currentItemBurnTime);

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

        access.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName())
        {
            access.setString("CustomName", this.field_145958_o);
        }
    }
	
	@Override
	public Packet getDescriptionPacket()
	{
    	super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("frontSide", this.frontSide);
        access.setInteger("steam", steam);
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
    	this.frontSide = access.getInteger("frontSide");
    	this.steam = access.getInteger("steam");
    	this.myTank.setFluid(new FluidStack(FluidRegistry.WATER,access.getInteger("water")));
    	this.furnaceBurnTime = access.getShort("BurnTime");
    	this.currentItemBurnTime = access.getShort("cIBT");
      	this.furnaceCookTime = access.getShort("CookTime");

    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	
	public void checkMultiblock(boolean isBreaking, int frontSide) {
		//System.out.println(frontSideIn);
		int x = xCoord, y=yCoord, z=zCoord;
		
		//System.out.println("Checking multiblock; frontSide: "+this.frontSide);
		
		boolean isMultiblock = false;
		boolean isTooManyBlocks = false;
		
		
		if (!worldObj.isRemote){
			if (!isBreaking){
				//System.out.println(this.frontSide);
				int[] validClusters = getValidClusters(); 
				//System.out.println("valid configs found: " +validClusters.length);
				
				if (validClusters.length == 1){
					
					updateMultiblock(validClusters[0], true, frontSide);
					
					
				}
			}
		}
		
	}
	
	public void destroyMultiblock(){
		updateMultiblock(this.getValidClusterFromMetadata(), false, -1);
	}
	
	private int getValidClusterFromMetadata(){
		int validCluster = -1;
		// Because the clusters at the top are doofy and not in the right order =\
		switch(worldObj.getBlockMetadata(xCoord,yCoord,zCoord)){
		case 1: validCluster = 0; break;
		case 2: validCluster = 2; break;
		case 3: validCluster = 4; break;
		case 4: validCluster = 6; break;
		case 5: validCluster = 1; break;
		case 6: validCluster = 3; break;
		case 7: validCluster = 5; break;
		case 8: validCluster = 7; break;
		}
		
		return validCluster;
	}
	
	private int checkCluster(int[][] cluster){
		int count = 0;
		for (int pos = 0; pos < 8; pos++){
			int x = cluster[pos][0]+xCoord, y= cluster[pos][1]+yCoord, z=cluster[pos][2]+zCoord;
			Block b = worldObj.getBlock(x,y,z);
			if (b == SteamcraftBlocks.flashBoiler){
				TileEntityFlashBoiler fb = (TileEntityFlashBoiler) worldObj.getTileEntity(x, y, z);
				if (! (worldObj.getBlockMetadata(x, y, z) > 0)){
					count++;
				}
				
			}
			
		}
		
		return count;
	}
	
	private int[] getValidClusters(){
		int[] valid = new int[8];
		int[] out;
		int count = 0;
		for (int clusterIndex = 0; clusterIndex< 8; clusterIndex++){
			//System.out.println("Checking cluster "+clusterIndex);
			boolean isValid = false;
			if (checkCluster(validConfigs[clusterIndex])==8){
				valid[count] = clusterIndex;
				count++;
				isValid = true;
			}
		}
		out = new int[count];
		for (int i = 0; i < count; i++){
			out[i] = valid[i];
		}
		return out;
	}
	
	private int[][] getClusterCoords(int clusterIndex){
		int[][] cluster = validConfigs[clusterIndex];
		int[][] out = new int[8][3];
		for (int pos = 0; pos < 8; pos++){
			out[pos]= new int[]{cluster[pos][0]+xCoord, cluster[pos][1]+yCoord, cluster[pos][2]+zCoord};
		}
		return out;
	}
	
	private void updateMultiblock(int clusterIndex, boolean isMultiblock, int frontSide){
		int[][] cluster = getClusterCoords(clusterIndex);
		for (int pos = 0; pos < 8; pos++){
			int x = cluster[pos][0], y= cluster[pos][1],z= cluster[pos][2];
			if (worldObj.getBlock(x, y, z) == SteamcraftBlocks.flashBoiler){
				worldObj.setBlockMetadataWithNotify(
						cluster[pos][0], cluster[pos][1], cluster[pos][2], 
						isMultiblock ? pos+1 : 0, 
						2
					);
				TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) worldObj.getTileEntity(cluster[pos][0], cluster[pos][1], cluster[pos][2]);
				boiler.setFront(frontSide, false);
			} else {
				System.out.println("ERROR! ("+x+","+y+","+z+") is not a flashBoiler!");
			}
			
		}
	}
	
	public TileEntityFlashBoiler getMasterTileEntity(){
		int[][] cluster = getClusterCoords(getValidClusterFromMetadata());
		int x = cluster[0][0], y=cluster[0][1], z=cluster[0][2];
		TileEntityFlashBoiler boiler = null;
		if (worldObj.getBlock(x, y, z)==SteamcraftBlocks.flashBoiler && worldObj.getBlockMetadata(x, y, z) > 0){
			boiler = (TileEntityFlashBoiler) worldObj.getTileEntity(x, y, z);
		}
		
		return boiler;
	}

	public void setFront(int frontSide, boolean print) {
		if (print) System.out.println("Setting front side to "+frontSide);
		if (!worldObj.isRemote)
			this.frontSide = frontSide;
	}
	
	public int getFront(){
		return this.frontSide;
	}
	
	public void updateEntity(){
		
		System.out.println(this.getFront());
		if (waitOneTick)
			waitOneTick = false;
		else {
			if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 4){ // Only the top layer can distribute, just like the boiler.
				if (!isInCluster(xCoord-1, yCoord, zCoord)){
					UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,new ForgeDirection[] { ForgeDirection.WEST });
				}
				if (!isInCluster(xCoord + 1, yCoord, zCoord)){
					UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,new ForgeDirection[] { ForgeDirection.EAST });
				}
				if (!isInCluster(xCoord, yCoord, zCoord - 1)){
					UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,new ForgeDirection[] { ForgeDirection.NORTH });
				}
				if (!isInCluster(xCoord, yCoord, zCoord + 1)){
					UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,new ForgeDirection[] { ForgeDirection.SOUTH });
				}
				UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,new ForgeDirection[] { ForgeDirection.UP });
		    	
			}
			UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
			
	    	if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1){
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
	
	                if (this.isBurning() && this.canSmelt())
	                {
	                    ++this.furnaceCookTime;
	
	                    if (this.furnaceCookTime > 0)
	                    {
	                    	//int i = 0;
	                    //	while (i<maxThisTick && this.isBurning() && this.canSmelt()) {
	                    		this.steam+=10;
	                    		this.myTank.drain(20, true);
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
	                    //BlockBoiler.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	                }
	           }
	    	}
		}
    	
        if (!this.worldObj.isRemote) {
        	//System.out.println(this.furnaceBurnTime);
        }
        this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public static int getItemBurnTime(ItemStack stack)
    {
        if (stack == null)
        {
            return 0;
        }
        else
        {
            Item item = stack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 15;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 30;
                }

                if (block == Blocks.coal_block)
                {
                    return 1600;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 20;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 20;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 20;
            if (item == Items.stick) return 10;
            if (item == Items.coal) return 160;
            if (item == Items.lava_bucket) return 2000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 10;
            if (item == Items.blaze_rod) return 240;
            return GameRegistry.getFuelValue(stack);
        }
    }

	private boolean canSmelt() {
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1 ? myTank.getFluidAmount() > 9 : worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().canSmelt() : false;
	}
	
	private boolean canDrainItem(ItemStack stack) {
    	return stack.stackSize == 1; 
    }
	
	public boolean isBurning() {
		if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1){
			return this.furnaceBurnTime > 0;
		} else if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0){
			if(getMasterTileEntity() != null){
				return getMasterTileEntity().isBurning();
			} else return false;
			
		} else return false;
		  
	}

	public boolean hasMaster(){
		return getMasterTileEntity() != null;
	}
	
	@Override
	public int getSizeInventory() {
		
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1 ? this.furnaceItemStacks.length : (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().getSizeInventory() : 0);
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1? this.furnaceItemStacks[slot] : (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().getStackInSlot(slot) : null);
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1){
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
		} else {
			return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().decrStackSize(par1, par2) : null;
		}
	 
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1){
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
		} else {
			return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().getStackInSlotOnClosing(par1) : null;
		}
        
	}
	
	public boolean isInCluster(int x, int y, int z){
		int[][] cluster = this.getClusterCoords(this.getValidClusterFromMetadata());
		for (int pos = 0; pos < cluster.length; pos++){
			if (x == cluster[pos][0] && y == cluster[pos][1] && z == cluster[pos][1]){
				return worldObj.getBlock(x, y, z) == SteamcraftBlocks.flashBoiler && worldObj.getBlockMetadata(x, y, z) > 0;
			}
		}
		return false;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1){
    		this.furnaceItemStacks[par1] = par2ItemStack;

            if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
            {
                par2ItemStack.stackSize = this.getInventoryStackLimit();
            }	
        } else if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0) {
        	 getMasterTileEntity().setInventorySlotContents(par1, par2ItemStack);
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

	@Override
	public void closeInventory() {}

	@Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == 0 ? getItemBurnTime(stack) > 0 : (stack.getItem() == Items.water_bucket || FluidContainerRegistry.isEmptyContainer(stack) || stack.getItem()  instanceof IFluidContainerItem);
    }

	@Override
	public float getPressure() {
		if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1){
			return (this.steam/(5000.0F*8F));
		} else {
			return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().getPressure() : 0F;
		}
		
	}

	@Override
	public boolean canInsert(ForgeDirection face) {
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 4 && face != myDir();
	}

	@Override
	public int getCapacity() {
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? 5000*8 : 0;
	}

	@Override
	public int getSteam() {
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1 ? steam : (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().getSteam() : 0);
	}

	@Override
	public void insertSteam(int amount, ForgeDirection face) {
		if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1 ) {
			steam+=amount;
		} else if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0) {
			getMasterTileEntity().insertSteam(amount, face);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scale)
    {
		if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1){
			if (this.currentItemBurnTime == 0)
	        {
	            this.currentItemBurnTime = 200;
	        }

	        return this.furnaceBurnTime * scale / this.currentItemBurnTime;
		} else {
			return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().getBurnTimeRemainingScaled(scale) : 0;
		}
        
    }

	@Override
	public void decrSteam(int i) {
		if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord)==1){
			this.steam-=i;
		} else if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0){
			getMasterTileEntity().decrSteam(i);
		}
		
	}

	@Override
	public boolean doesConnect(ForgeDirection face) {
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 4 && face != myDir();
	}

	@Override
	public boolean acceptsGauge(ForgeDirection face) {
		if (face != ForgeDirection.UP && worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0){
			if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 4 && face != ForgeDirection.UP){
				return true;
			} else if (face != myDir()){
				return true;
			}
		} 
		return false;
	}
	
	public ForgeDirection myDir() {
		int meta = this.frontSide;
		switch (meta) {
		case 2:
			return ForgeDirection.NORTH;
		case 3:
			return ForgeDirection.SOUTH;
		case 4:
			return ForgeDirection.WEST;
		case 5:
			return ForgeDirection.EAST;
		}
		return ForgeDirection.NORTH;
	}

	public int[] getAccessibleSlotsFromSide(int side)
    {
        return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
    }

	@Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {	
		int[] accessibleSlots = getAccessibleSlotsFromSide(side);
		boolean isAccessibleSlot = false;
		for (int i = 0; i < accessibleSlots.length; i++){
			if (accessibleSlots[i] == slot) isAccessibleSlot = true;
		}
        return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? this.isItemValidForSlot(slot, stack) && isAccessibleSlot : false;
    }

	@Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
		if (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1){
			return stack.getItem() == Items.bucket;
		} else {
			return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().canExtractItem(slot, stack, side): false;
		}
    }
	
	@SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scale)
    {
        return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1 ? this.furnaceCookTime * scale / 200 : (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().getCookProgressScaled(scale) : 0);
    }

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1 ? myTank.fill(resource, doFill) : (worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().fill(from, resource, doFill) : 0);
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
		return worldObj.getBlockMetadata(xCoord,yCoord,zCoord) == 1 ? new FluidTankInfo[] { new FluidTankInfo(myTank) } : worldObj.getBlockMetadata(xCoord,yCoord,zCoord) > 0 && hasMaster() ? getMasterTileEntity().getTankInfo(from) : new FluidTankInfo[]{new FluidTankInfo(new FluidTank(0))};
	}
}