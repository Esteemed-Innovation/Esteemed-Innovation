package flaxbeard.steamcraft.item.firearm;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.entity.EntityMusketBall;

public class ItemEnhancementRecoil extends Item implements IEnhancementFirearm {

	@Override
	public boolean canApplyTo(ItemStack stack) {
		return stack.getItem() == SteamcraftItems.blunderbuss;
	}

	@Override
	public int cost(ItemStack stack) {
		return 8;
	}

	@Override
	public String getID() {
		return "Recoil";
	}

	@Override
	public String getIcon(Item item) {
		return "steamcraft:weaponBlunderbussRecoil";
	}

	@Override
	public String getName(Item item) {
		return "item.steamcraft:blunderbussRecoil";
	}
	
	
	@Override
	public String getEnhancementName(Item item) {
		return "enhancement.steamcraft:musketAblaze";
	}

	@Override
	public float getAccuracyChange(Item weapon) {
		return -1.0F;
	}

	@Override
	public float getKnockbackChange(Item weapon) {
		return -2.0F;
	}

	@Override
	public float getDamageChange(Item weapon) {
		return 0;
	}

	@Override
	public int getReloadChange(Item weapon) {
		return 0;
	}

	@Override
	public int getClipSizeChange(Item weapon) {
		return 0;
	}

	@Override
	public EntityMusketBall changeBullet(EntityMusketBall bullet) {
		return bullet;
	}

}
