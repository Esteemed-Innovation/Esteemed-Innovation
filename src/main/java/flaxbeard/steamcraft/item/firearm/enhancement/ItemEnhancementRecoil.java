package flaxbeard.steamcraft.item.firearm.enhancement;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.entity.projectile.EntityMusketBall;
import flaxbeard.steamcraft.init.items.firearms.FirearmItems;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemEnhancementRecoil extends Item implements IEnhancementFirearm {
    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == FirearmItems.Items.BLUNDERBUSS.getItem();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return Steamcraft.upgrade;
    }

    @Override
    public String getID() {
        return "Recoil";
    }

    @Override
    public ResourceLocation getIcon(Item item) {
        return new ResourceLocation(Steamcraft.MOD_ID, "weaponBlunderbussRecoil");
    }

    @Override
    public String getName(Item item) {
        return "item.steamcraft:blunderbussRecoil";
    }

    @Override
    public float getAccuracyChange(Item weapon) {
        return -1.0F;
    }

    @Override
    public float getKnockbackChange(Item weapon) {
        return -2.0F;
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
        return bullet;
    }

}
