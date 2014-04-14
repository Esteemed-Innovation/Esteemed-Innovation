package flaxbeard.steamcraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.tile.TileEntityBoiler;

public class BlockBoiler extends BlockContainer {

	public BlockBoiler() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBoiler();
	}
	
	@Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityBoiler tileentityfurnace = (TileEntityBoiler)par1World.getTileEntity(par2, par3, par4);

            if (tileentityfurnace != null)
            {
            	par5EntityPlayer.openGui(Steamcraft.instance, 0, par1World, par2,par3,par4);
            }

            return true;
        }
    }

}
