package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemModularAcceleratorUpgrade extends ItemSteamExosuitUpgrade {
    public ItemModularAcceleratorUpgrade() {
        super(ExosuitSlot.LEGS_LEGS, resource("runUpgrade"), null, 0);
    }

    @Override
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, EntityPlayer player, @Nonnull ItemStack armorStack, EntityEquipmentSlot slot) {
        if (ChargableUtility.hasPower(player, 1)) {
            PlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);

            if (data.getLastMotions() == null) {
                data.setLastMotions(Pair.of(player.posX, player.posZ));
            }
            double lastX = data.getLastMotions().getLeft();
            double lastZ = data.getLastMotions().getRight();
            if ((player.moveForward > 0.0F) && (lastX != player.posX || lastZ != player.posZ) && player.onGround && !player.isInWater()) {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                player.moveRelative(0F, 1F, 0.075F); //entity.moveFlying(0.0F, 1.0F, 0.075F); TODO Test this.
                if (!chestStack.getTagCompound().hasKey("TicksUntilSteamDrain")) {
                    chestStack.getTagCompound().setInteger("TicksUntilSteamDrain", 2);
                }
                if (chestStack.getTagCompound().getInteger("TicksUntilSteamDrain") <= 0) {
                    ChargableUtility.drainSteam(chestStack, Config.runAssistConsumption, player);
                }
            }
        }
    }
}
