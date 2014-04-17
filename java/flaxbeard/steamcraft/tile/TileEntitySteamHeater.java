package flaxbeard.steamcraft.tile;

import net.minecraft.block.BlockFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.ISteamTransporter;

public class TileEntitySteamHeater extends TileEntity implements ISteamTransporter {
	
	private int steam = 0;
	
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
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		if (this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) != null) {
			if (this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) instanceof TileEntityFurnace) {
				TileEntityFurnace furnace = (TileEntityFurnace) this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);

				if ((furnace.furnaceBurnTime == 1 || furnace.furnaceBurnTime == 0) && this.steam >= 2 && canSmelt(furnace)) {
					
					if (furnace.furnaceBurnTime == 0) {
						BlockFurnace.updateFurnaceBlockState(true, this.worldObj, xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
					}
					steam-=2;
					furnace.furnaceBurnTime+=3;
					if (furnace.furnaceCookTime > 0) {
						furnace.furnaceCookTime--;
					}
					this.worldObj.markBlockForUpdate(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
		return face == dir.getOpposite();
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
	public void decrSteam(int i) {}

	@Override
	public boolean doesConnect(ForgeDirection face) {
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		return face == dir.getOpposite();
	}

}
