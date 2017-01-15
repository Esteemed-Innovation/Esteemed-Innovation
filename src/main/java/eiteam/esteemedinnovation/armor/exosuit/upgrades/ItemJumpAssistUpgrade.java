package eiteam.esteemedinnovation.armor.exosuit.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.handler.GenericEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemJumpAssistUpgrade extends ItemExosuitUpgrade {
    public ItemJumpAssistUpgrade() {
        super(ExosuitSlot.BOOTS_TOP, resource("jumpUpgrade"), null, 0);
    }

    @Override
    public void onPlayerJump(LivingEvent.LivingJumpEvent event, EntityPlayer jumper, ItemStack armorStack, EntityEquipmentSlot slot) {
        boolean shiftJump = jumper.isSneaking() && GenericEventHandler.hasPower(jumper, Config.jumpBoostConsumptionShiftJump);

        if (shiftJump || GenericEventHandler.hasPower(jumper, Config.jumpBoostConsumption)) {
            if (shiftJump) {
                Vec3d vector = jumper.getLook(0.5F);
                double total = Math.abs(vector.zCoord + vector.xCoord);
                double y = vector.yCoord < total ? total : vector.yCoord;

                jumper.motionY += (y) / 1.5F;
                jumper.motionZ += vector.zCoord * 2;
                jumper.motionX += vector.xCoord * 2;
                GenericEventHandler.drainSteam(jumper.getItemStackFromSlot(EntityEquipmentSlot.CHEST), Config.jumpBoostConsumptionShiftJump);
            } else {
                GenericEventHandler.drainSteam(jumper.getItemStackFromSlot(EntityEquipmentSlot.CHEST), Config.jumpBoostConsumption);
                jumper.motionY += 0.2750000059604645D;
            }
        }

        //noinspection ConstantConditions
        armorStack.getTagCompound().setBoolean("releasedSpace", false);
    }
}
