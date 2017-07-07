package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.wings;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.ItemSteamExosuitUpgrade;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ItemWingsUpgrade extends ItemSteamExosuitUpgrade {
    public ItemWingsUpgrade() {
        super(ExosuitSlot.BODY_FRONT, "", "", 0);
    }

    private static int getTicks(Entity entity) {
        PlayerData nbt = entity.getCapability(EsteemedInnovation.PLAYER_DATA, null);
        int ticks = nbt.getTickCache();
        if (ticks < 0) {
            nbt.setTickCache(0);
            ticks = 0;
        }
        return ticks;
    }

    private static void updateTicks(Entity entity, int ticks) {
        PlayerData nbt = entity.getCapability(EsteemedInnovation.PLAYER_DATA, null);
        nbt.setTickCache(ticks);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelWings.class;
    }

    @Override
    public void updateModel(ModelBiped modelBiped, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {
        int ticks = getTicks(entityLivingBase);
        float expansion = (float) (0.1F + (Math.sin(Math.toRadians(ticks * (20.0F / 15.0F) * 4.5F))) * 0.8F);

        modelExosuitUpgrade.nbtTagCompound.setFloat("Expansion", expansion);

        if (!entityLivingBase.onGround && entityLivingBase.motionY < 0.0F && !entityLivingBase.isSneaking() && entityLivingBase.fallDistance > 1.4F && ticks < 15) {
            ticks++;
        }

        if ((entityLivingBase.onGround || entityLivingBase.motionY >= 0.0F || entityLivingBase.isSneaking()) && ticks > 0) {
            ticks--;
        }

        updateTicks(entityLivingBase, ticks);
    }

    @Override
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, EntityPlayer player, ItemStack armorStack, EntityEquipmentSlot slot) {
        if (player.fallDistance > 1.5F && !player.isSneaking()) {
            player.fallDistance = 1.5F;
            player.motionY = Math.max(player.motionY, -0.1F);
            player.move(MoverType.SELF, player.motionX, 0, player.motionZ);
        }
    }
}
