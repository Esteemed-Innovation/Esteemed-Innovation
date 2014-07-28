package flaxbeard.steamcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityFishGenocideMachine;

public class BlockFishGenocideMachine extends BlockSteamTransporter
{
    @SideOnly(Side.CLIENT)
	public IIcon top;
    public int pass = 0;
    
    @Override
    public boolean canRenderInPass(int x) {
    	pass = x;
    	return x <= 1;
    }
    
    public BlockFishGenocideMachine()
    {
        super(Material.iron);
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
    	float px = 1.0F/16.0F;
        this.setBlockBounds(0.0F+3*px, 0.0F, 0.0F+3*px, 1.0F-3*px, 1.0F, 1.0F-3*px);
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.top : (p_149691_1_ == 0 ? this.top : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blockBrass");
        this.top =  p_149651_1_.registerIcon("steamcraft:blockBrass");
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFishGenocideMachine();
	}
	
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return Steamcraft.genocideRenderID;
    }
}