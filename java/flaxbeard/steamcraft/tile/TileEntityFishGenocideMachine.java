package flaxbeard.steamcraft.tile;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilSteamTransport;

public class TileEntityFishGenocideMachine extends TileEntity implements ISteamTransporter {
	
	private int steam = 0;
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.steam = par1NBTTagCompound.getShort("steam");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("steam",(short) this.steam);
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
			ForgeDirection[] distr = { ForgeDirection.UP, ForgeDirection.DOWN };
			UtilSteamTransport.generalDistributionEvent(worldObj, xCoord, yCoord, zCoord,distr);
			UtilSteamTransport.generalPressureEvent(worldObj,xCoord, yCoord, zCoord, this.getPressure(), this.getCapacity());
		}
	}
	
	@Override
	public float getPressure() {
		return this.steam/1000.0F;
	}

	@Override
	public boolean canInsert(ForgeDirection face) {
		return face == ForgeDirection.DOWN || face == ForgeDirection.UP;
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
		return face == ForgeDirection.DOWN || face == ForgeDirection.UP;
	}
	
	public void dropItem(ItemStack item) {
		EntityItem entityItem = new EntityItem(this.worldObj, this.xCoord+0.5F, this.yCoord + 1.25F, this.zCoord+0.5F, item);
		this.worldObj.spawnEntityInWorld(entityItem);
	}

	public void dropItem(ItemStack item, float x, float y, float z) {
		EntityItem entityItem = new EntityItem(this.worldObj, x, y, z, item);
		this.worldObj.spawnEntityInWorld(entityItem);
	}
}
