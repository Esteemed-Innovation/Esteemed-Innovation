package eiteam.esteemedinnovation.api.enhancement;

import eiteam.esteemedinnovation.api.entity.EntityRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface EnhancementRocketLauncher extends Enhancement {
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

    @Override
    default void afterRoundFired(ItemStack weaponStack, World world, EntityPlayer player) {
        if (player.capabilities.isFlying && !player.onGround && weaponStack.hasTagCompound()) {
            int timeBetweenFire = weaponStack.getTagCompound().getInteger("fireDelay");
            weaponStack.getTagCompound().setInteger("fireDelay", timeBetweenFire + getFireDelayChange(weaponStack));
        }
    }
}
