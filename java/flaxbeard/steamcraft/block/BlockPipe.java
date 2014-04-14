package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPipe extends BlockContainer {

	public BlockPipe() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySteamPipe();
	}

}
