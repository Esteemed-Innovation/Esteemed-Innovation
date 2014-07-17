package flaxbeard.steamcraft.api.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import flaxbeard.steamcraft.api.ISteamTransporter;

public abstract class BlockSteamTransporter extends BlockContainer {
	
	public BlockSteamTransporter(Material material){
		super(material);
	}
	
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta){
		ISteamTransporter te = (ISteamTransporter) world.getTileEntity(x, y, z);
		if (te != null && te.getNetwork() != null){
			te.getNetwork().split(te);
		}
		
	}

}
