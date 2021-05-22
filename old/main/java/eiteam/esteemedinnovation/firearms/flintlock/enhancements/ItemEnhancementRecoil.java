package eiteam.esteemedinnovation.firearms.flintlock.enhancements;

import eiteam.esteemedinnovation.api.enhancement.EnhancementFirearm;
import eiteam.esteemedinnovation.api.entity.EntityMusketBall;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static eiteam.esteemedinnovation.firearms.FirearmModule.BLUNDERBUSS;

public class ItemEnhancementRecoil extends Item implements EnhancementFirearm {
    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == BLUNDERBUSS;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EsteemedInnovation.upgrade;
    }

    @Override
    public String getID() {
        return "Recoil";
    }

    @Override
    public ResourceLocation getModel(Item item) {
        return new ResourceLocation(EsteemedInnovation.MOD_ID, "blunderbuss_recoil");
    }

    @Override
    public String getName(Item item) {
        return "item.esteemedinnovation:blunderbussRecoil";
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
