package eiteam.esteemedinnovation.armor.exosuit.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitArmor;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class PhobicCoatingHandler {
    private static final UUID uuid4 = UUID.fromString("ddb705e1-f95d-40e7-8cda-1be73a4739a1");
    private static final AttributeModifier exoWaterBoost = new AttributeModifier(uuid4, "EXOWATERBOOST", 0.4D, 2).setSaved(true);
    private static final UUID uuid5 = UUID.fromString("ecc2b61a-b8ca-4411-9158-e4d365e3ca7c");
    private static final AttributeModifier exoLavaBoost = new AttributeModifier(uuid5, "EXOLAVABOOST", 0.4D, 2).setSaved(true);
    private boolean isWalkingInLava = false;

    @SubscribeEvent
    public void preventLavaDamage(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if (isWalkingInLava && (source == DamageSource.lava || source == DamageSource.inFire ||
          source == DamageSource.onFire)) {
            event.getEntity().motionY = 0.5D;
            event.setCanceled(true);
        }
    }

    private boolean isJumping = false;

    @SubscribeEvent
    public void walkOnFluid(TickEvent.PlayerTickEvent event) {
        EntityPlayer entity = event.player;
        int consumptionHydro = Config.hydrophobicConsumption;
        int consumptionPyro = Config.pyrophobicConsumption;
        IAttributeInstance attributes = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        AttributeModifier modifierWater = attributes.getModifier(uuid4);
        AttributeModifier modifierLava = attributes.getModifier(uuid5);
        boolean canHydro = canWalkOnFluid(entity, consumptionHydro,
          ExosuitUpgradeItems.Items.HYDROPHOBIC_COATINGS.getItem(), modifierWater);
        boolean canPyro = canWalkOnFluid(entity, consumptionPyro,
          ExosuitUpgradeItems.Items.PYROPHOBIC_COATINGS.getItem(), modifierLava);
        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.getEntityBoundingBox().minY - 0.11F);
        int z = MathHelper.floor_double(entity.posZ);
        BlockPos underPos = new BlockPos(x, y, z);
        IBlockState underState = entity.worldObj.getBlockState(underPos);
        if (event.side == Side.CLIENT) {
            isJumping = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
        }

        if (canHydro) {
            if (underState.getMaterial() == Material.WATER) {
                entity.fallDistance = 0;

                entity.motionY = isJumping ? 0.5D : 0;

                if (modifierWater == null) {
                    attributes.applyModifier(exoWaterBoost);
                }
                ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (chest != null) {
                    Item chestItem = chest.getItem();
                    if (chestItem instanceof ExosuitArmor) {
                        ((ExosuitArmor) chestItem).drainSteam(chest, consumptionHydro);
                    }
                }
            }

            if (entity.isInWater()) {
                entity.jump();
                entity.fallDistance = 0;
            }
        } else if (modifierWater != null) {
            attributes.removeModifier(exoWaterBoost);
        }


        if (canPyro) {
            if (entity.isBurning()) {
                isWalkingInLava = true;
                entity.extinguish();
                entity.motionY = 0.5;
            }

            if (underState.getMaterial() == Material.LAVA) {
                isWalkingInLava = true;
                entity.motionY = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() ? 0.5D : 0;

                if (modifierLava == null) {
                    attributes.applyModifier(exoLavaBoost);
                }
                ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (chest != null) {
                    Item chestItem = chest.getItem();
                    if (chestItem instanceof ExosuitArmor) {
                        ((ExosuitArmor) chestItem).drainSteam(chest, consumptionPyro);
                    }
                }
            }

            if (entity.isInLava()) {
                isWalkingInLava = true;
                entity.motionY = 0.5;
//                entity.jump();
                entity.fallDistance = 0;
            }
        } else if (modifierLava != null) {
            attributes.removeModifier(exoLavaBoost);
        }
    }

    /**
     * Gets whether the Entity can walk on fluids.
     * @param player The EntityPlayer testing against.
     * @param consumption The amount of steam required.
     * @param coating The Exo upgrade needed to walk on fluids.
     * @param modifier The AttributeModifier that cannot exist.
     * @return True if the player can walk on top of fluids.
     */
    private boolean canWalkOnFluid(EntityPlayer player, int consumption, Item coating, AttributeModifier modifier) {
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack feet = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (chest != null && feet != null && modifier == null) {
            Item chestItem = chest.getItem();
            Item feetItem = feet.getItem();
            if (feetItem instanceof ExosuitArmor && chestItem instanceof ExosuitArmor) {
                ExosuitArmor chestArmor = (ExosuitArmor) chestItem;
                ExosuitArmor feetArmor = (ExosuitArmor) feetItem;
                if (chestArmor.hasPower(chest, consumption) && feetArmor.hasUpgrade(feet, coating)) {
                    return true;
                }
            }
        }
        return false;
    }
}
