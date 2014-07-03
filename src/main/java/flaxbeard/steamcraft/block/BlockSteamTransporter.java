package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import flaxbeard.steamcraft.data.ChunkScoreWorldData;

public class BlockSteamTransporter extends BlockContainer {

	protected BlockSteamTransporter(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return null;
	}
	
	@Override
    public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_)
    {
		ChunkScoreWorldData data = ChunkScoreWorldData.get(world);
		Chunk c = world.getChunkFromBlockCoords(x, z);
		data.down(data, new ChunkCoordinates(c.xPosition, 0, c.zPosition));
        world.perWorldStorage.saveAllData();

		super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
    }
	
	@Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
		ChunkScoreWorldData data = ChunkScoreWorldData.get(world);
		Chunk c = world.getChunkFromBlockCoords(x, z);
		data.up(data, new ChunkCoordinates(c.xPosition, 0, c.zPosition));
        world.perWorldStorage.saveAllData();

		super.onBlockAdded(world, x, y, z);
    }

}
