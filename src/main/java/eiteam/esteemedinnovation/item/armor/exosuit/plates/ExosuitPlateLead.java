package eiteam.esteemedinnovation.item.armor.exosuit.plates;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ExosuitPlateLead extends ExosuitPlate {
    public ExosuitPlateLead() {
        super("Lead", null, "Lead", "Lead", EsteemedInnovation.MOD_ID + ".plate.lead");
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiersForExosuit(EntityEquipmentSlot armorSlot, ItemStack armorPieceStack) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        map.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getAttributeUnlocalizedName(),
          new AttributeModifier(new UUID(776437, armorSlot.getSlotIndex()), "Lead exosuit " + armorSlot.getName(),
            0.25D, 0));
        return map;
    }
}
