package flaxbeard.steamcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityFluidSteamConverter;

public class BlockFluidSteamConverter extends BlockSteamTransporter implements IWrenchable{
	
    private IIcon sideIcon;

	public BlockFluidSteamConverter() {
		super(Material.iron);
	}
	
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == meta ? ((BlockBoiler)SteamcraftBlocks.boiler).steamIcon : side == ForgeDirection.getOrientation(meta).getOpposite().ordinal() ? sideIcon : blockIcon;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
    	int meta = world.getBlockMetadata(i, j, k);
		float ringMin = 4.0F/16.0F;
		float ringMax = 12.0F/16.0F;
        float px = 1.0F/16.0F;
		float x = ringMin;
		float y = ringMin;
		float z = 0.0F;
		float x2 = ringMax;
		float y2 = ringMax;
		float z2 = 0.999F;
		ForgeDirection dir = ForgeDirection.getOrientation(meta).getOpposite();
		this.setBlockBounds(z, y, x, z2, y2, x2);
		//Steamcraft.log.debug(meta);
        switch (meta) {
        	case 0:
        		return AxisAlignedBB.getBoundingBox(i+x, j+z, k+y, i+x2, j+z2,k+ y2);
        	case 1:
        		return AxisAlignedBB.getBoundingBox(i+x, j+z, k+y, i+x2, j+z2,k+ y2);
			case 5:
				return AxisAlignedBB.getBoundingBox(i+z, j+y, k+x, i+z2, j+y2, k+x2);
			case 2:
				return AxisAlignedBB.getBoundingBox(i+1-x2, j+y, k+1-z2, i+1-x,  j+y2, k+1-z);
			case 4:
				return AxisAlignedBB.getBoundingBox(i+1-z2, j+y, k+1-x2, i+1-z, j+y2, k+1-x);
			case 3:
				return AxisAlignedBB.getBoundingBox(i+x, j+y, k+z, i+x2, j+y2, k+z2);
		}
        return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }
    
    @Override
    @SideOnly (Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) 
    {
    	int meta = world.getBlockMetadata(i, j, k);
		float ringMin = 2.0F/16.0F;
		float ringMax = 14.0F/16.0F;
        float px = 1.0F/16.0F;
		float x = ringMin;
		float y = ringMin;
		float z = 0.0F;
		float x2 = ringMax;
		float y2 = ringMax;
		float z2 = 0.999F;
		ForgeDirection dir = ForgeDirection.getOrientation(meta).getOpposite();        switch (meta) {
        	case 0:
        		this.setBlockBounds(x, z, y, x2, z2, y2);
        		break;
        	case 1:
        		this.setBlockBounds(x, z, y, x2, z2, y2);
        		break;
			case 5:
				this.setBlockBounds(z, y, x, z2, y2, x2);
				break;
			case 2:
				this.setBlockBounds(1-x2, y, 1-z2, 1-x, y2, 1-z);
				break;
			case 4:
				this.setBlockBounds(1-z2, y, 1-x2, 1-z, y2, 1-x);
				break;
			case 3:
				this.setBlockBounds(x, y, z, x2, y2, z2);
				break;
		}
		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int xl, int yl, int zl)
    {
        int meta = world.getBlockMetadata(xl, yl, zl);
		float ringMin = 4.0F/16.0F;
		float ringMax = 12.0F/16.0F;
        float px = 1.0F/16.0F;
		float x = ringMin;
		float y = ringMin;
		float z = 0.0F;
		float x2 = ringMax;
		float y2 = ringMax;
		float z2 = 0.999F;
		ForgeDirection dir = ForgeDirection.getOrientation(meta).getOpposite();
		this.setBlockBounds(z, y, x, z2, y2, x2);
        switch (meta) {
        	case 0:
        		this.setBlockBounds(x, z, y, x2, z2, y2);
        		break;
        	case 1:
        		this.setBlockBounds(x, z, y, x2, z2, y2);
        		break;
			case 5:
				this.setBlockBounds(z, y, x, z2, y2, x2);
				break;
			case 2:
				this.setBlockBounds(1-x2, y, 1-z2, 1-x, y2, 1-z);
				break;
			case 4:
				this.setBlockBounds(1-z2, y, 1-x2, 1-z, y2, 1-x);
				break;
			case 3:
				this.setBlockBounds(x, y, z, x2, y2, z2);
				break;
		}
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blockBrass");
        this.sideIcon = p_149651_1_.registerIcon("steamcraft:blockBrass" + "_pipe");

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
		return new TileEntityFluidSteamConverter();
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }
    
	@Override
	public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float xO, float yO, float zO) {
		int meta = world.getBlockMetadata(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, side == meta ? ForgeDirection.getOrientation(side).getOpposite().ordinal() : side, 2);
        return true;
	}
}
