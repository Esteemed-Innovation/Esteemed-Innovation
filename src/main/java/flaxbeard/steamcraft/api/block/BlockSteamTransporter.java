package flaxbeard.steamcraft.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.data.ChunkScoreWorldData;

public abstract class BlockSteamTransporter extends BlockContainer {
	
	public BlockSteamTransporter(Material material){
		super(material);
	}
	
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta){
		ISteamTransporter te = (ISteamTransporter) world.getTileEntity(x, y, z);
		if (te != null && te.getNetwork() != null){
			te.getNetwork().split(te, true);
		}
		
	}
	
	
	@Override
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_)
    {
		ChunkScoreWorldData data = ChunkScoreWorldData.get(world);
		Chunk c = world.getChunkFromBlockCoords(x, z);
		data.down(c.xPosition,c.zPosition);
        world.perWorldStorage.saveAllData();

		super.breakBlock(world, x, y, z, block, p_149749_6_);
    }
	
	@Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
		ChunkScoreWorldData data = ChunkScoreWorldData.get(world);
		Chunk c = world.getChunkFromBlockCoords(x, z);
		data.up(c.xPosition,c.zPosition);
        world.perWorldStorage.saveAllData();

		super.onBlockAdded(world, x, y, z);
    }

}
