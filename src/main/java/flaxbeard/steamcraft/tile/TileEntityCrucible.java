package flaxbeard.steamcraft.tile;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.MutablePair;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.SteamcraftRegistry;

public class TileEntityCrucible extends TileEntity {
	public ArrayList<CrucibleLiquid> contents = new ArrayList<CrucibleLiquid>();
	public HashMap<CrucibleLiquid,Integer> number = new HashMap<CrucibleLiquid,Integer>();
	public boolean hasUpdated = true;
	private int targetFill = -1;
	private boolean tipping;
	public boolean needsUpdate = false;
	public int tipTicks=0;
	private ForgeDirection[] dirs = { ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST };
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
    	NBTTagList nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("liquids");

    	for (int i = 0; i < nbttaglist.tagCount(); ++i)
    	{
    		NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
    		CrucibleLiquid liquid = SteamcraftRegistry.getLiquidFromName(nbttagcompound1.getString("name"));
    		if (liquid != null) {
	    		this.contents.add(liquid);
	    		this.number.put(liquid, (int) nbttagcompound1.getShort("amount"));
    		}
    	}
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (CrucibleLiquid liquid : this.contents)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setShort("amount", (short)(int)this.number.get(liquid));
            nbttagcompound1.setString("name", liquid.name);
            nbttaglist.appendTag(nbttagcompound1);
        }
        
        par1NBTTagCompound.setTag("liquids", nbttaglist);
    }
	
	@Override
	public Packet getDescriptionPacket()
	{
    	super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();
        access.setInteger("tipTicks",this.tipTicks);
        access.setBoolean("tipping", tipping);

        for (CrucibleLiquid liquid : this.contents)
        {
        	if (liquid != null) {
	            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	            nbttagcompound1.setShort("amount", (short)(int)this.number.get(liquid));
	            nbttagcompound1.setString("name", liquid.name);
	            nbttaglist.appendTag(nbttagcompound1);
        	}
        }
        
        access.setTag("liquids", nbttaglist);
        
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	    

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	NBTTagList nbttaglist = (NBTTagList) access.getTag("liquids");

    	contents = new ArrayList<CrucibleLiquid>();
    	number = new HashMap<CrucibleLiquid,Integer>();
    	if (this.tipTicks == 0) {
    		this.tipTicks = access.getInteger("tipTicks");
    	}
    	this.tipping = access.getBoolean("tipping");
    	for (int i = 0; i < nbttaglist.tagCount(); ++i)
    	{
    		NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
    		CrucibleLiquid liquid = SteamcraftRegistry.getLiquidFromName(nbttagcompound1.getString("name"));
    		this.contents.add(liquid);
    		this.number.put(liquid, (int) nbttagcompound1.getShort("amount"));
    	}
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	
	
	public TileEntityCrucible() {
		//contents.add(Steamcraft.liquidCopper);
		//number.put(Steamcraft.liquidCopper, 90);
		//contents.add(Steamcraft.liquidGold);
		//number.put(Steamcraft.liquidGold, 27);
	}
	
	@Override
	public void updateEntity() {
		if (targetFill <0){
			this.targetFill = this.getFill();
		}
		if (this.getFill() == targetFill){
			hasUpdated = true;
		}
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		if (this.tipping) {
			this.tipTicks++;
			if (this.tipTicks == 45 && !this.worldObj.isRemote) {
				
				int posX = this.xCoord+dirs[meta].offsetX;
				int posZ = this.zCoord+dirs[meta].offsetZ;
				if (this.worldObj.getTileEntity(posX, this.yCoord, posZ) != null) {
					if (this.worldObj.getTileEntity(posX, this.yCoord, posZ) instanceof TileEntityMold) {
						TileEntityMold mold = (TileEntityMold) this.worldObj.getTileEntity(posX, this.yCoord, posZ);
						if (mold.canPour() && this.contents.size() > 0) {
							ICrucibleMold crucibleMold = (ICrucibleMold) mold.mold[0].getItem();
							CrucibleLiquid liquid = this.getNextLiquid(crucibleMold);
							if (liquid != null) {
								if (!worldObj.isRemote) {
									mold.pour(liquid);
								}
								int currNum = number.get(liquid);
								currNum -= crucibleMold.getCostToMold(liquid);
								if (currNum == 0) {
									contents.remove(liquid);
								}
								number.remove(liquid);
								if (currNum > 0) {
									number.put(liquid, currNum);
								}
								needsUpdate = true;

							}
						}
					}
				}
			}
			if (this.tipTicks > 140)
			{
				this.tipTicks = 0;
				this.tipping = false;
			}
			if (this.tipTicks > 140)
			{
				this.tipTicks = 0;
				this.tipping = false;
			}
		}
		for (CrucibleLiquid liquid : SteamcraftRegistry.liquids) {
			if (liquid.recipe != null) {
				CrucibleFormula recipe = liquid.recipe;
				if (recipe.matches(contents, number, recipe)) {
					int currNum = number.get(recipe.liquid1);
					currNum -= recipe.liquid1num;
					if (currNum == 0) {
						contents.remove(recipe.liquid1);
					}
					number.remove(recipe.liquid1);
					number.put(recipe.liquid1, currNum);
					
					currNum = number.get(recipe.liquid2);
					currNum -= recipe.liquid2num;
					if (currNum == 0) {
						contents.remove(recipe.liquid2);
					}
					number.remove(recipe.liquid2);
					number.put(recipe.liquid2, currNum);
					
					if (!number.containsKey(liquid)) {
						contents.add(liquid);
						number.put(liquid, 0);
					}
					currNum = number.get(liquid);
					currNum += recipe.output;
				//	System.out.println(currNum);
					number.remove(liquid);
					number.put(liquid, currNum);
					needsUpdate = true;
				}
			}
		}
		if (needsUpdate) {
			System.out.println("UDPATE");
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			needsUpdate = false;
		}

	}
	
	public int getFill() {
		int fill = 0;
		for (CrucibleLiquid liquid : contents) {
			fill += number.get(liquid);
		}
		//System.out.println("fill: " + fill);
		return fill;
		
	}
	
	public ItemStack fillWith(ItemStack stack, int amount, MutablePair output){
		int fill = this.getFill();
		if (!worldObj.isRemote){
			if (fill + amount <= 90 && this.hasUpdated) {
				//System.out.println(fill + " " +this.hasUpdated);
				CrucibleLiquid fluid = (CrucibleLiquid) output.left;
				if (!this.contents.contains(fluid)) {
					this.contents.add(fluid);
					this.number.put(fluid, 0);
				}
				int currAmount = this.number.get(fluid);
				currAmount += amount;
				this.number.remove(fluid);
				this.number.put(fluid, currAmount);
				stack.stackSize--;
				this.hasUpdated = false;
				this.targetFill = fill + amount;
				needsUpdate = true;

			}
		}
		return stack;
	}
	
	public CrucibleLiquid getLiquidFromIngot(ItemStack ingot) {
		CrucibleLiquid output = null;
		for (CrucibleLiquid liquid : SteamcraftRegistry.liquids) {
			ItemStack ingotClone = ingot.copy();
			ingotClone.stackSize = 1;
			if (liquid.ingot.equals(ingotClone)) {
				output = liquid;
				break;
			}
		}
		return output;
	}
	
	public CrucibleLiquid getNextLiquid(ICrucibleMold mold) {
		for (CrucibleLiquid liquid : SteamcraftRegistry.liquids) {
			if (this.number.containsKey(liquid)) {
				if (mold.canUseOn(liquid) && this.number.get(liquid) >= mold.getCostToMold(liquid)) {
					return liquid;
				}
			}
		}
		return null;
	}
	
	public boolean isTipping() {
		return !(!this.tipping || this.tipTicks > 90);
	}

	public void setTipping() {
		//needsUpdate = true;
		this.tipping = true;
		this.tipTicks = 0;
	}
}
