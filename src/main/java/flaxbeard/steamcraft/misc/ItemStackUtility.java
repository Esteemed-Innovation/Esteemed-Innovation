package flaxbeard.steamcraft.misc;

import net.minecraft.item.ItemStack;

public class ItemStackUtility {
	/**
	 * "Mostly" equal; returns true if the item and meta are the same (don't care about stacksize).
	 * 
	 * @param stack1
	 * @param stack2
	 * @return
	 */
	public static boolean areItemStacksMostlyEqual(ItemStack stack1, ItemStack stack2){
		return stack1.getItem().equals(stack2.getItem()) && stack1.getItemDamage() == stack2.getItemDamage();
	}
}
