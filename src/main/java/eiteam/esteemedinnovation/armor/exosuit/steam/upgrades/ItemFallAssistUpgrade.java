package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemFallAssistUpgrade extends ItemSteamExosuitUpgrade {
    public ItemFallAssistUpgrade() {
        super(ExosuitSlot.BOOTS_TOP, resource("fall_upgrade"), null, 0);
    }

    @Override
    public void onPlayerHurt(LivingHurtEvent event, EntityPlayer victim, @Nonnull ItemStack armorStack, EntityEquipmentSlot slot) {
        if (event.getSource() == DamageSource.FALL) {
            boolean hasPower = ChargableUtility.hasPower(event.getEntityLiving(), (int) (event.getAmount() / Config.fallAssistDivisor));
            if (hasPower) {
                if (event.getAmount() <= 6.0F) {
                    event.setAmount(0F);
                }
                event.setAmount(event.getAmount() / 3F);
                ChargableUtility.drainSteam(victim.getItemStackFromSlot(EntityEquipmentSlot.CHEST), (int) (event.getAmount() / Config.fallAssistDivisor), victim);
                if (event.getAmount() == 0.0F) {
                    event.setResult(Event.Result.DENY);
                    event.setCanceled(true);
                }
            }
        }
    }
}
