package eiteam.esteemedinnovation.firearms.flintlock.enhancements;

import eiteam.esteemedinnovation.api.enhancement.EnhancementFirearm;
import eiteam.esteemedinnovation.api.entity.EntityMusketBall;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static eiteam.esteemedinnovation.firearms.FirearmModule.BLUNDERBUSS;
import static eiteam.esteemedinnovation.firearms.FirearmModule.MUSKET;

public class ItemEnhancementSpeedloader extends Item implements EnhancementFirearm {
    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == MUSKET || stack.getItem() == BLUNDERBUSS;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EsteemedInnovation.upgrade;
    }

    @Override
    public String getID() {
        return "Speedloader";
    }

    @Override
    public ResourceLocation getModel(Item item) {
        String weapon = item == MUSKET ? "musket" : "blunderbuss";
        return new ResourceLocation(EsteemedInnovation.MOD_ID, weapon + "_speedloader");
    }

    @Override
    public String getName(Item item) {
        String weapon = item == MUSKET ? "musket" : "blunderbuss";
        return "item.esteemedinnovation:" + weapon + "Speedloader";
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
        return -30;
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
