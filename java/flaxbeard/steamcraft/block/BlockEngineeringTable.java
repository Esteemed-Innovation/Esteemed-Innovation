package flaxbeard.steamcraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.tile.TileEntityEngineeringTable;

public class BlockEngineeringTable extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private IIcon top;
    private IIcon bottom;

    public BlockEngineeringTable()
    {
        super(Material.wood);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.top : (p_149691_1_ == 0 ? this.bottom : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("furnace_side");
        this.bottom = p_149651_1_.registerIcon("furnace_top");
        this.top = p_149651_1_.registerIcon(this.getTextureName() + "_top");
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
        	TileEntityEngineeringTable tileentityfurnace = (TileEntityEngineeringTable)par1World.getTileEntity(par2, par3, par4);

            if (tileentityfurnace != null)
            {
            	par5EntityPlayer.openGui(Steamcraft.instance, 2, par1World, par2,par3,par4);
            }

            return true;
        }
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityEngineeringTable();
	}
}