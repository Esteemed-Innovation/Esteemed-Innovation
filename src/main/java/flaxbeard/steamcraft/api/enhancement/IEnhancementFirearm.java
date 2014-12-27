package flaxbeard.steamcraft.api.enhancement;

import flaxbeard.steamcraft.entity.EntityMusketBall;
import net.minecraft.item.Item;

public interface IEnhancementFirearm extends IEnhancement {
    public float getAccuracyChange(Item weapon);

    public float getKnockbackChange(Item weapon);

    public float getDamageChange(Item weapon);

    public int getReloadChange(Item weapon);

    public int getClipSizeChange(Item weapon);

    /**
     * Called to make the upgrade use shots
     * This can be used to change features of the base EntityMusketBall bullet
     */
    public EntityMusketBall changeBullet(EntityMusketBall bullet);
}
