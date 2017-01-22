package eiteam.esteemedinnovation.armor.exosuit.upgrades.thrusters;

import eiteam.esteemedinnovation.api.exosuit.ExosuitArmor;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ExosuitUtility;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.ItemExosuitUpgrade;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import eiteam.esteemedinnovation.commons.util.EntityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;

import javax.vecmath.Vector2d;

public class ItemExosuitSidepack extends ItemExosuitUpgrade {
    public ItemExosuitSidepack() {
        super(ExosuitSlot.LEGS_HIPS, "", "", 0);
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelSidepack.class;
    }

    @Override
    public void updateModel(ModelBiped modelBiped, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {
        Vector2d vector = new Vector2d(entityLivingBase.motionX, entityLivingBase.motionZ);

        if (entityLivingBase instanceof EntityPlayer && EntityHelper.hasEntityMoved(entityLivingBase)) {
            float targetRotation = 360F * ((float) (StrictMath.atan2(vector.y, vector.x) / (2 * Math.PI)));

            NBTTagCompound nbt = modelExosuitUpgrade.nbtTagCompound;

            if (!nbt.hasKey("rotation")) {
                nbt.setFloat("rotation", targetRotation);
            }

            modelExosuitUpgrade.nbtTagCompound.setFloat("rotation", targetRotation);
        }
    }

    @Override
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, EntityPlayer player, ItemStack armorStack, EntityEquipmentSlot slot) {
        if (ExosuitUtility.hasPower(player, 1)) {
            PlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);
            if (data.getLastMotions() == null) {
                data.setLastMotions(MutablePair.of(player.posX, player.posZ));
            }
            MutablePair<Double, Double> lastMotions = data.getLastMotions();
            if ((lastMotions.getLeft() != player.posX || lastMotions.getRight() != player.posZ) &&
              !player.onGround && isPlayerNotInWaterOrFlying(player)) {
                player.moveEntity(player.motionX, 0, player.motionZ);
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                ExosuitArmor chestArmor = (ExosuitArmor) chestStack.getItem();
                if (!chestStack.getTagCompound().hasKey("ticksUntilConsume")) {
                    chestStack.getTagCompound().setInteger("ticksUntilConsume", 2);
                }
                if (chestStack.getTagCompound().getInteger("ticksUntilConsume") <= 0) {
                    chestArmor.drainSteam(chestStack, Config.thrusterConsumption);
                }
            }
        }
    }

    /**
     * @param player The Player
     * @return Whether the player is not in water or flying.
     */
    private static boolean isPlayerNotInWaterOrFlying(EntityPlayer player) {
        return !player.isInWater() && !player.capabilities.isFlying;
    }

    private final class EventHandlers {
        /**
         * @param player The player to check
         * @return Whether the player is moving at all, is not in water, and is not flying.
         */
        private boolean isMoving(EntityPlayer player) {
            return Math.abs(player.motionX) + Math.abs(player.motionZ) > 0F && isPlayerNotInWaterOrFlying(player);
        }

        private void spawnSmoke(EntityPlayer player, double rotation) {
            player.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
              player.posX + 0.5 * StrictMath.sin(rotation),
              player.posY - 1F, player.posZ - 0.5 * StrictMath.cos(rotation),
              player.motionX * -0.1F, 0, player.motionZ * -0.1F);
        }

        /**
         * Checks if the player is wearing thrusters, has power, and is moving, and if so, spawns smoke on each side.
         */
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void emitSmokeParticles(TickEvent.ClientTickEvent event) {
            if (event.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                EntityPlayer player = mc.thePlayer;
                if (player != null && player.worldObj.isRemote && isInstalled(player) &&
                  ExosuitUtility.hasPower(player, 1) && isMoving(player)) {
                    spawnSmoke(player, Math.toRadians(player.renderYawOffset + 90.0F));
                    spawnSmoke(player, Math.toRadians(player.renderYawOffset + 270.0F));
                }
            }
        }
    }
}
