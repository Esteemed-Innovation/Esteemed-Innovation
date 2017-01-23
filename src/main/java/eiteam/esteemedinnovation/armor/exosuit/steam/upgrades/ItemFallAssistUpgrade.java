package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ExosuitUtility;
import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemFallAssistUpgrade extends ItemSteamExosuitUpgrade {
    public ItemFallAssistUpgrade() {
        super(ExosuitSlot.BOOTS_TOP, resource("fallUpgrade"), null, 0);
    }

    @Override
    public void onPlayerHurt(LivingHurtEvent event, EntityPlayer victim, ItemStack armorStack, EntityEquipmentSlot slot) {
        if (event.getSource() == DamageSource.fall) {
            boolean hasPower = ExosuitUtility.hasPower(event.getEntityLiving(), (int) (event.getAmount() / Config.fallAssistDivisor));
            if (hasPower) {
                if (event.getAmount() <= 6.0F) {
                    event.setAmount(0F);
                }
                event.setAmount(event.getAmount() / 3F);
                ExosuitUtility.drainSteam(victim.getItemStackFromSlot(EntityEquipmentSlot.CHEST), (int) (event.getAmount() / Config.fallAssistDivisor));
                if (event.getAmount() == 0.0F) {
                    event.setResult(Event.Result.DENY);
                    event.setCanceled(true);
                }
            }
        }
    }
}
