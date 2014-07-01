package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import flaxbeard.steamcraft.tile.TileEntityFlashBoiler;

// Notes:
// ATM meta 1 has a special texture, because that's going to be the master TE for the multi.
// meta 1 is always the one with the lowest x, y, and z.

// meta 0  : not in multiblock
// meta 1-8: in multiblock, signifies which corner

// getIcon doesn't get world object, so can't use TE to determine icon
// Might have to do the other block for on status =\
// looking into how connected textures work might help figure out a way around it.

public class BlockFlashBoiler extends BlockContainer{
	public IIcon otherIcon;
	public IIcon specialIcon;
	
	
	public BlockFlashBoiler(){
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFlashBoiler();
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block){
		checkMultiblock(world, x, y, z, false);
	}
	
	
	
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta){
		if (meta > 0){
			TileEntityFlashBoiler te = (TileEntityFlashBoiler) world.getTileEntity(x, y, z);
			te.destroyMultiblock();
		}
		
	}
	
	public void checkMultiblock(World world, int x, int y, int z, boolean isBreaking){
		if (!world.isRemote){
			TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) world.getTileEntity(x, y, z);
			boiler.checkMultiblock(isBreaking);
		}
		
	}
	
	public void registerBlockIcons(IIconRegister p_149651_1_){
		this.blockIcon = p_149651_1_.registerIcon("steamcraft:testSide");
		this.otherIcon = p_149651_1_.registerIcon("steamcraft:testFront");
		this.specialIcon = p_149651_1_.registerIcon("steamcraft:testSpecial");
	}
	
	public IIcon getIcon(int side, int meta){
		//System.out.println(meta);
		if (meta == 1){
			return specialIcon;
		} else if (meta == 0) {
			return blockIcon;
		} else {
			return otherIcon;
		}
		
	}
	
	
}
