package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemAcousticDampenerUpgrade extends ItemSteamExosuitUpgrade {
    public ItemAcousticDampenerUpgrade() {
        super(ExosuitSlot.LEGS_LEGS, resource("stealth_upgrade"), null, 0);
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    private class EventHandlers {
        @SubscribeEvent
        public void hideFromAttacker(LivingSetAttackTargetEvent event) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            if (entityLiving instanceof EntityLiving) {
                hideCloakedPlayers((EntityLiving) entityLiving, event.getTarget());
            }
        }

        @SubscribeEvent
        public void hideFromAttacker(LivingEvent.LivingUpdateEvent event) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            if (entityLiving instanceof EntityLiving) {
                EntityLiving entity = (EntityLiving) entityLiving;
                hideCloakedPlayers(entity, entity.getAttackTarget());
            }
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void muffleSounds(PlaySoundEvent event) {
            if (event.getName().contains("step")) {
                float x = event.getSound().getXPosF();
                float y = event.getSound().getYPosF();
                float z = event.getSound().getZPosF();
                List<EntityLivingBase> entities = Minecraft.getMinecraft().player.world.getEntitiesWithinAABB(
                  EntityLivingBase.class, new AxisAlignedBB(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F));
                for (EntityLivingBase entity : entities) {
                    if (isInstalled(entity)) {
                        event.setResultSound(null);
                    }
                }
            }
        }

        private void hideCloakedPlayers(EntityLiving entity, EntityLivingBase target) {
            if (target == null || !isInstalled(target)) {
                return;
            }

            IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            double d0 = (iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue()) / 1.5D;
            List<Entity> list = entity.world.getEntitiesWithinAABB(Entity.class,
              entity.getEntityBoundingBox().expand(d0, 4.0D, d0));
            boolean foundPlayer = false;
            for (Entity mob : list) {
                if (mob == target) {
                    foundPlayer = true;
                    break;
                }
            }
            if (!foundPlayer) {
                entity.setAttackTarget(null);
            }
        }
    }
}
