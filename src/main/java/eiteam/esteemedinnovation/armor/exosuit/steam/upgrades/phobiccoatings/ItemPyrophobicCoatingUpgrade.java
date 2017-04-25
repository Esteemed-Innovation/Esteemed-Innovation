package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.phobiccoatings;

import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.util.EntityHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.UUID;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemPyrophobicCoatingUpgrade extends ItemPhobicCoatingUpgrade {
    private static final UUID MODIFIER_ID = UUID.fromString("ecc2b61a-b8ca-4411-9158-e4d365e3ca7c");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_ID, "EXOLAVABOOST", 0.4D, 2).setSaved(true);

    public ItemPyrophobicCoatingUpgrade() {
        super(resource("pyrophobiccoating"), MODIFIER, MODIFIER_ID, Material.LAVA);
    }

    @Override
    protected int getConsumption() {
        return Config.pyrophobicConsumption;
    }

    @Override
    protected void beforeFluidWalk(EntityPlayer player) {
        if (player.isBurning()) {
            player.extinguish();
            player.motionY = 0.5;
        }
    }

    @Override
    protected void afterFluidWalk(EntityPlayer player) {
        if (player.isInLava()) {
            player.motionY = 0.5;
//            player.jump();
            player.fallDistance = 0;
        }
    }

    @Override
    public void onPlayerHurt(LivingHurtEvent event, EntityPlayer victim, ItemStack armorStack, EntityEquipmentSlot slot) {
        DamageSource source = event.getSource();
        if (isHotDamageSource(source) && isInHotness(victim)) {
            event.getEntity().motionY = 0.5D;
            event.setCanceled(true);
        }
    }

    private boolean isHotDamageSource(DamageSource source) {
        return source == DamageSource.lava || source == DamageSource.inFire || source == DamageSource.onFire;
    }

    private boolean isInHotness(Entity entity) {
        return entity.isInLava() || entity.isBurning() || EntityHelper.getBlockUnderEntity(entity).getMaterial() == Material.LAVA;
    }
}
