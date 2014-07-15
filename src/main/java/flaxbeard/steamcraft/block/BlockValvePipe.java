package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import flaxbeard.steamcraft.tile.TileEntityValvePipe;

public class BlockValvePipe extends BlockPipe {
	
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
    }
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_)
    {
        boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
	    if ((tileEntity != null && tileEntity instanceof TileEntityValvePipe))
	    {
	    	TileEntityValvePipe ped = (TileEntityValvePipe)tileEntity;
	    	ped.updateRedstoneState(flag);
	    }
    }
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityValvePipe();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBlock)) {
			TileEntityValvePipe tile = (TileEntityValvePipe) world.getTileEntity(x, y, z);
			if (!tile.isTurning()) {
				tile.setTurining();
				System.out.println(tile.getNetworkName());
			}
		}
		return true;
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
}
