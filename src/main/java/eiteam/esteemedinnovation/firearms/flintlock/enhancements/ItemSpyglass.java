package eiteam.esteemedinnovation.firearms.flintlock.enhancements;

import eiteam.esteemedinnovation.api.enhancement.EnhancementFirearm;
import eiteam.esteemedinnovation.api.entity.EntityMusketBall;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static eiteam.esteemedinnovation.firearms.FirearmModule.MUSKET;

public class ItemSpyglass extends Item implements EnhancementFirearm {
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 72000;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == MUSKET;
    }

    @Override
    public String getID() {
        return "scope";
    }

    @Override
    public ResourceLocation getModel(Item item) {
        return new ResourceLocation(EsteemedInnovation.MOD_ID, "musket_sharpshooter");
    }

    @Override
    public String getName(Item item) {
        return "item.esteemedinnovation:musketMarksman";
    }

    @Override
    public float getAccuracyChange(Item weapon) {
        return -0.1F;
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
        return bullet;
    }
}
