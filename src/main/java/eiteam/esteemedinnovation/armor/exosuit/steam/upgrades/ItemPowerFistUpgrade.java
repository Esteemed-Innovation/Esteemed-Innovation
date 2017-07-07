package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemPowerFistUpgrade extends ItemSteamExosuitUpgrade {
    public ItemPowerFistUpgrade() {
        super(ExosuitSlot.BODY_HAND, resource("fireFist"), null, 0);
    }

    @Override
    public void onPlayerAttacksOther(LivingAttackEvent event, EntityPlayer attacker, ItemStack armorStack, EntityEquipmentSlot slot) {
        boolean hasPower = ChargableUtility.hasPower(attacker, Config.powerFistConsumption);
        ItemStack stack = attacker.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (hasPower && attacker.getHeldItemMainhand() == null) {
            Entity victim = event.getEntity();
            World world = victim.world;
            world.playSound(victim.posX, victim.posY, victim.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE,
              SoundCategory.PLAYERS, 4F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F, false);
            Vec3d normalizedLookVec = attacker.getLookVec().normalize();
            victim.motionX += 3.0F * normalizedLookVec.x;
            victim.motionY += (normalizedLookVec.y > 0.0F ? 2.0F * normalizedLookVec.y : 0.0F) + 1.5F;
            victim.motionZ += 3.0F * normalizedLookVec.z;
            victim.motionX += -0.5F * normalizedLookVec.x;
            victim.motionZ += -0.5F * normalizedLookVec.z;
            ChargableUtility.drainSteam(stack, Config.powerFistConsumption, attacker);
        }
    }
}
