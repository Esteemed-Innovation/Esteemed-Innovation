package flaxbeard.steamcraft.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import flaxbeard.steamcraft.data.AetherBlockData;

public class SteamcraftIslandGen implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

		switch(world.provider.dimensionId){
		case -1:
		    generateNether(world, random, chunkX * 16, chunkZ * 16);
		    break;
		case 0:
		    generateSurface(world, random, chunkX * 16, chunkZ * 16);
		    break;
		case 1:
		    generateEnd(world, random, chunkX * 16, chunkZ * 16);
		    break;
		}	
	}

	private void generateEnd(World world, Random random, int i, int j) {
		// TODO Auto-generated method stub
		
	}

	private void generateSurface(World world, Random random, int i, int j) {
		if (random.nextInt(10) == 0) {
			int minY = 255;
			int maxY = 0;
			for (int x = 0; x<16; x++) {
				for (int z = 0; z<16; z++) {
					for (int y = 255; y>0; y--) {
						if (world.getBlock(i+x, y, j+z).getMaterial() != Material.air && !world.getBlock(i+x, y, j+z).isFoliage(world, i+x, y, j+z)) {
							if (y < minY) {
								minY = y;
							}
							if (y > maxY) {
								maxY = y;
							}
							break;
						}
					}
				}
			}
			if (maxY-minY <= 14) {
				int offset = Math.max(20, 255 - maxY - (maxY-minY)-random.nextInt(125));
				for (int x = 0; x<16; x++) {
					for (int z = 0; z<16; z++) {
						int xOffset = 8-Math.max(Math.abs(8-x), Math.abs(7-x));
						int zOffset = 8-Math.max(Math.abs(8-z), Math.abs(7-z));
	
						int bottomOffset = Math.min(6, (int)((xOffset+zOffset)/1.5F))-2;
						if (xOffset + zOffset > 4) {
							for (int y = 255; y>(minY-bottomOffset); y--) {
								if (world.getBlock(i+x, y, j+z).getMaterial() != Material.air && world.getBlock(i+x, y, j+z).getMaterial() != Material.water) {
									////Steamcraft.log.debug("YAY");
		
									Block block = world.getBlock(i+x, y, j+z);
									int meta = world.getBlockMetadata(i+x, y, j+z);
									world.setBlockToAir(i+x, y, j+z);
									if (y == (minY-bottomOffset)+1 && world.getBlock(i+x, y-1, j+z) == Blocks.dirt) {
										world.setBlock(i+x, y-1, j+z, Blocks.grass);
									}
									world.setBlock(i+x, y+offset, j+z,block,meta,2);
									AetherBlockData.get(world).addCoord(new ChunkCoordinates(i+x, y+offset, j+z));
								}
							}
						}
					}
				}
			}
		}
	}

	private void generateNether(World world, Random random, int i, int j) {
		// TODO Auto-generated method stub
		
	}
}
