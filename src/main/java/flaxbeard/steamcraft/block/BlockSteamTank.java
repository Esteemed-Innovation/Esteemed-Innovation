package flaxbeard.steamcraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityCreativeTank;
import flaxbeard.steamcraft.tile.TileEntitySteamTank;

public class BlockSteamTank extends BlockSteamTransporter {

	public BlockSteamTank() {
		super(Material.iron);
	}
	
    public int damageDropped(int meta)
    {
        return meta;
    }
	
	public IIcon[] icon = new IIcon[2];
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int par1, int par2)
	{
		if (par2 == 0) {
			return this.icon[0];
		}
		if (par2 == 1) {
			return this.icon[1];
	    }
			return this.icon[0];
	}
	 
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir)
	{
		this.icon[0] = ir.registerIcon("steamcraft:brassTank");
		this.icon[1] = ir.registerIcon("steamcraft:creativeTank");
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		if (var2 == 1) {
			return new TileEntityCreativeTank();
		}
		else
		{
			return new TileEntitySteamTank();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
	}

}
