package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;

public class BlockSteamCharger extends BlockContainer
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

    public BlockSteamCharger()
    {
        super(Material.iron);
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
    	float px = 1.0F/16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }
    
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		TileEntitySteamCharger tile = (TileEntitySteamCharger) world.getTileEntity(x,y,z);
		if (tile.getStackInSlot(0) != null) {
			if (!world.isRemote ) {
				tile.dropItem(tile.getStackInSlot(0));
			}
			tile.setInventorySlotContents(0, null);
		}
		else
		{
			if (player.getHeldItem() != null) {
				if (player.getHeldItem().getItem() instanceof ISteamChargable) {
					ISteamChargable item = (ISteamChargable) player.getHeldItem().getItem();
					if (item.canCharge(player.getHeldItem())) {
						ItemStack copy = player.getCurrentEquippedItem().copy();
						copy.stackSize = 1;
						tile.setInventorySlotContents(0, copy);
						player.getCurrentEquippedItem().stackSize -= 1;
						tile.randomDegrees = world.rand.nextInt(361);
					}
				}
			}
		}
		return false;
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.bottom : (p_149691_1_ == 0 ? this.bottom : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blockCharger");
        this.top =  p_149651_1_.registerIcon("steamcraft:blockChargerTop");
        this.bottom = p_149651_1_.registerIcon("steamcraft:blockBrass");
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySteamCharger();
	}
	
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return Steamcraft.chargerRenderID;
    }
}