package flaxbeard.steamcraft.tile;

import java.util.ArrayList;

import net.minecraft.block.BlockFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.MutablePair;

import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.UtilSteamTransport;

public class TileEntitySteamHeater extends TileEntity implements ISteamTransporter {
	
	private int steam = 0;
	public boolean master;
	private int numHeaters = 0;
	private boolean prevHadYuck = true;
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.steam = par1NBTTagCompound.getShort("steam");
        this.prevHadYuck = par1NBTTagCompound.getBoolean("prevHadYuck");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("steam",(short) this.steam);
        par1NBTTagCompound.setBoolean("prevHadYuck", prevHadYuck);
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
		if (!this.worldObj.isRemote) {
			int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			ForgeDirection dir = ForgeDirection.getOrientation(meta);
			ForgeDirection[] directions = new ForgeDirection[6];
			int i = 0;
			for (ForgeDirection direction : ForgeDirection.values()) {
				if (direction != dir) {
					directions[i] = direction;
					i++;
				}
			}
			UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,directions);
			UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
		}
		
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		ArrayList<TileEntitySteamHeater> slaves = new ArrayList<TileEntitySteamHeater>();
		if (this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) != null) {
			if (this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) instanceof TileEntityFurnace) {
				numHeaters = 0;
				if (!this.master) {
					prevHadYuck = true;
				}
				this.master = false;
				for (int i = 0; i<6; i++) {
					ForgeDirection dir2 = ForgeDirection.getOrientation(i);
					int x = xCoord+dir.offsetX + dir2.offsetX;
					int y = yCoord+dir.offsetY + dir2.offsetY;
					int z = zCoord+dir.offsetZ + dir2.offsetZ;
					if (this.worldObj.getTileEntity(x,y,z) != null) {
						if (this.worldObj.getTileEntity(x,y,z) instanceof TileEntitySteamHeater && ((TileEntitySteamHeater)this.worldObj.getTileEntity(x,y,z)).getSteam() > 2 && this.worldObj.getBlockMetadata(x, y, z) == ForgeDirection.OPPOSITES[i]) {
							this.master = (x==xCoord && y==yCoord && z==zCoord);
							slaves.add((TileEntitySteamHeater) this.worldObj.getTileEntity(x,y,z));
							numHeaters++;
							if (slaves.size() > 4) {
								slaves.remove(0);
							}
							numHeaters = Math.min(4, numHeaters);
						}
					}
				}
				if (this.master && numHeaters > 0) {
					TileEntityFurnace furnace = (TileEntityFurnace) this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
					if (!(furnace instanceof TileEntitySteamFurnace)) {
						ItemStack[] furnaceItemStacks = new ItemStack[]  {furnace.getStackInSlot(0), furnace.getStackInSlot(1), furnace.getStackInSlot(2)};
					    int furnaceBurnTime = furnace.furnaceBurnTime;
					    int currentItemBurnTime = furnace.currentItemBurnTime;
					    int furnaceCookTime = furnace.furnaceCookTime; 
						this.worldObj.setTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ, new TileEntitySteamFurnace());
						TileEntityFurnace furnace2 = (TileEntityFurnace) this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
						furnace2.setInventorySlotContents(0, furnaceItemStacks[0]);
						furnace2.setInventorySlotContents(1, furnaceItemStacks[1]);
						furnace2.setInventorySlotContents(2, furnaceItemStacks[2]);
						furnace2.furnaceBurnTime = furnaceBurnTime;
						furnace2.currentItemBurnTime = currentItemBurnTime;
						furnace2.furnaceCookTime = furnaceCookTime;
					}
//					if (!prevHadYuck && furnace.getStackInSlot(2) != null && SteamcraftRegistry.steamedFoods.containsKey(furnace.getStackInSlot(2).getItem())) {
//						int size = furnace.getStackInSlot(2).stackSize;
//						ItemStack replacement = new ItemStack(SteamcraftRegistry.steamedFoods.get(furnace.getStackInSlot(2).getItem()));
//						replacement.stackSize = size;
//						furnace.setInventorySlotContents(2, replacement);
//						this.worldObj.markBlockForUpdate(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
//					}
					if ((furnace.furnaceBurnTime == 1 || furnace.furnaceBurnTime == 0) && this.steam >= 2 && canSmelt(furnace)) {
						if (furnace.furnaceBurnTime == 0) {
							BlockFurnace.updateFurnaceBlockState(true, this.worldObj, xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
						}
						for (TileEntitySteamHeater heater : slaves) {
							heater.decrSteam(2);
						}
						furnace.furnaceBurnTime+=3;
//						if (furnace.furnaceCookTime > 0) {
//							furnace.furnaceCookTime--;
//						}
						if (this.numHeaters > 1 && furnace.furnaceCookTime > 0) {
							furnace.furnaceCookTime += 2*this.numHeaters-1;
							furnace.furnaceCookTime = Math.min(furnace.furnaceCookTime,199);
						}
						this.worldObj.markBlockForUpdate(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
						this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
					this.prevHadYuck = !(furnace.getStackInSlot(2) == null || !SteamcraftRegistry.steamedFoods.containsKey(furnace.getStackInSlot(2).getItem()));
				}
			}
		}
	}
	
	private boolean canSmelt(TileEntityFurnace furnace)
    {
        if (furnace.getStackInSlot(0) == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(furnace.getStackInSlot(0));
            if (itemstack == null) return false;
            if (SteamcraftRegistry.steamedFoods.containsKey(MutablePair.of(itemstack.getItem(), -1))) {
            	int meta = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), -1)).right;
            	Item item = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), -1)).left;
            	if (meta == -1) {
            		itemstack = new ItemStack(item);
            	}
            	else
            	{
            		itemstack = new ItemStack(item, 0, meta);
            	}
            }
            if (SteamcraftRegistry.steamedFoods.containsKey(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage()))) {
            	int meta = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage())).right;
            	Item item = SteamcraftRegistry.steamedFoods.get(MutablePair.of(itemstack.getItem(), itemstack.getItemDamage())).left;
            	if (meta == -1) {
            		itemstack = new ItemStack(item);
            	}
            	else
            	{
            		itemstack = new ItemStack(item, 0, meta);
            	}
            }
            if (furnace.getStackInSlot(2) == null) return true;
            if (!furnace.getStackInSlot(2).isItemEqual(itemstack)) return false;
            int result = furnace.getStackInSlot(2).stackSize + itemstack.stackSize;
            return result <= furnace.getInventoryStackLimit() && result <= furnace.getStackInSlot(2).getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

	@Override
	public float getPressure() {
		return this.steam/1000.0F;
	}

	@Override
	public boolean canInsert(ForgeDirection face) {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		return face != dir;
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
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		return face != dir;
	}
	
	@Override
	public boolean acceptsGauge(ForgeDirection face) {
		return false;
	}

	public static void replace(TileEntitySteamFurnace te) {
		TileEntitySteamFurnace furnace = (TileEntitySteamFurnace) te.getWorldObj().getTileEntity(te.xCoord, te.yCoord, te.zCoord);
		if (furnace != null) {
			ItemStack[] furnaceItemStacks = new ItemStack[]  {furnace.getStackInSlot(0), furnace.getStackInSlot(1), furnace.getStackInSlot(2)};
		    int furnaceBurnTime = furnace.furnaceBurnTime;
		    int currentItemBurnTime = furnace.currentItemBurnTime;
		    int furnaceCookTime = furnace.furnaceCookTime; 
		    te.getWorldObj().setTileEntity(te.xCoord, te.yCoord, te.zCoord, new TileEntityFurnace());
			TileEntityFurnace furnace2 = (TileEntityFurnace) te.getWorldObj().getTileEntity(te.xCoord, te.yCoord, te.zCoord);
			furnace2.setInventorySlotContents(0, furnaceItemStacks[0]);
			furnace2.setInventorySlotContents(1, furnaceItemStacks[1]);
			furnace2.setInventorySlotContents(2, furnaceItemStacks[2]);
			furnace2.furnaceBurnTime = furnaceBurnTime;
			furnace2.currentItemBurnTime = currentItemBurnTime;
			furnace2.furnaceCookTime = furnaceCookTime;
		}
	}

}
