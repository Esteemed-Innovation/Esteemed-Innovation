package eiteam.esteemedinnovation.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Coord4 {
    public int dimension;
    public BlockPos pos;

    public Coord4(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public static Coord4 readFromNBT(NBTTagCompound nbt) {
        int xc = nbt.getInteger("x");
        int yc = nbt.getInteger("y");
        int zc = nbt.getInteger("z");
        int d = nbt.getInteger("dimension");
        BlockPos pos = new BlockPos(xc, yc, zc);
        return new Coord4(pos, d);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("x", pos.getX());
        nbt.setInteger("y", pos.getY());
        nbt.setInteger("z", pos.getZ());
        nbt.setInteger("dimension", dimension);

        return nbt;
    }

    public TileEntity getTileEntity(IBlockAccess world) {
        return world.getTileEntity(pos);
    }

    public Block getBlock(IBlockAccess world) {
        return getBlockState(world).getBlock();
    }

    public IBlockState getBlockState(IBlockAccess world) {
        return world.getBlockState(pos);
    }

    public String toString() {
        return "Coord4: " + pos.toString() + "; Dimension: " + dimension;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Coord4) {
            Coord4 coord = (Coord4) other;
            return pos.equals(coord.pos) && dimension == coord.dimension;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + pos.getX();
        hash = 31 * hash + pos.getY();
        hash = 31 * hash + pos.getZ();
        hash = 31 * hash + dimension;
        return hash;
    }
}