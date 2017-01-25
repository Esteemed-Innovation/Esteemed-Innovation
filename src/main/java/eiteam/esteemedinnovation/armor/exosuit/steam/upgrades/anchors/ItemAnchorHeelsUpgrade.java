package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.anchors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.ItemSteamExosuitUpgrade;
import eiteam.esteemedinnovation.commons.util.ReflectionHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.UUID;

public class ItemAnchorHeelsUpgrade extends ItemSteamExosuitUpgrade {
    public ItemAnchorHeelsUpgrade() {
        super(ExosuitSlot.BOOTS_FEET, "", null, 0);
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelAnchors.class;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiersForExosuit(EntityEquipmentSlot armorSlot, ItemStack armorPieceStack) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        map.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getAttributeUnlocalizedName(),
          new AttributeModifier(new UUID(776437, armorSlot.getSlotIndex()), "Lead exosuit " + armorSlot.getName(),
            0.25D, 0));
        return map;
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event, ItemStack armorStack, EntityEquipmentSlot slot) {
        EntityPlayer player = event.player;
        if (ChargableUtility.hasPower(player, 1)) {
            boolean inWater = player.isInWater();
            double newY = inWater ? -0.6 : -1.1;
            try {
                if (((inWater && !ReflectionHelper.getIsEntityJumping(player)) || player.motionY < -0.3) && player.motionY != newY) {
                    player.motionY = newY;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
