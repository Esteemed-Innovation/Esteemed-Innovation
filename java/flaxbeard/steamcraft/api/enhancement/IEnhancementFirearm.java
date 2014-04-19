package flaxbeard.steamcraft.api.enhancement;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import flaxbeard.steamcraft.entity.EntityMusketBall;

public interface IEnhancementFirearm extends IEnhancement {
	public float getAccuracyChange(Item weapon);
	public float getKnockbackChange(Item weapon);
	public float getDamageChange(Item weapon);
	public int getReloadChange(Item weapon);
	public int getClipSizeChange(Item weapon);
	public EntityMusketBall changeBullet(EntityMusketBall bullet);
}
