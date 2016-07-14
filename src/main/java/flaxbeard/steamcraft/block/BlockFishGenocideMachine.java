package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.tile.TileEntityFishGenocideMachine;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFishGenocideMachine extends BlockContainer {
    public BlockFishGenocideMachine() {
        super(Material.IRON);
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityFishGenocideMachine();
	}
}