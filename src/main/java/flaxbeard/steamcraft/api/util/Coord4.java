package flaxbeard.steamcraft.api.util;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class Coord4 {
	public int x, y, z, dimension;
	
	public Coord4(int x, int y, int z, int dimension){
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		nbt.setInteger("dimension", dimension);

		return nbt;
	}
	
	public static Coord4 readFromNBT(NBTTagCompound nbt){
		int xc = nbt.getInteger("x");
		int yc = nbt.getInteger("y");
		int zc = nbt.getInteger("z");
		int d = nbt.getInteger("dimension");
		return new Coord4(xc, yc, zc, d);
	}
	
	public TileEntity getTileEntity(IBlockAccess world){
		return world.getTileEntity(x, y, z);
	}
	
	public Block getBlock(IBlockAccess world){
		return world.getBlock(x, y, z);
	}
	
	
	public String toString(){
		return "Coord4: " + x + ", " + y + ", " + z + "; Dimension: " + dimension;
	}
	
	
	
	@Override
	public boolean equals(Object other){
		if (other instanceof Coord4 &&
			this.x == ((Coord4)other).x &&
			this.y == ((Coord4)other).y &&
			this.z == ((Coord4)other).z &&
			this.dimension == ((Coord4)other).dimension
			){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		int hash = 1;
		hash = 31 * hash + x;
		hash = 31 * hash + y;
		hash = 31 * hash + z;
		hash = 31 * hash + dimension;
		return hash;
	}
}
