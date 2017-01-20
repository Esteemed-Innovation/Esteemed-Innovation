package eiteam.esteemedinnovation.armor.exosuit.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.handler.GenericEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemRebreatherUpgrade extends ItemExosuitUpgrade {
    public ItemRebreatherUpgrade() {
        super(ExosuitSlot.HEAD_GOGGLES, resource("rebreatherUpgrade"), null, 1);
    }

    @Override
    public void onPlayerAttacked(LivingAttackEvent event, EntityPlayer victim, ItemStack armorStack, EntityEquipmentSlot slot) {
        if (event.getSource() == DamageSource.drown && GenericEventHandler.hasPower(victim, Config.rebreatherConsumption)) {
            GenericEventHandler.drainSteam(victim.getItemStackFromSlot(EntityEquipmentSlot.CHEST), Config.rebreatherConsumption);
            event.setCanceled(true);
        }
    }
}
