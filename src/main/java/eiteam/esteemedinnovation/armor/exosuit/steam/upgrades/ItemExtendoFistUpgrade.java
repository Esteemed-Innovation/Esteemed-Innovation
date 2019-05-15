package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.armor.ArmorModule;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import java.util.UUID;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemExtendoFistUpgrade extends ItemSteamExosuitUpgrade {
    public ItemExtendoFistUpgrade() {
        super(ExosuitSlot.BODY_HAND, resource("extendo_fist"), null, 0);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiersForExosuit(EntityEquipmentSlot armorSlot, @Nonnull ItemStack armorPieceStack) {
        if(armorSlot == EntityEquipmentSlot.CHEST) {
            Multimap<String, AttributeModifier> map = HashMultimap.create();
            map.put(EntityPlayer.REACH_DISTANCE.getName(),
              new AttributeModifier(new UUID(776438, armorSlot.getSlotIndex()),"extendo exosuit" + armorSlot.getName(), ArmorModule.extendedRange, 0));
            return map;
        } else {
            return super.getAttributeModifiersForExosuit(armorSlot, armorPieceStack);
        }
    }
}
