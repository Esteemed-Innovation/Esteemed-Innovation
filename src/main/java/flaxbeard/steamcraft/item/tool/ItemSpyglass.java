package flaxbeard.steamcraft.item.tool;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.entity.projectile.EntityMusketBall;
import flaxbeard.steamcraft.init.items.firearms.FirearmItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemSpyglass extends Item implements IEnhancementFirearm {
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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
        return stack.getItem() == FirearmItems.Items.MUSKET.getItem();
    }

    @Override
    public String getID() {
        return "scope";
    }

    @Override
    public ResourceLocation getIcon(Item item) {
        return new ResourceLocation(Steamcraft.MOD_ID, "weaponMusketSharpshooter");
    }

    @Override
    public String getName(Item item) {
        return "item.steamcraft:musketMarksman";
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
