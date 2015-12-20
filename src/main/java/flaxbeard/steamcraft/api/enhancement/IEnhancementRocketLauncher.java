package flaxbeard.steamcraft.api.enhancement;

import flaxbeard.steamcraft.entity.EntityRocket;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IEnhancementRocketLauncher extends IEnhancement {
    float getAccuracyChange(Item weapon);

    float getExplosionChange(Item weapon);

    int getReloadChange(Item weapon);

    int getFireDelayChange(ItemStack weapon);

    int getClipSizeChange(Item weapon);

    /**
    * Called to make the upgrade use shots
    * This can be used to change features of the base EntityMusketBall bullet
    */
    EntityRocket changeBullet(EntityRocket bullet);
}
