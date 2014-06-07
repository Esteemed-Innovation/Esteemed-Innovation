package flaxbeard.steamcraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.tile.TileEntityMold;

public class BlockMold extends BlockContainer {
	private static float px = (1.0F/16.0F);
	public IIcon blank;
	public BlockMold() {
		super(Material.rock);
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
    
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
    {
    	setBlockBounds( 2*px,0.0F, 2*px, 1.0F-2*px, 1.0F-8*px, 1.0F-2*px);
    	super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
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
				if (tile.mold != null) {
					if (!world.isRemote) {
						tile.dropItem(tile.mold);
					}
					tile.mold = null;
				}
				if (player.getHeldItem() != null) {
					if (player.getHeldItem().getItem() instanceof ICrucibleMold) {
						tile.mold = player.getHeldItem();
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}
				}
			}
		}
		else
		{
			if (tile.changeTicks == 0 && (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBlock))) {
				tile.open = !tile.open;
				tile.changeTicks = 20;
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
