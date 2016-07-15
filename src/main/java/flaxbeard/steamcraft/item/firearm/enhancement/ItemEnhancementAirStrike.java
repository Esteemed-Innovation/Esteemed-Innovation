package flaxbeard.steamcraft.item.firearm.enhancement;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.enhancement.IEnhancementRocketLauncher;
import flaxbeard.steamcraft.entity.projectile.EntityRocket;
import flaxbeard.steamcraft.init.items.firearms.FirearmItems;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemEnhancementAirStrike extends Item implements IEnhancementRocketLauncher {
    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == FirearmItems.Items.ROCKET_LAUNCHER.getItem();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return Steamcraft.upgrade;
    }

    @Override
    public String getID() {
        return "airStrike";
    }

    @Override
    public ResourceLocation getIcon(Item item) {
        return new ResourceLocation(Steamcraft.MOD_ID, "weaponRocketLauncherAirStrike");
    }

    @Override
    public String getName(Item item) {
        return "item.steamcraft:rocketLauncherAirStrike";
    }


    @Override
    public float getAccuracyChange(Item weapon) {
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
