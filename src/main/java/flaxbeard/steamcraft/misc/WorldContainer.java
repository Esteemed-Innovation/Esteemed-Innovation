package flaxbeard.steamcraft.misc;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WorldContainer implements IBlockAccess {
	
	IBlockAccess myWorld;
	int allMeta;
	
	public WorldContainer(IBlockAccess world, int meta) {
		this.myWorld = world;
		this.allMeta = meta;
	}

	@Override
	public Block getBlock(int var1, int var2, int var3) {
		return myWorld.getBlock(var1, var2, var3);
	}

	@Override
	public TileEntity getTileEntity(int var1, int var2, int var3) {
		return myWorld.getTileEntity(var1, var2, var3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
		return myWorld.getLightBrightnessForSkyBlocks(var1, var2, var3, var4);
	}

	@Override
	public int getBlockMetadata(int var1, int var2, int var3) {
		return allMeta;
	}

	@Override
	public boolean isAirBlock(int var1, int var2, int var3) {
		return myWorld.isAirBlock(var1, var2, var3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BiomeGenBase getBiomeGenForCoords(int var1, int var2) {
		return myWorld.getBiomeGenForCoords(var1, var2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight() {
		return myWorld.getHeight();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache() {
		return myWorld.extendedLevelsInChunkCache();
	}

	@Override
	public int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4) {
		return myWorld.isBlockProvidingPowerTo(var1, var2, var3, var4);
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side,
			boolean _default) {
		return myWorld.isSideSolid(x, y, z, side, _default);
	}

}
