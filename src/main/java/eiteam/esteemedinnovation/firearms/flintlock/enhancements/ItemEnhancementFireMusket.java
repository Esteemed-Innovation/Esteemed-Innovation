package eiteam.esteemedinnovation.firearms.flintlock.enhancements;

import eiteam.esteemedinnovation.api.enhancement.EnhancementFirearm;
import eiteam.esteemedinnovation.api.entity.EntityMusketBall;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static eiteam.esteemedinnovation.init.items.firearms.FirearmItems.Items.BLUNDERBUSS;
import static eiteam.esteemedinnovation.init.items.firearms.FirearmItems.Items.MUSKET;

public class ItemEnhancementFireMusket extends Item implements EnhancementFirearm {
    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == MUSKET.getItem() || stack.getItem() == BLUNDERBUSS.getItem();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EsteemedInnovation.upgrade;
    }

    @Override
    public String getID() {
        return "fireMusket";
    }

    @Override
    public ResourceLocation getModel(Item item) {
        String weapon = item == MUSKET.getItem() ? "musket" : "blunderbuss";
        return new ResourceLocation(EsteemedInnovation.MOD_ID, weapon + "_blaze");
    }

    @Override
    public String getName(Item item) {
        String weapon = item == MUSKET.getItem() ? "musket" : "blunderbuss";
        return "item.esteemedinnovation:" + weapon + "Ablaze";
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
        return 0;
    }

    @Override
    public int getClipSizeChange(Item weapon) {
        return 0;
    }

    @Override
    public EntityMusketBall changeBullet(EntityMusketBall bullet) {
        bullet.setFire(100);
        return bullet;
    }
}
