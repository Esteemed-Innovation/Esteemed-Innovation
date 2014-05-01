package flaxbeard.steamcraft.item.tool;

import java.util.Set;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableSet;

import flaxbeard.steamcraft.api.UtilMisc;

public class ItemSteamcraftShovel extends ItemSpade {
	private int harvestLevel;
	private Object repairMaterial;
	public ItemSteamcraftShovel(ToolMaterial p_i45347_1_, Object rM) {
		super(p_i45347_1_);
		harvestLevel = p_i45347_1_.getHarvestLevel();
		repairMaterial = rM;
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("shovel");
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return harvestLevel;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		if (repairMaterial instanceof ItemStack) {
			return par2ItemStack.isItemEqual((ItemStack) repairMaterial) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
		}
		if (repairMaterial instanceof String) {
			return UtilMisc.doesMatch(par2ItemStack, (String) repairMaterial) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
		}
		return super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

}
