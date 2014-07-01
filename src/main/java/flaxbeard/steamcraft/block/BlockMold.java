package flaxbeard.steamcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
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
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.tile.TileEntityMold;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;

public class BlockMold extends BlockContainer {
	private static float px = (1.0F/16.0F);
	public IIcon blank;
    private final Random rand = new Random();

	
	public BlockMold() {
		super(Material.rock);
	}

   public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
   {
    	TileEntityMold tileentitymold = (TileEntityMold)p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

        if (tileentitymold != null)
        {
            for (int i1 = 0; i1 < tileentitymold.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileentitymold.getStackInSlot(i1);

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
                itemstack = tileentitymold.mold[0];

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


	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityMold();
	}
	
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
	{
        int l = MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return blank;
        
    }
	
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
    {
    	setBlockBounds( 2*px,0.0F, 2*px, 1.0F-2*px, 1.0F-8*px, 1.0F-2*px);
    	super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
    }
    
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
    	return AxisAlignedBB.getBoundingBox( i+ 2*px,j +0.0F, k + 2*px, i + 1.0F-2*px, j + 1.0F-8*px,k +1.0F-2*px);
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		TileEntityMold tile = (TileEntityMold) world.getTileEntity(x, y, z);
		boolean editingMold = false;
		if (player.getHeldItem() != null) {
			if (player.getHeldItem().getItem() instanceof ICrucibleMold) {
				editingMold = true;
			}
		}
		if (player.isSneaking()) {
			editingMold = true;
		}
		if (editingMold) {
			if (tile.open) {
				if (tile.mold[0] != null) {
					if (!world.isRemote) {
						tile.dropItem(tile.mold[0]);
					}
					tile.mold[0] = null;
					world.markBlockForUpdate(x, y, z);

				}
				if (player.getHeldItem() != null) {
					if (player.getHeldItem().getItem() instanceof ICrucibleMold) {
						tile.mold[0] = player.getHeldItem();
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
						world.markBlockForUpdate(x, y, z);

					}
				}
			}
		}
		else
		{
			if (tile.changeTicks == 0 && (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBlock))) {
				tile.open = !tile.open;
				tile.changeTicks = 20;
				world.markBlockForUpdate(x, y, z);

			}
			
		}
	
	return false;
	}
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blank =  p_149651_1_.registerIcon("steamcraft:blankTexture");
    }


}
