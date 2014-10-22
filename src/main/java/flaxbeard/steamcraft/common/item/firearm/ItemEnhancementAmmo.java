package flaxbeard.steamcraft.common.item.firearm;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.enhancement.IEnhancementRocketLauncher;
import flaxbeard.steamcraft.common.entity.EntityRocket;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemEnhancementAmmo extends Item implements IEnhancementRocketLauncher {

    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == SteamcraftItems.rocketLauncher;
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return Steamcraft.upgrade;
    }

    @Override
    public int cost(ItemStack stack) {
        return 8;
    }

    @Override
    public String getID() {
        return "ammo";
    }

    @Override
    public String getIcon(Item item) {
        return "steamcraft:weaponRocketLauncherAmmo";
    }

    @Override
    public String getName(Item item) {
        return "item.steamcraft:rocketLauncherAmmo";
    }


    @Override
    public String getEnhancementName(Item item) {
        return "enhancement.steamcraft:rocketLauncherAmmo";
    }

    @Override
    public float getAccuracyChange(Item weapon) {
        return 0;
    }

    @Override
    public int getReloadChange(Item weapon) {
        return 20;
    }

    @Override
    public int getClipSizeChange(Item weapon) {
        return 2;
    }

    @Override
    public float getExplosionChange(Item weapon) {
        return 0;
    }

    @Override
    public int getFireDelayChange(ItemStack weapon) {
        return 0;
    }

    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return bullet;
    }

}
