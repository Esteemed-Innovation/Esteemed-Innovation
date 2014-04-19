package flaxbeard.steamcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.entity.EntityMusketBall;

public class ItemEnhancementFireMusket extends Item implements IEnhancementFirearm {

	@Override
	public boolean canApplyTo(ItemStack stack) {
		return stack.getItem() == SteamcraftItems.musket;
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
		return "steamcraft:weaponMusketAblaze";
	}

	@Override
	public String getName(Item item) {
		return "item.steamcraft:musketAblaze";
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
