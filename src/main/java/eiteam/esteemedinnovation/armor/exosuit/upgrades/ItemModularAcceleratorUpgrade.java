package eiteam.esteemedinnovation.armor.exosuit.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ExosuitUtility;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.apache.commons.lang3.tuple.MutablePair;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemModularAcceleratorUpgrade extends ItemExosuitUpgrade {
    public ItemModularAcceleratorUpgrade() {
        super(ExosuitSlot.LEGS_LEGS, resource("runUpgrade"), null, 0);
    }

    @Override
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, EntityPlayer player, ItemStack armorStack, EntityEquipmentSlot slot) {
        if (ExosuitUtility.hasPower(player, 1)) {
            PlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);

            if (data.getLastMotions() == null) {
                data.setLastMotions(MutablePair.of(player.posX, player.posZ));
            }
            double lastX = data.getLastMotions().left;
            double lastZ = data.getLastMotions().right;
            if ((player.moveForward > 0.0F) && (lastX != player.posX || lastZ != player.posZ) && player.onGround && !player.isInWater()) {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                player.moveRelative(0F, 1F, 0.075F); //entity.moveFlying(0.0F, 1.0F, 0.075F); TODO Test this.
                if (!chestStack.getTagCompound().hasKey("ticksUntilConsume")) {
                    chestStack.getTagCompound().setInteger("ticksUntilConsume", 2);
                }
                if (chestStack.getTagCompound().getInteger("ticksUntilConsume") <= 0) {
                    ExosuitUtility.drainSteam(chestStack, Config.runAssistConsumption);
                }
            }
        }
    }
}
