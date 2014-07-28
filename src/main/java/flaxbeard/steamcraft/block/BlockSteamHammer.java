package flaxbeard.steamcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;

public class BlockSteamHammer extends BlockContainer
{
    @SideOnly(Side.CLIENT)
	public IIcon top;
    private IIcon bottom;

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
	{
        int l = MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}

    public BlockSteamHammer()
    {
        super(Material.iron);
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int xp, int yp, int zp)
    {
    	int meta = blockAccess.getBlockMetadata(xp, yp, zp);
    	float px = 1.0F/16.0F;
		float x = 0;
		float y = 0;
		float z = 5*px;
		float x2 = 15*px;
		float y2 = 12*px;
		float z2 = 11*px;
		switch (meta) {
		case 0:
			this.setBlockBounds(z, y, x, z2, y2, x2);
			break;
		case 1:
			this.setBlockBounds(1-x2, y, 1-z2, 1-x, y2, 1-z);
			break;
		case 2:	
			this.setBlockBounds(1-z2, y, 1-x2, 1-z, y2, 1-x);
			break;
		case 3:
			this.setBlockBounds(x, y, z, x2, y2, z2);
			break;
		
		}
    }
//    
//	@Override
//	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
//	{
//		TileEntitySteamCharger tile = (TileEntitySteamCharger) world.getTileEntity(x,y,z);
//		if (tile.getStackInSlot(0) != null) {
//			if (!world.isRemote ) {
//				tile.dropItem(tile.getStackInSlot(0));
//			}
//			tile.setInventorySlotContents(0, null);
//		}
//		else
//		{
//			if (player.getHeldItem() != null) {
//				if (player.getHeldItem().getItem() instanceof ISteamChargable) {
//					ISteamChargable item = (ISteamChargable) player.getHeldItem().getItem();
//					if (item.canCharge(player.getHeldItem())) {
//						ItemStack copy = player.getCurrentEquippedItem().copy();
//						copy.stackSize = 1;
//						tile.setInventorySlotContents(0, copy);
//						player.getCurrentEquippedItem().stackSize -= 1;
//						tile.randomDegrees = world.rand.nextInt(361);
//					}
//				}
//			}
//		}
//		return false;
//	}
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	if (world.getBlock(x, y-1, z) != null) {
        	if (world.getBlock(x, y-1, z) == Blocks.anvil) {
                if (world.isRemote)
                {
                    return true;
                }
                else
                {
                	TileEntitySteamHammer tileentityfurnace = (TileEntitySteamHammer)world.getTileEntity(x, y, z);
                    if (tileentityfurnace != null)
                    {
                    	player.openGui(Steamcraft.instance, 3, world, x,y,z);
                    }
                    return true;
                }
        	}
    	}
    	return false;
    }
	
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
		return new TileEntitySteamHammer();
	}
	
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
    	TileEntitySteamHammer tileentitysteamcharger = (TileEntitySteamHammer)p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
        if (tileentitysteamcharger != null)
        {
            for (int i1 = 0; i1 < tileentitysteamcharger.getSizeInventory()-1; ++i1)
            {
                ItemStack itemstack = tileentitysteamcharger.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = this.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(p_149749_1_, (double)((float)p_149749_2_ + f), (double)((float)p_149749_3_ + f1), (double)((float)p_149749_4_ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                        p_149749_1_.spawnEntityInWorld(entityitem);
                    }
                }
            }

            p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
        }
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }
    private final Random rand = new Random();

}