package flaxbeard.steamcraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.MutablePair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.Tuple3;
import flaxbeard.steamcraft.tile.TileEntityCrucible;

public class BlockSteamcraftCrucible extends BlockContainer {
	
	public IIcon innerIcon;
	public IIcon topIcon;
	public IIcon bottomIcon;
	public IIcon liquidIcon;
	public IIcon blank;
	private static float px = (1.0F/16.0F);

	public BlockSteamcraftCrucible() {
		super(Material.rock);
	}
	
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
    	return AxisAlignedBB.getBoundingBox(i+px, j+0.0F+px, k+px, i+1.0F-px, j+1.0F-px, k+1.0F-px);
    }

	
	
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {

        if (entity instanceof EntityItem) {
        	EntityItem item = (EntityItem) entity;
        	if (world.getBlock(x, y-1, z) == Blocks.fire || world.getBlock(x, y-1, z).getMaterial() == Material.lava) {
        		MutablePair output;
        		if (SteamcraftRegistry.smeltThings.containsKey(MutablePair.of(item.getEntityItem().getItem(),item.getEntityItem().getItemDamage()))) {
        			output = SteamcraftRegistry.smeltThings.get(MutablePair.of(item.getEntityItem().getItem(),item.getEntityItem().getItemDamage()));
        		}
        		else if (SteamcraftRegistry.smeltThings.containsKey(MutablePair.of(item.getEntityItem().getItem(),-1))) {
        			output = SteamcraftRegistry.smeltThings.get(MutablePair.of(item.getEntityItem().getItem(),-1));
        			
        		}
        		else
        		{
        			return;
        		}
        		TileEntityCrucible crucible = (TileEntityCrucible) world.getTileEntity(x, y, z);
        		int amount = (Integer) output.right;
        		if (crucible != null) {
        			ItemStack stack = item.getEntityItem();
    				ItemStack out = crucible.fillWith(stack, amount, output);
    				
    				if (out.stackSize <= 0){
    					entity.setDead();
    				} else {
    					item.setEntityItemStack(out);
    				}
        		}
        	}
        }
    }
	
	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
	{
        int l = MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
    {
    	setBlockBounds(px, 0.0F+px, px, 1.0F-px, 1.0F-px, 1.0F-px);
    	super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return blank;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.innerIcon = p_149651_1_.registerIcon(this.getTextureName() + "_" + "inner");
        this.topIcon = p_149651_1_.registerIcon(this.getTextureName() + "_top");
        this.bottomIcon = p_149651_1_.registerIcon(this.getTextureName() + "_" + "bottom");
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        this.liquidIcon = p_149651_1_.registerIcon(this.getTextureName() + "_liquid");
        this.blank =  p_149651_1_.registerIcon("steamcraft:blankTexture");
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityCrucible();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if (!player.isSneaking() && player.getHeldItem() == null) {
			TileEntityCrucible tile = (TileEntityCrucible) world.getTileEntity(x, y, z);
			if (!tile.isTipping()) {
				tile.setTipping();
				tile.needsUpdate = true;
			}
		}
		else if (player.getHeldItem() != null) {
			TileEntityCrucible tile = (TileEntityCrucible) world.getTileEntity(x, y, z);
			for (CrucibleLiquid liquid : tile.contents) {
				Tuple3 tuple = new Tuple3(player.getHeldItem().getItem(),player.getHeldItem().getItemDamage(),liquid);
				if (SteamcraftRegistry.dunkThings.get(tuple) != null) {
					int needed = SteamcraftRegistry.dunkThings.get(tuple).left;
					ItemStack result = SteamcraftRegistry.dunkThings.get(tuple).right.copy();
					if (tile.number.get(liquid) >= needed) {
						player.inventory.decrStackSize(player.inventory.currentItem,1);
						int currNum = tile.number.get(liquid);
						currNum -= needed;
						if (currNum == 0) {
							tile.contents.remove(liquid);
						}
						tile.number.remove(liquid);
						tile.number.put(liquid, currNum);
						if (!player.inventory.addItemStackToInventory(result)) {
							if (!player.worldObj.isRemote) {
								player.entityDropItem(result,0.0F);
							}
						}
						tile.needsUpdate = true;

						break;
					}
				}
				else if (SteamcraftRegistry.dunkThings.get(new Tuple3(player.getHeldItem().getItem(),-1,liquid)) != null) {
					tuple = new Tuple3(player.getHeldItem().getItem(),-1,liquid);
					int needed = SteamcraftRegistry.dunkThings.get(tuple).left;
					ItemStack result = SteamcraftRegistry.dunkThings.get(tuple).right.copy();
					if (tile.number.get(liquid) >= needed) {
						player.inventory.decrStackSize(player.inventory.currentItem,1);
						int currNum = tile.number.get(liquid);
						currNum -= needed;
						if (currNum == 0) {
							tile.contents.remove(liquid);
						}
						tile.number.remove(liquid);
						tile.number.put(liquid, currNum);
						if (!player.inventory.addItemStackToInventory(result)) {
							if (!player.worldObj.isRemote) {
								player.entityDropItem(result,0.0F);
							}
						}
						tile.needsUpdate = true;

						break;
					}
				}
			}
		}
		return true;
	}

}
