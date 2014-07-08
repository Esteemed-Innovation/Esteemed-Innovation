package flaxbeard.steamcraft.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.SteamcraftBlocks;

public class TileEntitySteam extends TileEntity {
	
	public int steam = -1;
	
	public TileEntitySteam() {

	}
	
	 
    @Override
    public void readFromNBT(NBTTagCompound access)
    {
        super.readFromNBT(access);
        steam = access.getInteger("steam");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound access)
    {
    	super.writeToNBT(access);
    	access.setInteger("steam", steam);
    }
	
	@Override
	public void updateEntity() {
		//this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);

		if (this.steam == -1) {
			int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			this.steam = 10000 * (meta+1);
		//	System.out.println(MathHelper.floor_double((double)this.steam/10000.0D));
		}
		super.updateEntity();
		this.steam--;
		if (this.steam <= 0) {
			this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		}


		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		//System.out.println(meta);
		if (MathHelper.ceiling_double_int((double)this.steam/10000.0D)-1 != (meta)) {
			this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, MathHelper.ceiling_double_int((double)this.steam/10000.0D)-1, 2);
		}
		ArrayList<Integer> array = new ArrayList<Integer>();
		array.add(0);
		array.add(1);
		array.add(2);
		array.add(3);
		array.add(4);
		array.add(5);
		Collections.shuffle(array, new Random(xCoord+yCoord+zCoord));
		if (MathHelper.ceiling_double_int((double)this.steam/10000.0D) > 0) {
			
			for (int i = 0; i<6; i++) {
				if (this.steam > 5) {
					ForgeDirection dir = ForgeDirection.getOrientation(array.get(i));
					if (this.worldObj.isAirBlock(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) && this.worldObj.getBlock(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) != SteamcraftBlocks.steam) {
						this.worldObj.setBlock(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ, SteamcraftBlocks.steam, MathHelper.ceiling_double_int((double)this.steam/10000.0D)-1, 2);
						((TileEntitySteam) this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ)).steam = 1;
						this.steam--;
					}
					if (this.worldObj.getBlock(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) == SteamcraftBlocks.steam) {
						int theirSteam = ((TileEntitySteam) this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ)).steam;
						if (theirSteam < this.steam - 1) {
							int diff = (this.steam - theirSteam) / 2;
							this.steam -= diff;
							((TileEntitySteam) this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ)).steam += diff;
						}
					}
				}
			}
		}
	}
	
}
