package eiteam.esteemedinnovation.item.firearm.enhancement;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.enhancement.IEnhancementFirearm;
import eiteam.esteemedinnovation.api.entity.EntityMusketBall;
import eiteam.esteemedinnovation.init.items.firearms.FirearmItems;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemEnhancementRevolver extends Item implements IEnhancementFirearm {
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
        return "revolver";
    }

    @Override
    public ResourceLocation getModel(Item item) {
        return new ResourceLocation(EsteemedInnovation.MOD_ID, "pistol_revolver");
    }

    @Override
    public String getName(Item item) {
        return "item.esteemedinnovation:revolver";
    }

    @Override
    public float getAccuracyChange(Item weapon) {
        return 0.2F;
    }

    @Override
    public float getKnockbackChange(Item weapon) {
        return -1.0F;
    }

    @Override
    public float getDamageChange(Item weapon) {
        return -2.5F;
    }

    @Override
    public int getReloadChange(Item weapon) {
        return 42;
    }

    @Override
    public int getClipSizeChange(Item weapon) {
        return 5;
    }

    @Override
    public EntityMusketBall changeBullet(EntityMusketBall bullet) {
        return bullet;
    }

}
