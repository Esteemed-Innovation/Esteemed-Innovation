package flaxbeard.steamcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityItemMortar;

public class BlockItemMortar extends BlockSteamTransporter
{

    public BlockItemMortar()
    {
        super(Material.iron);
    }
    
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityItemMortar();
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		TileEntityItemMortar tile = (TileEntityItemMortar) world.getTileEntity(x,y,z);
	
		if (player.getHeldItem() != null) {
			ItemStack item = player.getHeldItem();
			if (item.getItem() == SteamcraftItems.astrolabe) {
				if (item.hasTagCompound() && item.stackTagCompound.hasKey("targetX")) {
					if (world.provider.dimensionId == item.stackTagCompound.getInteger("dim")) {
						tile.xT = item.stackTagCompound.getInteger("targetX");
						tile.zT = item.stackTagCompound.getInteger("targetZ");
						tile.hasTarget = true;
					}
				}
			}
		}
		return false;
	}	
	
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
    	TileEntityItemMortar tileentitysteamcharger = (TileEntityItemMortar)p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
        if (tileentitysteamcharger != null)
        {
            for (int i1 = 0; i1 < tileentitysteamcharger.getSizeInventory(); ++i1)
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