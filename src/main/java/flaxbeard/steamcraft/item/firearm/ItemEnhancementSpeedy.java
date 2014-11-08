package flaxbeard.steamcraft.item.firearm;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.entity.EntityMusketBall;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemEnhancementSpeedy extends Item implements IEnhancementFirearm {

    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == SteamcraftItems.pistol;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return Steamcraft.upgrade;
    }

    @Override
    public int cost(ItemStack stack) {
        return 8;
    }

    @Override
    public String getID() {
        return "Speedy";
    }

    @Override
    public String getIcon(Item item) {
        return "steamcraft:weaponPistolSpeedy";
    }

    @Override
    public String getName(Item item) {
        return "item.steamcraft:pistolSpeedy";
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
        return -8;
    }

    @Override
    public int getClipSizeChange(Item weapon) {
        return 0;
    }

    @Override
    public EntityMusketBall changeBullet(EntityMusketBall bullet) {
        return bullet;
    }

}
