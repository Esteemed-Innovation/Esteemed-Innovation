package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.armor.ArmorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemRebreatherUpgrade extends ItemSteamExosuitUpgrade {
    public ItemRebreatherUpgrade() {
        super(ExosuitSlot.HEAD_GOGGLES, resource("rebreather_upgrade"), null, 1);
    }

    @Override
    public void onPlayerAttacked(LivingAttackEvent event, EntityPlayer victim, @Nonnull ItemStack armorStack, EntityEquipmentSlot slot) {
        if (event.getSource() == DamageSource.DROWN && ChargableUtility.hasPower(victim, ArmorModule.rebreatherConsumption)) {
            ChargableUtility.drainSteam(victim.getItemStackFromSlot(EntityEquipmentSlot.CHEST), ArmorModule.rebreatherConsumption, victim);
            event.setCanceled(true);
        }
    }
}
