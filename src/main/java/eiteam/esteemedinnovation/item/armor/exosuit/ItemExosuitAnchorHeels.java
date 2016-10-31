package eiteam.esteemedinnovation.item.armor.exosuit;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelAnchors;
import eiteam.esteemedinnovation.handler.GenericEventHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.UUID;

public class ItemExosuitAnchorHeels extends ItemExosuitUpgrade {
    public ItemExosuitAnchorHeels() {
        super(ExosuitSlot.BOOTS_FEET, "", null, 0);
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
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

    private class EventHandlers {
        @SubscribeEvent
        public void fallFast(TickEvent.PlayerTickEvent event) {
            EntityPlayer player = event.player;
            if (GenericEventHandler.hasPower(player, 1) && isInstalled(player)) {
                double newY = player.isInWater() ? -0.6 : -1.1;
                if (player.motionY < -0.3 && player.motionY != newY) {
                    player.motionY = newY;
                }
            }
        }
    }
}
