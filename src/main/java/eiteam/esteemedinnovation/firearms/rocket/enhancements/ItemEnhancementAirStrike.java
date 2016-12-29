package eiteam.esteemedinnovation.firearms.rocket.enhancements;

import eiteam.esteemedinnovation.api.enhancement.EnhancementRocketLauncher;
import eiteam.esteemedinnovation.api.entity.EntityRocket;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.init.items.firearms.FirearmItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemEnhancementAirStrike extends Item implements EnhancementRocketLauncher {
    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() == FirearmItems.Items.ROCKET_LAUNCHER.getItem();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EsteemedInnovation.upgrade;
    }

    @Override
    public String getID() {
        return "airStrike";
    }

    @Override
    public ResourceLocation getModel(Item item) {
        return new ResourceLocation(EsteemedInnovation.MOD_ID, "rocket_launcher_air_strike");
    }

    @Override
    public String getName(Item item) {
        return "item.esteemedinnovation:rocketLauncherAirStrike";
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

    @Override
    public void afterRoundFired(ItemStack weaponStack, World world, EntityPlayer player) {
        // Suppressing fire delay stuff by not calling the supermethod.
    }
}
