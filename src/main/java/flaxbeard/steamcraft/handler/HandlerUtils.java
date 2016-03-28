package flaxbeard.steamcraft.handler;

import flaxbeard.steamcraft.item.ItemExosuitArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HandlerUtils {
	public static boolean hasPower(EntityLivingBase entityLiving, int i) {
		if (entityLiving.getEquipmentInSlot(3) != null) {
			ItemStack chestStack = entityLiving.getEquipmentInSlot(3);
			if (chestStack.getItem() instanceof ItemExosuitArmor) {
				return ((ItemExosuitArmor) chestStack.getItem()).hasPower(chestStack, i);
			}
		}
		return false;
	}

	public static void drainSteam(ItemStack stack, int amount) {
		if (stack != null) {
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}
			if (!stack.stackTagCompound.hasKey("steamFill")) {
				stack.stackTagCompound.setInteger("steamFill", 0);
			}
			int fill = stack.stackTagCompound.getInteger("steamFill");
			fill = Math.max(0, fill - amount);
			stack.stackTagCompound.setInteger("steamFill", fill);
		}
	}
}
