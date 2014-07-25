package flaxbeard.steamcraft.item.tool;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.api.UtilMisc;

public class ItemSteamcraftArmor extends ItemArmor {
	private Object repairMaterial;
	protected String name;
	
	public ItemSteamcraftArmor(ArmorMaterial p_i45325_1_, int p_i45325_2_,
			int p_i45325_3_, Object repair, String n) {
		super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
		this.repairMaterial = repair;
		this.name = n;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		if (name != "Gilded") {
			if (slot == 2) {
				return "steamcraft:textures/models/armor/" + this.name.substring(0, 1).toLowerCase() + this.name.substring(1) + "_2.png";
			}
			return "steamcraft:textures/models/armor/" + this.name.substring(0, 1).toLowerCase() + this.name.substring(1) + "_1.png";
		}
		else
		{
			if (slot == 2) {
				return "minecraft:textures/models/armor/gold_layer_2.png";
			}
			return "minecraft:textures/models/armor/gold_layer_1.png";
		}
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
