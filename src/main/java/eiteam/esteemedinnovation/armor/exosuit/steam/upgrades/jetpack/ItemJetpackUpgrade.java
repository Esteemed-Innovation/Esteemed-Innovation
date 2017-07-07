package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.jetpack;

import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.ItemSteamExosuitUpgrade;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ExosuitArmor;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.commons.util.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ItemJetpackUpgrade extends ItemSteamExosuitUpgrade {
    public ItemJetpackUpgrade() {
        super(ExosuitSlot.BODY_FRONT, "", "", 0);
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelJetpack.class;
    }

    /**
     * This is sort of a workaround for getModel() causing NoClassDefFoundErrors (ModelBiped) when `this` (ItemJetpackUpgrade)
     * was registered to the event bus on the server.
     */
    private class EventHandlers {
        /**
         * Checks if the player is capable of flight (see {@link #getArmorAndCanFly(EntityPlayer)}) and produces flight
         * by increasing their Y motion and negating their fall damage. On the client it produces particles and
         * on the server it drains steam.
         */
        @SubscribeEvent
        public void handleJetpackFlight(TickEvent.PlayerTickEvent event) {
            if (ReflectionHelper.isJumpingField == null) {
                return;
            }
            EntityPlayer player = event.player;
            boolean isServer = event.side.isServer();
            ItemStack chest = getArmorAndCanFly(player);
            if (chest == null) {
                return;
            }
            ExosuitArmor chestArmor = (ExosuitArmor) chest.getItem();

            player.motionY += 0.06D;
            player.fallDistance = 0.0F;
            if (isServer) {
                ((SteamChargable) chestArmor).drainSteam(chest, Config.jetpackConsumption, player);
            } else {
                World world = player.world;
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

        /**
         * Checks if the player is capable of flying with a jetpack.
         * @param player The player to check.
         * @return The ItemStack containing the chest piece. Null if they cannot fly. Null does *not* mean that they
         *         are not wearing a chestpiece, it simply means they do not match the criteria for jetpack flight.
         */
        private ItemStack getArmorAndCanFly(EntityPlayer player) {
            boolean isJumping;
            try {
                isJumping = ReflectionHelper.getIsEntityJumping(player);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }

            if (!isJumping || player.onGround || player.capabilities.isFlying) {
                return null;
            }

            ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (!isInstalled(player)) {
                return null;
            }

            Item chestItem = chest.getItem();
            if (!(chestItem instanceof ExosuitArmor) || !(chestItem instanceof SteamChargable)) {
                return null;
            }
            ExosuitArmor chestArmor = (ExosuitArmor) chestItem;
            if (!((SteamChargable) chestArmor).hasPower(chest, Config.jetpackConsumption)) {
                return null;
            }

            return chest;
        }
    }
}
