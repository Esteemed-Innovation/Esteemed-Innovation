package flaxbeard.steamcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityPump;

public class BlockPump extends BlockSteamTransporter
{

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
	{
        int l = MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}

    public BlockPump()
    {
        super(Material.iron);
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
//    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
//		return null;
//    }
//    
//    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int xp, int yp, int zp)
//    {
//    	int meta = blockAccess.getBlockMetadata(xp, yp, zp);
//    	float px = 1.0F/16.0F;
//		float x = 0;
//		float y = 0;
//		float z = 5*px;
//		float x2 = 15*px;
//		float y2 = 12*px;
//		float z2 = 11*px;
//		switch (meta) {
//		case 0:
//			this.setBlockBounds(z, y, x, z2, y2, x2);
//			break;
//		case 1:
//			this.setBlockBounds(1-x2, y, 1-z2, 1-x, y2, 1-z);
//			break;
//		case 2:	
//			this.setBlockBounds(1-z2, y, 1-x2, 1-z, y2, 1-x);
//			break;
//		case 3:
//			this.setBlockBounds(x, y, z, x2, y2, z2);
//			break;
//		
//		}
//    }
//    
	
    public boolean isOpaqueCube()
    {
        return false;
    }
    
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blankTexture");
    }
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityPump();
	}
}