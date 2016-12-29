package eiteam.esteemedinnovation.firearms.flintlock.enhancements;

import eiteam.esteemedinnovation.api.enhancement.EnhancementFirearm;
import eiteam.esteemedinnovation.api.entity.EntityMusketBall;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.init.items.firearms.FirearmItems;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemEnhancementSpeedy extends Item implements EnhancementFirearm {
    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == FirearmItems.Items.PISTOL.getItem();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EsteemedInnovation.upgrade;
    }

    @Override
    public String getID() {
        return "Speedy";
    }

    @Override
    public ResourceLocation getModel(Item item) {
        return new ResourceLocation(EsteemedInnovation.MOD_ID, "pistol_speedy");
    }

    @Override
    public String getName(Item item) {
        return "item.esteemedinnovation:pistolSpeedy";
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

    @Override
    public void onWeaponUpdate(ItemStack weaponStack, World world, Entity entity, int itemSlot, boolean isWeaponCurrentItem) {
        if (isWeaponCurrentItem && entity instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) entity;
            player.movementInput.moveForward *= 5.0F;
            player.movementInput.moveStrafe *= 5.0F;
        }
    }
}
