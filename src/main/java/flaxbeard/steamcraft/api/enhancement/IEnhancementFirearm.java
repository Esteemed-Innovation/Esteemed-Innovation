package flaxbeard.steamcraft.api.enhancement;

import flaxbeard.steamcraft.common.entity.EntityMusketBall;
import net.minecraft.item.Item;

public interface IEnhancementFirearm extends IEnhancement {
    public float getAccuracyChange(Item weapon);

    public float getKnockbackChange(Item weapon);

    public float getDamageChange(Item weapon);

    public int getReloadChange(Item weapon);

    public int getClipSizeChange(Item weapon);

    public EntityMusketBall changeBullet(EntityMusketBall bullet);
}
