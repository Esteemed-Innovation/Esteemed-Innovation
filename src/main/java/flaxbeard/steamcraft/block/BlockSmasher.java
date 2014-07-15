package flaxbeard.steamcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySmasher;



public class BlockSmasher extends BlockSteamTransporter {
	
	@SideOnly(Side.CLIENT)
	public IIcon frontIcon;
	

	public BlockSmasher(){
		super(Material.iron);
		
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (l == 1)
        {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (l == 2)
        {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3)
        {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }

        
    }
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
    {
		if (meta == 0) {
    		meta = 3;
    	}
        return this.blockIcon;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon("steamcraft:blankTexture");
    }
	
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntitySmasher();
		
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor){
		TileEntitySmasher smasher = (TileEntitySmasher)world.getTileEntity(x, y, z);
		smasher.blockUpdate();
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	
}
