package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityFan;

public class BlockFan extends BlockSteamTransporter implements IWrenchable {
	
	private IIcon iconOn;
	private IIcon iconOff;
    public BlockFan() {
		super(Material.iron);
	}
    
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_)
    {
        boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
	    if ((tileEntity != null && tileEntity instanceof TileEntityFan))
	    {
	    	TileEntityFan ped = (TileEntityFan)tileEntity;
	    	ped.updateRedstoneState(flag);
	    }
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return -1;
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
    	int meta = world.getBlockMetadata(i,j,k);
    	ForgeDirection dir = ForgeDirection.getOrientation(meta);
    	return AxisAlignedBB.getBoundingBox(i+(dir.offsetX < 0 ? 0.625F : 0), j+(dir.offsetY < 0 ? 0.625F : 0), k+(dir.offsetZ < 0 ? 0.625F : 0), i+1.0F+(dir.offsetX > 0 ? -0.625F : 0), j+1.0F+(dir.offsetY > 0 ? -0.625F : 0), k+1.0F+(dir.offsetZ > 0 ? -0.625F : 0));
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
    {
    	int meta = par1iBlockAccess.getBlockMetadata(par2,par3,par4);
    	ForgeDirection dir = ForgeDirection.getOrientation(meta);
    	setBlockBounds((dir.offsetX < 0 ? 0.625F : 0), (dir.offsetY < 0 ? 0.625F : 0), (dir.offsetZ < 0 ? 0.625F : 0), 1.0F+(dir.offsetX > 0 ? -0.625F : 0), 1.0F+(dir.offsetY > 0 ? -0.625F : 0), 1.0F+(dir.offsetZ > 0 ? -0.625F : 0));
    	super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
    }

	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
    }
    
    public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_, EntityLivingBase p_150071_4_)
    {
        if (MathHelper.abs((float)p_150071_4_.posX - (float)p_150071_1_) < 2.0F && MathHelper.abs((float)p_150071_4_.posZ - (float)p_150071_3_) < 2.0F)
        {
            double d0 = p_150071_4_.posY + 1.82D - (double)p_150071_4_.yOffset;

            if (d0 - (double)p_150071_2_ > 2.0D)
            {
                return 1;
            }

            if ((double)p_150071_2_ - d0 > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double)(p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFan();
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }
    
	@Override
	public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float xO, float yO, float zO) {
        world.setBlockMetadataWithNotify(x, y, z, side, 2);
        return true;
	}
}
