package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency;

import eiteam.esteemedinnovation.api.event.AnimalTradeEvent;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.ArmorModule;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.ItemSteamExosuitUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eiteam.esteemedinnovation.api.ChargableUtility.hasPower;

public class ItemFrequencyShifterUpgrade extends ItemSteamExosuitUpgrade {
    public ItemFrequencyShifterUpgrade() {
        super(ExosuitSlot.HEAD_HELM, "", null, 0);
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelFrequencyShifter.class;
    }

    private final class EventHandlers {
        @SubscribeEvent
        public void handlePainfulFrequencies(AnimalTradeEvent event) {
            EntityLiving entity = event.salesperson;
            AnimalData data = entity.getCapability(ArmorModule.ANIMAL_DATA, null);
            if (data.getTotalTrades() > data.getMaximumTotalTrades()) {
                entity.setAttackTarget(event.customer);
            }
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void ignoreChatMessage(ClientChatReceivedEvent event) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            World world = mc.world;
            String message = event.getMessage().getUnformattedText();
            Matcher matcher = Pattern.compile("<(.+?)>").matcher(message);
            if (matcher.find()) {
                EntityPlayer messager = world.getPlayerEntityByName(matcher.group(0));
                if (messager != null) {
                    if (!messager.getDisplayName().equals(player.getDisplayName()) &&
                      playerCanUseFrequencyShifter(messager) && playerCanUseFrequencyShifter(player)) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void openMerchant(PlayerInteractEvent.EntityInteract event) {
            EntityPlayer player = event.getEntityPlayer();
            Entity target = event.getTarget();
            ItemStack held = player.getHeldItemMainhand();
            if (held.getItem() instanceof ItemNameTag) {
                return;
            }
            if (playerCanUseFrequencyShifter(player) && (target instanceof EntityWolf || target instanceof EntityOcelot)) {
                EntityLiving living = (EntityLiving) target;
                AnimalData data = target.getCapability(ArmorModule.ANIMAL_DATA, null);
                if (data.getTotalTrades() > data.getMaximumTotalTrades()) {
                    if (living instanceof EntityWolf) {
                        EntityWolf wolf = (EntityWolf) living;
                        wolf.setAngry(true);
                    } else {
                        EntityOcelot cat = (EntityOcelot) living;
                        living.targetTasks.addTask(3, new EntityAIHurtByTarget(cat, true));
                    }
                } else {
                    if (living.hasCustomName()) {
                        data.setMerchantName(living.getCustomNameTag());
                    }
                    String name = data.getMerchantName();
                    IMerchant merchant = new FrequencyMerchant(living, name);
                    merchant.setCustomer(player);
                    player.displayVillagerTradeGui(merchant);
                    data.setTotalTrades(data.getTotalTrades() + 1);
                }
            }
        }

        /**
         * Checks whether the given player has the Frequency Shifter upgrade, and enough steam in their suit.
         * @param player The player to check.
         * @return True if the player has the Frequency Shifter upgrade.
         */
        private boolean playerCanUseFrequencyShifter(EntityPlayer player) {
            return isInstalled(player) && hasPower(player, 1);
        }
    }
}
