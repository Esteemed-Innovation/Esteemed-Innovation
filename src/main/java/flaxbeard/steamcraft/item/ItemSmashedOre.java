package flaxbeard.steamcraft.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class ItemSmashedOre extends Item {
	
	
	public IIcon theOverlay;
	public static ArrayList<ImmutablePair<String,ImmutablePair<Integer,String>>> oreTypes = new ArrayList<ImmutablePair<String,ImmutablePair<Integer,String>>>();
	private static final int ironColor = 0xEEC1A4;
	private static final int zincColor = 0xD0D0D2;
	
	public ItemSmashedOre(){
		super();
		this.setHasSubtypes(true);
		
		oreTypes.add(getPair("oreIron", 0xEEC1A4, "iron"));
		oreTypes.add(getPair("oreGold", 0xF4CC00, "gold"));
		oreTypes.add(getPair("oreCopper", 0xC14B36, "copper"));
		oreTypes.add(getPair("oreZinc", 0xD0D0d2, "zinc"));
		
		// make sure we don't include things that don't exist
		oreTypes.add(getPair("oreSnozberry",0xff00ff, "snozberry"));
		
	}
	
	private ImmutablePair<String, ImmutablePair<Integer,String>> getPair(String oreDict, Integer colorHex, String uName){
		return new ImmutablePair<String, ImmutablePair<Integer, String>>(oreDict, new ImmutablePair<Integer, String>(colorHex, uName));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		
		//par3List.add(new ItemStack(par1, 1, 0));
		//par3List.add(new ItemStack(par1, 1, 1));
		//par3List.add(new ItemStack(par1, 1, 2));
		//par3List.add(new ItemStack(par1, 1, 3));
		for (int i = 0; i < oreTypes.size(); i++){
			if (OreDictionary.getOres(oreTypes.get(i).getLeft()).size() > 0){
				list.add(new ItemStack(item, 1, i));
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + oreTypes.get(stack.getItemDamage()).getRight().getRight();
	}
	
	
	@SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
	
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
		return renderPass == 0 ? 0xffffff : oreTypes.get((stack.getItemDamage())).getRight().getLeft().intValue();
    }
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 > 0 ? this.theOverlay : super.getIconFromDamageForRenderPass(par1, par2);
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        this.theOverlay = par1IconRegister.registerIcon(this.getIconString() + "_overlay");
    }
	
	
	
	
	
	

}
