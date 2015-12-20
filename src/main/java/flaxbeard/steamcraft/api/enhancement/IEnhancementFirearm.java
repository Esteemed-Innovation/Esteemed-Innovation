package flaxbeard.steamcraft.api.enhancement;

import flaxbeard.steamcraft.entity.EntityMusketBall;
import net.minecraft.item.Item;

public interface IEnhancementFirearm extends IEnhancement {
    float getAccuracyChange(Item weapon);

    float getKnockbackChange(Item weapon);

    float getDamageChange(Item weapon);

    int getReloadChange(Item weapon);

    int getClipSizeChange(Item weapon);

    /**
     * Called to make the upgrade use shots
     * This can be used to change features of the base EntityMusketBall bullet
     */
    EntityMusketBall changeBullet(EntityMusketBall bullet);
}
