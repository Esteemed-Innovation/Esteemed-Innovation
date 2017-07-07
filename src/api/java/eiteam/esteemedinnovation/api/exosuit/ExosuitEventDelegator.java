package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.api.util.TriConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Do not register this class to the event bus, or you will cause the ExosuitUpgrade methods to get called multiple times (bad).
 * This is registered in the static initializer for {@link ExosuitRegistry}.
 */
public class ExosuitEventDelegator {
    /**
     * Iterates over each armor slot, gets the exosuit armor in that slot, and calls the provided function for every
     * installed {@link ExosuitEventHandler}.
     * @param player The player whose armor to iterate through.
     * @param func A void function that passes the current ExosuitEventHandler, the armor ItemStack, and EntityEquipmentSlot
     *             containing this armor.
     */
    private static void doMethodForEachUpgrade(EntityPlayer player, TriConsumer<ExosuitEventHandler, ItemStack, EntityEquipmentSlot> func) {
        for (EntityEquipmentSlot slot : ItemStackUtility.ARMOR_SLOTS) {
            if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) {
                continue;
            }
            ItemStack armor = player.getItemStackFromSlot(slot);
            if (armor.getItem() instanceof ExosuitArmor) {
                for (ExosuitEventHandler thing : ((ExosuitArmor) armor.getItem()).getInstalledEventHandlers(armor)) {
                    func.accept(thing, armor, slot);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        EntityLivingBase jumper = event.getEntityLiving();
        if (jumper instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) jumper;
            doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerJump(event, player, armor, slot));
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(LivingAttackEvent event) {
        EntityLivingBase victim = event.getEntityLiving();
        if (victim instanceof EntityPlayer) {
            EntityPlayer playerVictim = (EntityPlayer) victim;
            doMethodForEachUpgrade(playerVictim, (handler, armor, slot) -> handler.onPlayerAttacked(event, playerVictim, armor, slot));
        }
        DamageSource source = event.getSource();
        Entity entitySource = source.getTrueSource();
        if (entitySource instanceof EntityPlayer) {
            EntityPlayer playerAttacker = (EntityPlayer) entitySource;
            doMethodForEachUpgrade(playerAttacker, (handler, armor, slot) -> handler.onPlayerAttacksOther(event, playerAttacker, armor, slot));
        }
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        EntityLivingBase victim = event.getEntityLiving();
        if (victim instanceof EntityPlayer) {
            EntityPlayer playerVictim = (EntityPlayer) victim;
            doMethodForEachUpgrade(playerVictim, (handler, armor, slot) -> handler.onPlayerHurt(event, playerVictim, armor, slot));
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerUpdate(event, player, armor, slot));
        }
    }

    @SubscribeEvent
    public void onPickupXP(PlayerPickupXpEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerPickupXP(event, armor, slot));
    }

    @SubscribeEvent
    public void onPlayerInteractsWithEntitySpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerInteractsWithEntitySpecific(event, armor, slot));
    }

    @SubscribeEvent
    public void onPlayerInteractsWithEntity(PlayerInteractEvent.EntityInteract event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerInteractsWithEntity(event, armor, slot));
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerRightClickBlock(event, armor, slot));
    }

    @SubscribeEvent
    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerRightClickItem(event, armor, slot));
    }

    @SubscribeEvent
    public void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerRightClickEmpty(event, armor, slot));
    }

    @SubscribeEvent
    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerLeftClickBlock(event, armor, slot));
    }

    @SubscribeEvent
    public void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerLeftClickEmpty(event, armor, slot));
    }

    @SubscribeEvent
    public void onEntityItemPickedUp(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerPickupItem(event, armor, slot));
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        doMethodForEachUpgrade(player, (handler, armor, slot) -> handler.onPlayerTick(event, armor, slot));
    }
}
