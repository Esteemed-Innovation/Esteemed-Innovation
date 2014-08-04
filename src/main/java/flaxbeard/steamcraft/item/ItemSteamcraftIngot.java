package flaxbeard.steamcraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.entity.EntityMortarItem;

public class ItemSteamcraftIngot extends Item {
	public IIcon[] icon = new IIcon[4];
	
	public ItemSteamcraftIngot() {
		this.setHasSubtypes(true);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int par1)
	{
		if (par1 < icon.length) {
			return this.icon[par1];
		}
		return this.icon[0];
	}
	 
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir)
	{
		this.icon[0] = ir.registerIcon("steamcraft:ingotCopper");
		this.icon[1] = ir.registerIcon("steamcraft:ingotZinc");
		this.icon[2] = ir.registerIcon("steamcraft:ingotBrass");
		this.icon[3] = ir.registerIcon("steamcraft:gilded");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
	}
}
