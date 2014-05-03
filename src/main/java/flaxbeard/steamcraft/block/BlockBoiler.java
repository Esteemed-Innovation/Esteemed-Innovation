package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.tile.TileEntityBoiler;

public class BlockBoiler extends BlockContainer {

	public BlockBoiler(boolean on) {
		super(Material.iron);
        this.field_149932_b = on;
	}
    private final boolean field_149932_b;
    private static boolean field_149934_M;
    @SideOnly(Side.CLIENT)
    private IIcon field_149935_N;
    @SideOnly(Side.CLIENT)
    private IIcon field_149936_O;
    
    public static void updateFurnaceBlockState(boolean p_149931_0_, World p_149931_1_, int p_149931_2_, int p_149931_3_, int p_149931_4_)
    {
        int l = p_149931_1_.getBlockMetadata(p_149931_2_, p_149931_3_, p_149931_4_);
        TileEntity tileentity = p_149931_1_.getTileEntity(p_149931_2_, p_149931_3_, p_149931_4_);
        field_149934_M = true;

        if (p_149931_0_)
        {
            p_149931_1_.setBlock(p_149931_2_, p_149931_3_, p_149931_4_, SteamcraftBlocks.boilerOn);
        }
        else
        {
            p_149931_1_.setBlock(p_149931_2_, p_149931_3_, p_149931_4_, SteamcraftBlocks.boiler);
        }

        field_149934_M = false;
        p_149931_1_.setBlockMetadataWithNotify(p_149931_2_, p_149931_3_, p_149931_4_, l, 2);

        if (tileentity != null)
        {
            tileentity.validate();
            p_149931_1_.setTileEntity(p_149931_2_, p_149931_3_, p_149931_4_, tileentity);
        }
    }
	
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        this.func_149930_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }

    private void func_149930_e(World p_149930_1_, int p_149930_2_, int p_149930_3_, int p_149930_4_)
    {
        if (!p_149930_1_.isRemote)
        {
            Block block = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ - 1);
            Block block1 = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ + 1);
            Block block2 = p_149930_1_.getBlock(p_149930_2_ - 1, p_149930_3_, p_149930_4_);
            Block block3 = p_149930_1_.getBlock(p_149930_2_ + 1, p_149930_3_, p_149930_4_);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j())
            {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j())
            {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j())
            {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j())
            {
                b0 = 4;
            }

            p_149930_1_.setBlockMetadataWithNotify(p_149930_2_, p_149930_3_, p_149930_4_, b0, 2);
        }
    }
	
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
    	if (p_149691_2_ == 0) {
    		p_149691_2_ = 3;
    	}
        return p_149691_1_ == 1 ? this.field_149935_N : (p_149691_1_ == 0 ? this.field_149935_N : (p_149691_1_ != p_149691_2_ ? this.blockIcon : this.field_149936_O));
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blockBrass");
        this.field_149936_O = p_149651_1_.registerIcon(this.field_149932_b ? "steamcraft:boilerOn" : "steamcraft:boiler");
        this.field_149935_N = p_149651_1_.registerIcon("steamcraft:blockBrass");
    }
	
   public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int l = MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 2, 2);
        }

        if (l == 1)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 5, 2);
        }

        if (l == 2)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 3, 2);
        }

        if (l == 3)
        {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 4, 2);
        }

        if (p_149689_6_.hasDisplayName())
        {
            ((TileEntityFurnace)p_149689_1_.getTileEntity(p_149689_2_, p_149689_3_, p_149689_4_)).func_145951_a(p_149689_6_.getDisplayName());
        }
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBoiler();
	}
	
	@Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityBoiler tileentityfurnace = (TileEntityBoiler)par1World.getTileEntity(par2, par3, par4);

            if (tileentityfurnace != null)
            {
            	par5EntityPlayer.openGui(Steamcraft.instance, 0, par1World, par2,par3,par4);
            }

            return true;
        }
    }

}
