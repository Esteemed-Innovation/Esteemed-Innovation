package eiteam.esteemedinnovation.armor.exosuit.upgrades.wings;

import eiteam.esteemedinnovation.armor.exosuit.upgrades.ItemExosuitUpgrade;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.commons.capabilities.player.IPlayerData;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemExosuitWings extends ItemExosuitUpgrade {
    public ItemExosuitWings() {
        super(ExosuitSlot.BODY_FRONT, "", "", 0);
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    private static int getTicks(Entity entity) {
        IPlayerData nbt = entity.getCapability(EsteemedInnovation.PLAYER_DATA, null);
        int ticks = nbt.getTickCache();
        if (ticks < 0) {
            nbt.setTickCache(0);
            ticks = 0;
        }
        return ticks;
    }

    private static void updateTicks(Entity entity, int ticks) {
        IPlayerData nbt = entity.getCapability(EsteemedInnovation.PLAYER_DATA, null);
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

        modelExosuitUpgrade.nbtTagCompound.setFloat("expansion", expansion);

        if (!entityLivingBase.onGround && entityLivingBase.motionY < 0.0F && !entityLivingBase.isSneaking() && entityLivingBase.fallDistance > 1.4F && ticks < 15) {
            ticks++;
        }

        if ((entityLivingBase.onGround || entityLivingBase.motionY >= 0.0F || entityLivingBase.isSneaking()) && ticks > 0) {
            ticks--;
        }

        updateTicks(entityLivingBase, ticks);
    }

    private class EventHandlers {
        @SubscribeEvent
        public void glide(LivingEvent.LivingUpdateEvent event) {
            EntityLivingBase entity = event.getEntityLiving();
            if (isInstalled(entity) && entity.fallDistance > 1.5F && !entity.isSneaking()) {
                entity.fallDistance = 1.5F;
                entity.motionY = Math.max(entity.motionY, -0.1F);
                entity.moveEntity(entity.motionX, 0, entity.motionZ);
            }
        }
    }
}
