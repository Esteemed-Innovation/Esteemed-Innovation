package flaxbeard.steamcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import flaxbeard.steamcraft.SteamcraftBlocks;

public class BlockThumperDummy extends Block {

	public BlockThumperDummy() {
		super(Material.iron);
	}
	
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
    	int meta = world.getBlockMetadata(i, j, k) - 1;
		setBlockBounds(0.0F, 0.0F - meta, 0.0F, 1.0F, 4.0F - meta, 1.0F);
    }
	
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_) {
    	int meta = world.getBlockMetadata(x, y, z) - 1;
    	if (world.getBlock(x, y-meta, z) != SteamcraftBlocks.thumper) {
    		world.setBlockToAir(x, y, z);
    	}
    	if (meta != 1 ) {
    		for (int i = 1; i < meta; i++) {
    	    	if (world.getBlock(x, y-i, z) != SteamcraftBlocks.thumperDummy) {
    	    		world.setBlockToAir(x, y, z);
    	    	}
    		}
    	}
    	if (meta != 3 ) {
    		for (int i = 1; i < (3-meta); i++) {
    	    	if (world.getBlock(x, y+i, z) != SteamcraftBlocks.thumperDummy) {
    	    		world.setBlockToAir(x, y, z);
    	    	}
    		}
    	}
    }
    
    
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

}
