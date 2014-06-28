package flaxbeard.steamcraft.item.firearm;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.entity.EntityMusketBall;

public class ItemEnhancementFireMusket extends Item implements IEnhancementFirearm {

	@Override
	public boolean canApplyTo(ItemStack stack) {
		return stack.getItem() == SteamcraftItems.musket || stack.getItem() == SteamcraftItems.blunderbuss;
	}

	@Override
	public int cost(ItemStack stack) {
		return 8;
	}

	@Override
	public String getID() {
		return "fireMusket";
	}

	@Override
	public String getIcon(Item item) {
		if (item == SteamcraftItems.musket) {
			return "steamcraft:weaponMusketAblaze";
		}
		else
		{
			return "steamcraft:weaponBlunderbussAblaze";
		}
	}

	@Override
	public String getName(Item item) {
		if (item == SteamcraftItems.musket) {
			return "item.steamcraft:musketAblaze";
		}
		else
		{
			return "item.steamcraft:blunderbussAblaze";
		}
	}
	
	
	@Override
	public String getEnhancementName(Item item) {
		return "enhancement.steamcraft:musketAblaze";
	}

	@Override
	public float getAccuracyChange(Item weapon) {
		return 0;
	}

	@Override
	public float getKnockbackChange(Item weapon) {
		return 0;
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
		bullet.setFire(100);
		return bullet;
	}

}
