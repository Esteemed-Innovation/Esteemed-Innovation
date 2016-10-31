package eiteam.esteemedinnovation.item.armor.exosuit;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.IExosuitArmor;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelJetpack;
import eiteam.esteemedinnovation.handler.FieldHandler;
import eiteam.esteemedinnovation.network.JumpValueChangePacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemExosuitJetpack extends ItemExosuitUpgrade {
    public ItemExosuitJetpack() {
        super(ExosuitSlot.BODY_FRONT, "", "", 0);
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelJetpack.class;
    }

    /**
     * This is sort of a workaround for getModel() causing NoClassDefFoundErrors (ModelBiped) when `this` (ItemExosuitJetpack)
     * was registered to the event bus on the server.
     */
    private class EventHandlers {
        @SubscribeEvent
        public void handleJetpackFlight(TickEvent.PlayerTickEvent event) {
            if (FieldHandler.isJumpingField == null) {
                return;
            }
            EntityPlayer player = event.player;
            boolean isServer = event.side.isServer();
            boolean isJumping;
            try {
                isJumping = getIsEntityJumping(player);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }

            if (!isJumping || player.onGround || player.capabilities.isFlying) {
                return;
            }

            ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (chest == null || !isInstalled(player)) {
                return;
            }

            Item chestItem = chest.getItem();
            if (!(chestItem instanceof IExosuitArmor)) {
                return;
            }
            IExosuitArmor chestArmor = (IExosuitArmor) chestItem;
            if (!chestArmor.hasPower(chest, 5)) {
                return;
            }

            player.motionY += 0.06D;
            player.fallDistance = 0.0F;
            if (isServer) {
                chestArmor.drainSteam(chest, Config.jetpackConsumption);
            } else {
                World world = player.worldObj;
                double rotation = Math.toRadians(player.renderYawOffset);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                  player.posX + 0.4 * StrictMath.sin(rotation + 0.9F),
                  player.posY - 1F, player.posZ - 0.4 * StrictMath.cos(rotation + 0.9F),
                  0.0F, -1.0F, 0.0F);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                  player.posX + 0.4 * StrictMath.sin(rotation - 0.9F),
                  player.posY - 1F, player.posZ - 0.4 * StrictMath.cos(rotation - 0.9F),
                  0.0F, -1.0F, 0.0F);
            }
        }

        private Map<UUID, Boolean> prevIsJumping = new HashMap<>();

        @SubscribeEvent
        public void sendPlayerInputPacketToServer(LivingEvent.LivingUpdateEvent event) {
            EntityLivingBase elb = event.getEntityLiving();
            if (elb.worldObj.isRemote) {
                boolean isJumping;
                try {
                    isJumping = getIsEntityJumping(elb);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }

                UUID id = elb.getUniqueID();

                if (!prevIsJumping.containsKey(id) || prevIsJumping.get(id) != isJumping) {
                    prevIsJumping.put(id, isJumping);
                    EsteemedInnovation.channel.sendToServer(new JumpValueChangePacket(isJumping));
                }
            }
        }

        private boolean getIsEntityJumping(EntityLivingBase elb) throws IllegalAccessException {
            return (boolean) FieldHandler.isJumpingField.get(elb);
        }
    }
}
