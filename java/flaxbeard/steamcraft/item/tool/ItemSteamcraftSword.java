package flaxbeard.steamcraft.item.tool;

import flaxbeard.steamcraft.api.UtilMisc;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;

public class ItemSteamcraftSword extends ItemSword {
	private int harvestLevel;
	private Object repairMaterial;
	public ItemSteamcraftSword(ToolMaterial p_i45347_1_, Object rM) {
		super(p_i45347_1_);
		harvestLevel = p_i45347_1_.getHarvestLevel();
		repairMaterial = rM;
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
