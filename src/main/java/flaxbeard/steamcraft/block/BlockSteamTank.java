package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.tile.TileEntitySteamTank;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSteamTank extends BlockContainer {

	public BlockSteamTank() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySteamTank();
	}

}
